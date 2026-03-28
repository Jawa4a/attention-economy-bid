package main.state;

import main.dataModel.Bid;

public final class BudgetManager {

    private final BotState state;

    public BudgetManager(BotState state) {
        this.state = state;
    }

    public boolean canBid() {
        return state.getRemainingBudget() > 0;
    }

    public int getSafeMaxBid() {
        int remaining = state.getRemainingBudget();
        if (remaining <= 0) {
            return 0;
        }

        double spentRatio = state.getSpentRatio();
        int round = state.getRoundNumber();

        if (round < 1_000) {
            return Math.max(1, Math.min(remaining, Math.max(remaining / 2_000, 40)));
        }
        if (spentRatio < 0.08) {
            return Math.max(1, Math.min(remaining, Math.max(remaining / 1_200, 80)));
        }
        if (spentRatio < 0.20) {
            return Math.max(1, Math.min(remaining, Math.max(remaining / 1_800, 60)));
        }
        if (spentRatio > 0.92) {
            return Math.max(1, Math.min(remaining, Math.max(remaining / 8, 10)));
        }

        return Math.max(1, Math.min(remaining, Math.max(remaining / 2_500, 50)));
    }

    public Bid clampToBudget(Bid rawBid) {
        if (rawBid == null || state.getRemainingBudget() <= 0) {
            return new Bid(0, 0);
        }

        int remaining = state.getRemainingBudget();
        int maxBid = Math.max(0, Math.min(rawBid.getMaxBid(), remaining));
        int startBid = Math.max(0, Math.min(rawBid.getStartBid(), maxBid));

        return new Bid(startBid, maxBid);
    }

    public Bid capBySafeMax(Bid rawBid) {
        if (rawBid == null || state.getRemainingBudget() <= 0) {
            return new Bid(0, 0);
        }

        int safeMax = Math.min(getSafeMaxBid(), state.getRemainingBudget());
        int maxBid = Math.max(0, Math.min(rawBid.getMaxBid(), safeMax));
        int startBid = Math.max(0, Math.min(rawBid.getStartBid(), maxBid));

        return new Bid(startBid, maxBid);
    }

    public int getUrgencyBoostPercent() {
        double spentRatio = state.getSpentRatio();
        int round = state.getRoundNumber();

        if (round > 5_000 && spentRatio < 0.10) {
            return 30;
        }
        if (round > 2_000 && spentRatio < 0.07) {
            return 20;
        }
        if (round > 1_000 && spentRatio < 0.05) {
            return 10;
        }
        return 0;
    }
}