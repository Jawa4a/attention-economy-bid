package main.strategy;

import main.dataModel.AuctionInput;
import main.dataModel.Video;
import main.dataModel.Viewer;
import main.state.BotState;
import main.state.StatsTracker;

public final class ValueEstimator {
   public double estimate(AuctionInput input, BotState state, StatsTracker stats) {
      if (input == null) {
         return 0.0D;
      } else {
         Video video = input.getVideo();
         Viewer viewer = input.getViewer();
         String chosenCategory = state.getChosenCategory();
         double score = 0.0D;
         if (chosenCategory.equals(video.getCategory())) {
            score += 3.0D;
         }

         String[] interests = viewer.getInterests();

         for(int i = 0; i < interests.length; ++i) {
            if (chosenCategory.equals(interests[i])) {
               if (i == 0) {
                  score += 5.0D;
               } else if (i == 1) {
                  score += 3.0D;
               } else {
                  ++score;
               }
            }
         }

         if (viewer.isSubscribed()) {
            score += 2.0D;
         }

         double engagement = (double)video.getCommentCount() / (double)Math.max(1L, video.getViewCount());
         if (engagement >= 0.08D) {
            score += 3.0D;
         } else if (engagement >= 0.03D) {
            score += 2.0D;
         } else if (engagement >= 0.01D) {
            ++score;
         }

         String age = viewer.getAge();
         if ("18-24".equals(age) || "25-34".equals(age)) {
            score += 0.5D;
         }

         if (stats.getSeenCountForVideoCategory(video.getCategory()) > 5 && chosenCategory.equals(video.getCategory())) {
            score += 0.25D;
         }

         if (state.getRoundNumber() > 1500 && state.getSpentRatio() < 0.05D) {
            score += 0.5D;
         }

         return Math.max(0.0D, score);
      }
   }
}
