package main.strategy;

import main.dataModel.AuctionInput;
import main.dataModel.Bid;
import main.state.BotState;
import main.state.BudgetManager;
import main.state.StatsTracker;

public final class BidStrategy {

    private final ValueEstimator valueEstimator;
    private final BudgetManager budgetManager;

    public BidStrategy(ValueEstimator valueEstimator, BudgetManager budgetManager) {
        this.valueEstimator = valueEstimator;
        this.budgetManager = budgetManager;
    }

    public Bid decideBid(AuctionInput input, BotState state, StatsTracker stats) {
        if (input == null || !budgetManager.canBid()) {
            return new Bid(0, 0);
        }

        double rawScore = valueEstimator.estimate(input, state, stats);
        double adjustedScore = rawScore * state.getAggressionMultiplier();

        int start;
        int max;

        if (adjustedScore >= 10.0) {
            start = 12;
            max = 40;
        } else if (adjustedScore >= 8.0) {
            start = 8;
            max = 25;
        } else if (adjustedScore >= 6.0) {
            start = 4;
            max = 12;
        }
        else if (adjustedScore >= 4.0) {
            start = 2;
            max = 6;
        }
        else if (adjustedScore >= 2.5) {
            start = 1;
            max = 3;
        }
        else if (adjustedScore >= 1.5) {
            start = 1;
            max = 2;
        }
        else {
            start = 0;
            max = 1;
        }

        double spentRatio = state.getSpentRatio();

        if (spentRatio < 0.15) {
            if (max <= 6) {
                max += 2;
            } else {
                max = (int)(max * 1.2);
            }

            if (start > 0) {
                start += 1;
            }
        }
        else if (spentRatio < 0.30) {
            if (max <= 6) {
                max += 1;
            } else {
                max = (int)(max * 1.1);
            }
        }
        else if (spentRatio > 0.85) {
            start = (int)(start * 0.8);
            max = (int)(max * 0.8);
        }

        if (state.getRoundNumber() > 50 && spentRatio < 0.4) {
            max += 1;
            if (start > 0) {
                start += 1;
            }
        }

        int remaining = state.getRemainingBudget();

        if (max > remaining) {
            max = remaining;
        }

        if (start > max) {
            start = max;
        }

        if (max > 0 && start == 0) {
            start = 1;
        }

        return new Bid(start, max);
    }
}