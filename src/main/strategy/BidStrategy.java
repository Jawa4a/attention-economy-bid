package main.strategy;

import main.dataModel.AuctionInput;
import main.dataModel.Bid;
import main.state.BotState;
import main.state.BudgetManager;
import main.state.StatsTracker;

public final class BidStrategy {

    private final ValueEstimator valueEstimator;
    private final BudgetManager budgetManager;

    private double efficiency = 1.0;
    private int skipThreshold = 50;

    private final int totalExpectedRounds;

    public BidStrategy(ValueEstimator valueEstimator, BudgetManager budgetManager, int initialBudget) {
        this.valueEstimator = valueEstimator;
        this.budgetManager = budgetManager;
        this.totalExpectedRounds = Math.max(100_001, initialBudget / 10);
    }

    public Bid decideBid(AuctionInput input, BotState state, StatsTracker stats) {
        if (input == null || !budgetManager.canBid()) {
            return new Bid(0, 0);
        }

        double value = valueEstimator.estimate(input, state, stats);

        if (Double.isNaN(value) || Double.isInfinite(value) || value < 0) {
            return new Bid(1, 1);
        }

        double aggression = state.getAggressionMultiplier();

        if (stats.getRecentWinRate() < 0.15) {
            aggression *= 1.15;
        }

        else if (stats.getRecentWinRate() > 0.6) {
            aggression *= 0.9;
        }

        if (aggression < 0.5) aggression = 0.5;
        if (aggression > 2.5) aggression = 2.5;

        state.setAggressionMultiplier(aggression);

        double adjusted = value * aggression;

        int start;
        int max;

        if (adjusted >= 10) {
            start = 10;
            max = 30;
        } else if (adjusted >= 8) {
            start = 6;
            max = 18;
        } else if (adjusted >= 6) {
            start = 3;
            max = 10;
        } else if (adjusted >= 4) {
            start = 2;
            max = 6;
        } else if (adjusted >= 2.5) {
            start = 1;
            max = 3;
        } else {
            start = 1;
            max = 2;
        }

        double spentRatio = state.getSpentRatio();

        if (spentRatio < 0.20) {
            max += 2;
            if (start > 0) start += 1;
        }
        else if (spentRatio < 0.35) {
            max += 1;
        }
        else if (spentRatio > 0.85) {
            // conserve
            start = (int)(start * 0.7);
            max = (int)(max * 0.7);
        }

        if (state.getRoundNumber() > 50 && spentRatio < 0.4) {
            max += 1;
        }

        int remaining = state.getRemainingBudget();

        if (max > remaining) max = remaining;
        if (start > max) start = max;

        if (max > 0 && start == 0) start = 1;

        return new Bid(start, max);
    }

    public void applySummary(long points, long spent, int recentWins) {
        double pointsPerEbuck = points / Math.max((double) spent, 1.0);

        if (pointsPerEbuck < efficiency * 0.85) {
            skipThreshold = (int) Math.ceil(skipThreshold * 1.1);
            efficiency *= 0.95;
        } else if (recentWins < 20) {
            efficiency *= 1.1;
        }

        if (skipThreshold < 1) {
            skipThreshold = 1;
        } else if (skipThreshold > 10_000) {
            skipThreshold = 10_000;
        }

        if (efficiency < 0.25) {
            efficiency = 0.25;
        } else if (efficiency > 3.0) {
            efficiency = 3.0;
        }
    }

    public double getEfficiency() {
        return efficiency;
    }

    public int getSkipThreshold() {
        return skipThreshold;
    }
}