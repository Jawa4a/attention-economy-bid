package main.strategy;

import main.dataModel.AuctionInput;
import main.dataModel.Bid;
import main.state.BotState;
import main.state.BudgetManager;
import main.state.StatsTracker;

public final class BidStrategy {
   private final ValueEstimator valueEstimator;
   private final BudgetManager budgetManager;
   private double efficiency = 1.0D;
   private int skipThreshold = 50;
   private final int totalExpectedRounds;

   public BidStrategy(ValueEstimator valueEstimator, BudgetManager budgetManager, int initialBudget) {
      this.valueEstimator = valueEstimator;
      this.budgetManager = budgetManager;
      this.totalExpectedRounds = Math.max(100001, initialBudget / 10);
   }

   public Bid decideBid(AuctionInput input, BotState state, StatsTracker stats) {
      if (input != null && this.budgetManager.canBid()) {
         double value = this.valueEstimator.estimate(input, state, stats);
         if (!Double.isNaN(value) && !Double.isInfinite(value) && !(value < 0.0D)) {
            double aggression = state.getAggressionMultiplier();
            if (stats.getRecentWinRate() < 0.15D) {
               aggression *= 1.15D;
            } else if (stats.getRecentWinRate() > 0.6D) {
               aggression *= 0.9D;
            }

            if (aggression < 0.5D) {
               aggression = 0.5D;
            }

            if (aggression > 2.5D) {
               aggression = 2.5D;
            }

            state.setAggressionMultiplier(aggression);
            double adjusted = value * aggression;
            int start;
            int max;
            if (adjusted >= 10.0D) {
               start = 10;
               max = 30;
            } else if (adjusted >= 8.0D) {
               start = 6;
               max = 18;
            } else if (adjusted >= 6.0D) {
               start = 3;
               max = 10;
            } else if (adjusted >= 4.0D) {
               start = 2;
               max = 6;
            } else if (adjusted >= 2.5D) {
               start = 1;
               max = 3;
            } else {
               start = 1;
               max = 2;
            }

            double spentRatio = state.getSpentRatio();
            if (spentRatio < 0.2D) {
               max += 2;
               if (start > 0) {
                  ++start;
               }
            } else if (spentRatio < 0.35D) {
               ++max;
            } else if (spentRatio > 0.85D) {
               start = (int)((double)start * 0.7D);
               max = (int)((double)max * 0.7D);
            }

            if (state.getRoundNumber() > 50 && spentRatio < 0.4D) {
               ++max;
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
         } else {
            return new Bid(1, 1);
         }
      } else {
         return new Bid(0, 0);
      }
   }

   public void applySummary(long points, long spent, int recentWins) {
      double pointsPerEbuck = (double)points / Math.max((double)spent, 1.0D);
      if (pointsPerEbuck < this.efficiency * 0.85D) {
         this.skipThreshold = (int)Math.ceil((double)this.skipThreshold * 1.1D);
         this.efficiency *= 0.95D;
      } else if (recentWins < 20) {
         this.efficiency *= 1.1D;
      }

      if (this.skipThreshold < 1) {
         this.skipThreshold = 1;
      } else if (this.skipThreshold > 10000) {
         this.skipThreshold = 10000;
      }

      if (this.efficiency < 0.25D) {
         this.efficiency = 0.25D;
      } else if (this.efficiency > 3.0D) {
         this.efficiency = 3.0D;
      }

   }

   public double getEfficiency() {
      return this.efficiency;
   }

   public int getSkipThreshold() {
      return this.skipThreshold;
   }
}
