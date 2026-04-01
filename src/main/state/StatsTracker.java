package main.state;

import java.util.HashMap;
import java.util.Map;
import main.dataModel.AuctionInput;
import main.dataModel.Summary;

public final class StatsTracker {
   private final Map<String, Integer> seenVideoCategories = new HashMap();
   private final Map<String, Integer> seenInterestHits = new HashMap();
   private int auctionsSeen;
   private int subscribedViewersSeen;
   private int matchedVideoCategorySeen;
   private int firstInterestMatches;
   private int secondInterestMatches;
   private int thirdInterestMatches;
   private int recentWins = 0;
   private int recentLosses = 0;
   private int roundsInWindow = 0;
   private static final int WINDOW_SIZE = 100;
   private double averageEfficiency;
   private int efficiencySamples;
   double avgWinCost = (double)8.0F;
   int winSamples = 0;

   public StatsTracker() {
   }

   public void recordAuction(AuctionInput var1, String var2) {
      if (var1 != null) {
         ++this.auctionsSeen;
         String var3 = var1.getVideo().getCategory();
         this.seenVideoCategories.merge(var3, 1, Integer::sum);
         if (var1.getViewer().isSubscribed()) {
            ++this.subscribedViewersSeen;
         }

         if (var2.equals(var3)) {
            ++this.matchedVideoCategorySeen;
         }

         String[] var4 = var1.getViewer().getInterests();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5];
            this.seenInterestHits.merge(var6, 1, Integer::sum);
            if (var2.equals(var6)) {
               if (var5 == 0) {
                  ++this.firstInterestMatches;
               } else if (var5 == 1) {
                  ++this.secondInterestMatches;
               } else if (var5 == 2) {
                  ++this.thirdInterestMatches;
               }
            }
         }

      }
   }

   public void applySummary(Summary var1, BotState var2) {
      if (var1 != null) {
         var2.applySummary(var1.getPoints(), var1.getSpent());
         double var3 = var1.getSpent() > 0L ? (double)var1.getPoints() / (double)var1.getSpent() : (double)0.0F;
         ++this.efficiencySamples;
         this.averageEfficiency += (var3 - this.averageEfficiency) / (double)this.efficiencySamples;
         if (var1.getSpent() == 0L) {
            var2.multiplyAggression(1.15);
         } else if (var3 >= this.averageEfficiency * 1.1 && var3 > (double)0.0F) {
            var2.multiplyAggression(1.05);
         } else if (var3 <= this.averageEfficiency * 0.85) {
            var2.multiplyAggression(0.93);
         }

      }
   }

   public int getSeenCountForVideoCategory(String var1) {
      return (Integer)this.seenVideoCategories.getOrDefault(var1, 0);
   }

   public double getRecentWinRate() {
      int var1 = this.recentWins + this.recentLosses;
      return var1 < 10 ? (double)0.5F : (double)this.recentWins / (double)var1;
   }

   public void recordWin() {
      ++this.recentWins;
      ++this.roundsInWindow;
      this.resetIfNeeded();
   }

   public void recordLoss() {
      ++this.recentLosses;
      ++this.roundsInWindow;
      this.resetIfNeeded();
   }

   private void resetIfNeeded() {
      if (this.roundsInWindow >= 100) {
         this.recentWins = 0;
         this.recentLosses = 0;
         this.roundsInWindow = 0;
      }

   }

   public int getRecentWins() {
      return this.recentWins;
   }

   public void recordWinCost(int var1) {
      this.avgWinCost += ((double)var1 - this.avgWinCost) / (double)(++this.winSamples);
   }

   public double getAvgWinCost() {
      return this.avgWinCost;
   }

   public int getWinSamples() {
      return this.winSamples;
   }
}
