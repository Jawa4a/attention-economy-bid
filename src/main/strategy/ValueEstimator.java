package main.strategy;

import main.dataModel.AuctionInput;
import main.dataModel.Video;
import main.dataModel.Viewer;
import main.state.BotState;
import main.state.StatsTracker;

public final class ValueEstimator {

    public double estimate(AuctionInput input, BotState state, StatsTracker stats) {
        if (input == null) {
            return 0.0;
        }

        Video video = input.getVideo();
        Viewer viewer = input.getViewer();
        String chosenCategory = state.getChosenCategory();

        double score = 0.0;

        if (chosenCategory.equals(video.getCategory())) {
            score += 3.0;
        }

        String[] interests = viewer.getInterests();
        for (int i = 0; i < interests.length; i++) {
            if (chosenCategory.equals(interests[i])) {
                if (i == 0) {
                    score += 5.0;
                } else if (i == 1) {
                    score += 3.0;
                } else {
                    score += 1.5;
                }
            }
        }

        if (viewer.isSubscribed()) {
            score += 2.0;
        }

        double engagement = (double) video.getCommentCount() / Math.max(1L, video.getViewCount());
        if (engagement >= 0.080) {
            score += 3.0;
        } else if (engagement >= 0.030) {
            score += 2.0;
        } else if (engagement >= 0.010) {
            score += 1.0;
        }

        String age = viewer.getAge();
        if ("18-24".equals(age) || "25-34".equals(age)) {
            score += 0.5;
        }

        if (stats.getSeenCountForVideoCategory(video.getCategory()) > 5
                && chosenCategory.equals(video.getCategory())) {
            score += 0.25;
        }

        if (state.getRoundNumber() > 1_500 && state.getSpentRatio() < 0.05) {
            score += 0.5;
        }

        return Math.max(0.0, score);
    }
}