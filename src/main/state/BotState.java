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

   public void setRemainingBudget(int var1) {
      this.remainingBudget = var1;
   }

   public void setRoundNumber(int var1) {
      this.roundNumber = var1;
   }

   public int getWins() {
      return this.wins;
   }

   public void setWins(int var1) {
      this.wins = var1;
   }

   public int getLosses() {
      return this.losses;
   }

   public void setLosses(int var1) {
      this.losses = var1;
   }

   public long getTotalSpent() {
      return this.totalSpent;
   }

   public void setTotalSpent(long var1) {
      this.totalSpent = var1;
   }

   public long getTotalSummaryPoints() {
      return this.totalSummaryPoints;
   }

   public void setTotalSummaryPoints(long var1) {
      this.totalSummaryPoints = var1;
   }

   public long getCurrentBlockPoints() {
      return this.currentBlockPoints;
   }

   public void setCurrentBlockPoints(long var1) {
      this.currentBlockPoints = var1;
   }

   public long getCurrentBlockSpent() {
      return this.currentBlockSpent;
   }

   public void setCurrentBlockSpent(long var1) {
      this.currentBlockSpent = var1;
   }

   public int getSummariesSeen() {
      return this.summariesSeen;
   }

   public void setSummariesSeen(int var1) {
      this.summariesSeen = var1;
   }

   public void setAggressionMultiplier(double var1) {
      this.aggressionMultiplier = var1;
      this.clampAggression();
   }

   public String getLastAuctionCategory() {
      return this.lastAuctionCategory;
   }

   public BotState(int var1, String var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Initial budget cannot be negative.");
      } else if (var2 != null && !var2.isBlank()) {
         this.initialBudget = var1;
         this.chosenCategory = var2;
         this.remainingBudget = var1;
         this.aggressionMultiplier = (double)1.0F;
         this.lastEfficiency = (double)0.0F;
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

   public void setLastAuctionCategory(String var1) {
      this.lastAuctionCategory = var1 == null ? "" : var1;
   }

   public void incrementRound() {
      ++this.roundNumber;
   }

   public void recordWin(int var1) {
      int var2 = Math.max(0, var1);
      ++this.wins;
      this.remainingBudget = Math.max(0, this.remainingBudget - var2);
      this.totalSpent += (long)var2;
   }

   public void recordLoss() {
      ++this.losses;
   }

   public void applySummary(long var1, long var3) {
      this.currentBlockPoints = Math.max(0L, var1);
      this.currentBlockSpent = Math.max(0L, var3);
      this.totalSummaryPoints += this.currentBlockPoints;
      ++this.summariesSeen;
      if (this.currentBlockSpent > 0L) {
         this.lastEfficiency = (double)this.currentBlockPoints / (double)this.currentBlockSpent;
      } else {
         this.lastEfficiency = (double)0.0F;
      }

   }

   public void multiplyAggression(double var1) {
      this.aggressionMultiplier *= var1;
      this.clampAggression();
   }

   public double getSpentRatio() {
      return this.initialBudget == 0 ? (double)1.0F : (double)(this.initialBudget - this.remainingBudget) / (double)this.initialBudget;
   }

   private void clampAggression() {
      if (this.aggressionMultiplier < (double)0.5F) {
         this.aggressionMultiplier = (double)0.5F;
      } else if (this.aggressionMultiplier > (double)2.25F) {
         this.aggressionMultiplier = (double)2.25F;
      }

   }

   public double getLastEfficiency() {
      return this.lastEfficiency;
   }

   public void setLastEfficiency(double var1) {
      this.lastEfficiency = var1;
   }
}
