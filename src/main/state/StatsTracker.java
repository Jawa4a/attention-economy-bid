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

   public void recordAuction(AuctionInput input, String chosenCategory) {
      if (input != null) {
         ++this.auctionsSeen;
         String videoCategory = input.getVideo().getCategory();
         this.seenVideoCategories.merge(videoCategory, 1, Integer::sum);
         if (input.getViewer().isSubscribed()) {
            ++this.subscribedViewersSeen;
         }

         if (chosenCategory.equals(videoCategory)) {
            ++this.matchedVideoCategorySeen;
         }

         String[] interests = input.getViewer().getInterests();

         for(int i = 0; i < interests.length; ++i) {
            String interest = interests[i];
            this.seenInterestHits.merge(interest, 1, Integer::sum);
            if (chosenCategory.equals(interest)) {
               if (i == 0) {
                  ++this.firstInterestMatches;
               } else if (i == 1) {
                  ++this.secondInterestMatches;
               } else if (i == 2) {
                  ++this.thirdInterestMatches;
               }
            }
         }

      }
   }

   public void applySummary(Summary summary, BotState state) {
      if (summary != null) {
         state.applySummary(summary.getPoints(), summary.getSpent());
         double efficiency = summary.getSpent() > 0L ? (double)summary.getPoints() / (double)summary.getSpent() : 0.0D;
         ++this.efficiencySamples;
         this.averageEfficiency += (efficiency - this.averageEfficiency) / (double)this.efficiencySamples;
         if (summary.getSpent() == 0L) {
            state.multiplyAggression(1.15D);
         } else if (efficiency >= this.averageEfficiency * 1.1D && efficiency > 0.0D) {
            state.multiplyAggression(1.05D);
         } else if (efficiency <= this.averageEfficiency * 0.85D) {
            state.multiplyAggression(0.93D);
         }

      }
   }

   public int getSeenCountForVideoCategory(String category) {
      return (Integer)this.seenVideoCategories.getOrDefault(category, 0);
   }

   public double getRecentWinRate() {
      int total = this.recentWins + this.recentLosses;
      return total < 10 ? 0.5D : (double)this.recentWins / (double)total;
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
}
