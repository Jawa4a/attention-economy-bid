package main.state;

import main.dataModel.AuctionInput;
import main.dataModel.Summary;

import java.util.HashMap;
import java.util.Map;

public final class StatsTracker {

    private final Map<String, Integer> seenVideoCategories = new HashMap<>();
    private final Map<String, Integer> seenInterestHits = new HashMap<>();

    private int auctionsSeen;
    private int subscribedViewersSeen;
    private int matchedVideoCategorySeen;
    private int firstInterestMatches;
    private int secondInterestMatches;
    private int thirdInterestMatches;

    private double averageEfficiency;
    private int efficiencySamples;

    public void recordAuction(AuctionInput input, String chosenCategory) {
        if (input == null) {
            return;
        }

        auctionsSeen++;

        String videoCategory = input.getVideo().getCategory();
        seenVideoCategories.merge(videoCategory, 1, Integer::sum);

        if (input.getViewer().isSubscribed()) {
            subscribedViewersSeen++;
        }

        if (chosenCategory.equals(videoCategory)) {
            matchedVideoCategorySeen++;
        }

        String[] interests = input.getViewer().getInterests();
        for (int i = 0; i < interests.length; i++) {
            String interest = interests[i];
            seenInterestHits.merge(interest, 1, Integer::sum);

            if (chosenCategory.equals(interest)) {
                if (i == 0) {
                    firstInterestMatches++;
                } else if (i == 1) {
                    secondInterestMatches++;
                } else if (i == 2) {
                    thirdInterestMatches++;
                }
            }
        }
    }

    public void applySummary(Summary summary, BotState state) {
        if (summary == null) {
            return;
        }

        state.applySummary(summary.getPoints(), summary.getSpent());

        double efficiency = summary.getSpent() > 0
                ? (double) summary.getPoints() / summary.getSpent()
                : 0.0;

        efficiencySamples++;
        averageEfficiency += (efficiency - averageEfficiency) / efficiencySamples;

        if (summary.getSpent() == 0) {
            state.multiplyAggression(1.15);
        } else if (efficiency >= averageEfficiency * 1.10 && efficiency > 0.0) {
            state.multiplyAggression(1.05);
        } else if (efficiency <= averageEfficiency * 0.85) {
            state.multiplyAggression(0.93);
        }
    }

    public int getSeenCountForVideoCategory(String category) {
        return seenVideoCategories.getOrDefault(category, 0);
    }
}
