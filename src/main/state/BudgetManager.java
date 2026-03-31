package main.state;

import main.dataModel.Bid;

public final class BudgetManager {
   private final BotState state;

   public BudgetManager(BotState state) {
      this.state = state;
   }

   public boolean canBid() {
      return this.state.getRemainingBudget() > 0;
   }

   public int getSafeMaxBid() {
      int remaining = this.state.getRemainingBudget();
      if (remaining <= 0) {
         return 0;
      } else {
         double spentRatio = this.state.getSpentRatio();
         int round = this.state.getRoundNumber();
         if (round < 1000) {
            return Math.max(1, Math.min(remaining, Math.max(remaining / 2000, 40)));
         } else if (spentRatio < 0.08D) {
            return Math.max(1, Math.min(remaining, Math.max(remaining / 1200, 80)));
         } else if (spentRatio < 0.2D) {
            return Math.max(1, Math.min(remaining, Math.max(remaining / 1800, 60)));
         } else {
            return spentRatio > 0.92D ? Math.max(1, Math.min(remaining, Math.max(remaining / 8, 10))) : Math.max(1, Math.min(remaining, Math.max(remaining / 2500, 50)));
         }
      }
   }

   public Bid clampToBudget(Bid rawBid) {
      if (rawBid != null && this.state.getRemainingBudget() > 0) {
         int remaining = this.state.getRemainingBudget();
         int maxBid = Math.max(0, Math.min(rawBid.getMaxBid(), remaining));
         int startBid = Math.max(0, Math.min(rawBid.getStartBid(), maxBid));
         return new Bid(startBid, maxBid);
      } else {
         return new Bid(0, 0);
      }
   }

   public Bid capBySafeMax(Bid rawBid) {
      if (rawBid != null && this.state.getRemainingBudget() > 0) {
         int safeMax = Math.min(this.getSafeMaxBid(), this.state.getRemainingBudget());
         int maxBid = Math.max(0, Math.min(rawBid.getMaxBid(), safeMax));
         int startBid = Math.max(0, Math.min(rawBid.getStartBid(), maxBid));
         return new Bid(startBid, maxBid);
      } else {
         return new Bid(0, 0);
      }
   }

   public int getUrgencyBoostPercent() {
      double spentRatio = this.state.getSpentRatio();
      int round = this.state.getRoundNumber();
      if (round > 5000 && spentRatio < 0.1D) {
         return 30;
      } else if (round > 2000 && spentRatio < 0.07D) {
         return 20;
      } else {
         return round > 1000 && spentRatio < 0.05D ? 10 : 0;
      }
   }
}
