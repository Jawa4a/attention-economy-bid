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

   public int getInitialBudget() {
      return this.initialBudget;
   }

   public void setRemainingBudget(int remainingBudget) {
      this.remainingBudget = remainingBudget;
   }

   public void setRoundNumber(int roundNumber) {
      this.roundNumber = roundNumber;
   }

   public int getWins() {
      return this.wins;
   }

   public void setWins(int wins) {
      this.wins = wins;
   }

   public int getLosses() {
      return this.losses;
   }

   public void setLosses(int losses) {
      this.losses = losses;
   }

   public long getTotalSpent() {
      return this.totalSpent;
   }

   public void setTotalSpent(long totalSpent) {
      this.totalSpent = totalSpent;
   }

   public long getTotalSummaryPoints() {
      return this.totalSummaryPoints;
   }

   public void setTotalSummaryPoints(long totalSummaryPoints) {
      this.totalSummaryPoints = totalSummaryPoints;
   }

   public long getCurrentBlockPoints() {
      return this.currentBlockPoints;
   }

   public void setCurrentBlockPoints(long currentBlockPoints) {
      this.currentBlockPoints = currentBlockPoints;
   }

   public long getCurrentBlockSpent() {
      return this.currentBlockSpent;
   }

   public void setCurrentBlockSpent(long currentBlockSpent) {
      this.currentBlockSpent = currentBlockSpent;
   }

   public int getSummariesSeen() {
      return this.summariesSeen;
   }

   public void setSummariesSeen(int summariesSeen) {
      this.summariesSeen = summariesSeen;
   }

   public void setAggressionMultiplier(double aggressionMultiplier) {
      this.aggressionMultiplier = aggressionMultiplier;
      this.clampAggression();
   }

   public String getLastAuctionCategory() {
      return this.lastAuctionCategory;
   }

   public BotState(int initialBudget, String chosenCategory) {
      if (initialBudget < 0) {
         throw new IllegalArgumentException("Initial budget cannot be negative.");
      } else if (chosenCategory != null && !chosenCategory.isBlank()) {
         this.initialBudget = initialBudget;
         this.chosenCategory = chosenCategory;
         this.remainingBudget = initialBudget;
         this.aggressionMultiplier = 1.0D;
         this.lastEfficiency = 0.0D;
         this.lastAuctionCategory = "";
      } else {
         throw new IllegalArgumentException("Chosen category cannot be blank.");
      }
   }

   public String getChosenCategory() {
      return this.chosenCategory;
   }

   public int getRemainingBudget() {
      return this.remainingBudget;
   }

   public int getRoundNumber() {
      return this.roundNumber;
   }

   public double getAggressionMultiplier() {
      return this.aggressionMultiplier;
   }

   public void setLastAuctionCategory(String lastAuctionCategory) {
      this.lastAuctionCategory = lastAuctionCategory == null ? "" : lastAuctionCategory;
   }

   public void incrementRound() {
      ++this.roundNumber;
   }

   public void recordWin(int cost) {
      int safeCost = Math.max(0, cost);
      ++this.wins;
      this.remainingBudget = Math.max(0, this.remainingBudget - safeCost);
      this.totalSpent += (long)safeCost;
   }

   public void recordLoss() {
      ++this.losses;
   }

   public void applySummary(long points, long spent) {
      this.currentBlockPoints = Math.max(0L, points);
      this.currentBlockSpent = Math.max(0L, spent);
      this.totalSummaryPoints += this.currentBlockPoints;
      ++this.summariesSeen;
      if (this.currentBlockSpent > 0L) {
         this.lastEfficiency = (double)this.currentBlockPoints / (double)this.currentBlockSpent;
      } else {
         this.lastEfficiency = 0.0D;
      }

   }

   public void multiplyAggression(double factor) {
      this.aggressionMultiplier *= factor;
      this.clampAggression();
   }

   public double getSpentRatio() {
      return this.initialBudget == 0 ? 1.0D : (double)(this.initialBudget - this.remainingBudget) / (double)this.initialBudget;
   }

   private void clampAggression() {
      if (this.aggressionMultiplier < 0.5D) {
         this.aggressionMultiplier = 0.5D;
      } else if (this.aggressionMultiplier > 2.25D) {
         this.aggressionMultiplier = 2.25D;
      }

   }

   public double getLastEfficiency() {
      return this.lastEfficiency;
   }

   public void setLastEfficiency(double lastEfficiency) {
      this.lastEfficiency = lastEfficiency;
   }
}
