package main.strategy;

import main.dataModel.AuctionInput;
import main.dataModel.Video;
import main.dataModel.Viewer;
import main.state.BotState;
import main.state.StatsTracker;

public final class ValueEstimator {
   public ValueEstimator() {
   }

   public double estimate(AuctionInput var1, BotState var2, StatsTracker var3) {
      if (var1 == null) {
         return (double)0.0F;
      } else {
         Video var4 = var1.getVideo();
         Viewer var5 = var1.getViewer();
         String var6 = var2.getChosenCategory();
         double var7 = (double)0.0F;
         if (var6.equals(var4.getCategory())) {
            var7 += (double)2.5F;
         }

         String[] var9 = var5.getInterests();

         for(int var10 = 0; var10 < var9.length; ++var10) {
            if (var6.equals(var9[var10])) {
               if (var10 == 0) {
                  var7 += (double)4.5F;
               } else if (var10 == 1) {
                  var7 += (double)2.5F;
               } else {
                  ++var7;
               }
            }
         }

         if (var5.isSubscribed()) {
            ++var7;
         }

         String var16 = var5.getAge();
         if ("25-34".equals(var16)) {
            ++var7;
         } else if ("18-24".equals(var16)) {
            ++var7;
         } else if ("35-44".equals(var16)) {
            var7 += 0.7;
         } else if ("13-17".equals(var16) || "55+".equals(var16)) {
            var7 -= 0.2;
         }

         double var11 = (double)var4.getCommentCount() / (double)Math.max(1L, var4.getViewCount());
         if (var11 >= 0.08) {
            var7 += 2.8;
         } else if (var11 >= 0.04) {
            var7 += (double)2.0F;
         } else if (var11 >= 0.02) {
            ++var7;
         } else if (var11 >= 0.01) {
            var7 += (double)0.5F;
         }

         long var13 = var4.getViewCount();
         if (var13 < 1000L) {
            var7 += 0.3;
         } else if (var13 < 50000L) {
            ++var7;
         } else if (var13 < 500000L) {
            var7 += 2.6;
         } else if (var13 < 5000000L) {
            ++var7;
         } else if (var13 < 20000000L) {
            ++var7;
         } else {
            var7 += 0.6;
         }

         if (var6.equals(var4.getCategory()) && var3.getSeenCountForVideoCategory(var4.getCategory()) > 5) {
            var7 += 0.2;
         }

         if (var2.getRoundNumber() > 1000 && var2.getSpentRatio() < 0.08) {
            var7 += (double)0.5F;
         }

         if (var7 >= (double)10.0F) {
            var7 += (double)3.0F;
         } else if (var7 >= (double)8.0F) {
            ++var7;
         }

         return Math.max((double)0.0F, var7);
      }
   }
}
