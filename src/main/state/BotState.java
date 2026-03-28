package main.state;

public final class BotState {

    private final int initialBudget;
    private final String chosenCategory;

    private int remainingBudget;
    private int roundNumber;
    private int wins;
    private int losses;

    private long totalSpent;
    private long totalSummaryPoints;

    private long currentBlockPoints;
    private long currentBlockSpent;
    private int summariesSeen;

    private double aggressionMultiplier;
    private double lastEfficiency;
    private String lastAuctionCategory;

    public BotState(int initialBudget, String chosenCategory) {
        if (initialBudget < 0) {
            throw new IllegalArgumentException("Initial budget cannot be negative.");
        }
        if (chosenCategory == null || chosenCategory.isBlank()) {
            throw new IllegalArgumentException("Chosen category cannot be blank.");
        }

        this.initialBudget = initialBudget;
        this.chosenCategory = chosenCategory;
        this.remainingBudget = initialBudget;
        this.aggressionMultiplier = 1.0;
        this.lastEfficiency = 0.0;
        this.lastAuctionCategory = "";
    }

    public String getChosenCategory() {
        return chosenCategory;
    }

    public int getRemainingBudget() {
        return remainingBudget;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public double getAggressionMultiplier() {
        return aggressionMultiplier;
    }

    public void setLastAuctionCategory(String lastAuctionCategory) {
        this.lastAuctionCategory = lastAuctionCategory == null ? "" : lastAuctionCategory;
    }

    public void incrementRound() {
        this.roundNumber++;
    }

    public void recordWin(int cost) {
        int safeCost = Math.max(0, cost);
        this.wins++;
        this.remainingBudget = Math.max(0, this.remainingBudget - safeCost);
        this.totalSpent += safeCost;
    }

    public void recordLoss() {
        this.losses++;
    }

    public void applySummary(long points, long spent) {
        this.currentBlockPoints = Math.max(0L, points);
        this.currentBlockSpent = Math.max(0L, spent);
        this.totalSummaryPoints += this.currentBlockPoints;
        this.summariesSeen++;

        if (this.currentBlockSpent > 0) {
            this.lastEfficiency = (double) this.currentBlockPoints / this.currentBlockSpent;
        } else {
            this.lastEfficiency = 0.0;
        }
    }

    public void multiplyAggression(double factor) {
        this.aggressionMultiplier *= factor;
        clampAggression();
    }

    public double getSpentRatio() {
        if (initialBudget == 0) {
            return 1.0;
        }
        return (double) (initialBudget - remainingBudget) / initialBudget;
    }

    private void clampAggression() {
        if (this.aggressionMultiplier < 0.50) {
            this.aggressionMultiplier = 0.50;
        } else if (this.aggressionMultiplier > 2.25) {
            this.aggressionMultiplier = 2.25;
        }
    }
}