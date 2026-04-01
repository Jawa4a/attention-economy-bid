package main.strategy;

import main.dataModel.AuctionInput;
import main.dataModel.Bid;
import main.state.BotState;
import main.state.BudgetManager;
import main.state.StatsTracker;

public final class BidStrategy {
   private final ValueEstimator valueEstimator;
   private final BudgetManager budgetManager;

   public BidStrategy(ValueEstimator var1, BudgetManager var2, int var3) {
      this.valueEstimator = var1;
      this.budgetManager = var2;
   }

   public Bid decideBid(AuctionInput var1, BotState var2, StatsTracker var3) {
      if (var1 != null && this.budgetManager.canBid()) {
         double var4 = this.valueEstimator.estimate(var1, var2, var3);
         double var6 = var2.getSpentRatio();
         double var8 = var3.getRecentWinRate();
         int var10 = var2.getRoundNumber();
         double var11 = (double)1.0F;
         if (var10 < 100) {
            var11 *= 1.3;
         }

         if (var6 < 0.2) {
            var11 *= (double)1.5F;
         } else if (var6 < 0.3) {
            var11 *= 1.2;
         } else if (var6 > 0.8) {
            var11 *= 0.7;
         }

         if (var8 < (double)0.25F) {
            var11 *= 1.2;
         } else if (var8 > 0.6) {
            var11 *= 0.85;
         }

         double var13 = var4 * var11;
         int var15;
         int var16;
         if (var13 >= (double)13.0F) {
            var15 = 12;
            var16 = 36;
         } else if (var13 >= (double)10.0F) {
            var15 = 8;
            var16 = 24;
         } else if (var13 >= (double)7.0F) {
            var15 = 5;
            var16 = 14;
         } else if (var13 >= (double)5.0F) {
            var15 = 3;
            var16 = 8;
         } else if (var13 >= (double)3.0F) {
            var15 = 2;
            var16 = 5;
         } else {
            var15 = 1;
            var16 = 5;
         }

         if (var3.getWinSamples() > 50 && var8 < (double)0.25F) {
            var16 += 2;
         }

         if (var6 < 0.15 && var10 > 200) {
            var16 += 2;
         }

         if (var3.getWinSamples() > 20) {
            double var17 = var3.getAvgWinCost();
            int var19 = (int)(var17 * 1.4);
            if (var13 > var17 * (double)2.0F) {
               var19 = (int)(var17 * 2.2);
            }

            var16 = (int)(0.7 * (double)var16 + 0.3 * (double)var19);
         }

         if (var6 < 0.3) {
            if (var13 < (double)3.0F) {
               var15 = Math.max(var15, 1);
               var16 = Math.max(var16, 4);
            } else if (var13 < (double)6.0F) {
               var16 += 4;
            } else {
               var16 += 8;
               var15 += 2;
            }
         }

         int var20 = var2.getRemainingBudget();
         if (var16 > var20) {
            var16 = var20;
         }

         if (var15 > var16) {
            var15 = var16;
         }

         if (var16 < 2 && var20 >= 2) {
            var16 = 2;
         }

         if (var15 < 1 && var16 > 0) {
            var15 = 1;
         }

         return new Bid(var15, var16);
      } else {
         return new Bid(0, 0);
      }
   }

   public void applySummary(long var1, long var3, int var5) {
   }
}
