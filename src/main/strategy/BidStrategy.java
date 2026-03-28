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

        Bid bid;
        if (adjustedScore >= 10.0) {
            bid = new Bid(20, 80);
        } else if (adjustedScore >= 8.0) {
            bid = new Bid(12, 45);
        } else if (adjustedScore >= 6.0) {
            bid = new Bid(6, 20);
        } else if (adjustedScore >= 4.0) {
            bid = new Bid(2, 7);
        } else if (adjustedScore >= 3.0) {
            bid = new Bid(1, 3);
        } else {
            bid = new Bid(0, 0);
        }

        int urgencyBoostPercent = budgetManager.getUrgencyBoostPercent();
        if (urgencyBoostPercent > 0 && bid.getMaxBid() > 0) {
            int boostedStart = bid.getStartBid() + Math.max(1, bid.getStartBid() * urgencyBoostPercent / 100);
            int boostedMax = bid.getMaxBid() + Math.max(1, bid.getMaxBid() * urgencyBoostPercent / 100);
            bid = new Bid(boostedStart, boostedMax);
        }

        bid = budgetManager.clampToBudget(bid);
        bid = budgetManager.capBySafeMax(bid);

        if (bid.getMaxBid() > 0 && bid.getStartBid() == 0) {
            bid = new Bid(1, bid.getMaxBid());
        }

        return bid;
    }
}