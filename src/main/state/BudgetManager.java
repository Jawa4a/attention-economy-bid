package main.state;

import main.dataModel.Bid;

public final class BudgetManager {
   private final BotState state;

   public BudgetManager(BotState var1) {
      this.state = var1;
   }

   public boolean canBid() {
      return this.state.getRemainingBudget() > 0;
   }

   public int getSafeMaxBid() {
      int var1 = this.state.getRemainingBudget();
      if (var1 <= 0) {
         return 0;
      } else {
         double var2 = this.state.getSpentRatio();
         int var4 = this.state.getRoundNumber();
         if (var4 < 1000) {
            return Math.max(1, Math.min(var1, Math.max(var1 / 2000, 40)));
         } else if (var2 < 0.08) {
            return Math.max(1, Math.min(var1, Math.max(var1 / 1200, 80)));
         } else if (var2 < 0.2) {
            return Math.max(1, Math.min(var1, Math.max(var1 / 1800, 60)));
         } else {
            return var2 > 0.92 ? Math.max(1, Math.min(var1, Math.max(var1 / 8, 10))) : Math.max(1, Math.min(var1, Math.max(var1 / 2500, 50)));
         }
      }
   }

   public Bid clampToBudget(Bid var1) {
      if (var1 != null && this.state.getRemainingBudget() > 0) {
         int var2 = this.state.getRemainingBudget();
         int var3 = Math.max(0, Math.min(var1.getMaxBid(), var2));
         int var4 = Math.max(0, Math.min(var1.getStartBid(), var3));
         return new Bid(var4, var3);
      } else {
         return new Bid(0, 0);
      }
   }

   public Bid capBySafeMax(Bid var1) {
      if (var1 != null && this.state.getRemainingBudget() > 0) {
         int var2 = Math.min(this.getSafeMaxBid(), this.state.getRemainingBudget());
         int var3 = Math.max(0, Math.min(var1.getMaxBid(), var2));
         int var4 = Math.max(0, Math.min(var1.getStartBid(), var3));
         return new Bid(var4, var3);
      } else {
         return new Bid(0, 0);
      }
   }

   public int getUrgencyBoostPercent() {
      double var1 = this.state.getSpentRatio();
      int var3 = this.state.getRoundNumber();
      if (var3 > 5000 && var1 < 0.1) {
         return 30;
      } else if (var3 > 2000 && var1 < 0.07) {
         return 20;
      } else {
         return var3 > 1000 && var1 < 0.05 ? 10 : 0;
      }
   }
}
