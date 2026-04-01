package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import main.dataModel.AuctionInput;
import main.dataModel.Bid;
import main.dataModel.Summary;
import main.parser.InputParser;
import main.state.BotState;
import main.state.BudgetManager;
import main.state.StatsTracker;
import main.strategy.BidStrategy;
import main.strategy.CategoryPicker;
import main.strategy.ValueEstimator;

public final class AuctionBot {
   private final BotState state;
   private final InputParser parser;
   private final StatsTracker statsTracker;
   private final BudgetManager budgetManager;
   private final ValueEstimator valueEstimator;
   private final BidStrategy bidStrategy;

   public AuctionBot(int var1) {
      CategoryPicker var2 = new CategoryPicker();
      String var3 = var2.chooseCategory();
      this.state = new BotState(var1, var3);
      this.parser = new InputParser();
      this.statsTracker = new StatsTracker();
      this.budgetManager = new BudgetManager(this.state);
      this.valueEstimator = new ValueEstimator();
      this.bidStrategy = new BidStrategy(this.valueEstimator, this.budgetManager, var1);
   }

   public void run() throws IOException {
      BufferedReader var1 = new BufferedReader(new InputStreamReader(System.in));
      PrintWriter var2 = new PrintWriter(System.out, true);
      var2.println(this.state.getChosenCategory());

      String var3;
      while((var3 = var1.readLine()) != null) {
         var3 = var3.trim();
         if (!var3.isEmpty()) {
            try {
               this.handleLine(var3, var2);
            } catch (Exception var5) {
               System.err.println("Failed to handle line: " + var3);
               System.err.println("Reason: " + var5.getMessage());
               if (this.parser.isAuctionLine(var3)) {
                  var2.println("0 0");
               }
            }
         }
      }

   }

   private void handleLine(String var1, PrintWriter var2) {
      if (this.parser.isAuctionLine(var1)) {
         this.handleAuctionLine(var1, var2);
      } else if (this.parser.isWinLine(var1)) {
         this.handleWinLine(var1);
      } else if (this.parser.isLossLine(var1)) {
         this.handleLossLine();
      } else if (this.parser.isSummaryLine(var1)) {
         this.handleSummaryLine(var1);
      } else {
         System.err.println("Unknown input line ignored: " + var1);
      }
   }

   private void handleAuctionLine(String var1, PrintWriter var2) {
      AuctionInput var3 = this.parser.parseAuctionLine(var1);
      this.state.incrementRound();
      this.state.setLastAuctionCategory(var3.getVideo().getCategory());
      Bid var4 = this.bidStrategy.decideBid(var3, this.state, this.statsTracker);
      this.statsTracker.recordAuction(var3, this.state.getChosenCategory());
      Bid var5 = this.budgetManager.clampToBudget(var4);
      int var10001 = var5.getStartBid();
      var2.println(var10001 + " " + var5.getMaxBid());
   }

   private void handleWinLine(String var1) {
      int var2 = this.parser.parseWinCost(var1);
      this.state.recordWin(var2);
      this.statsTracker.recordWin();
   }

   private void handleLossLine() {
      this.state.recordLoss();
      this.statsTracker.recordLoss();
   }

   private void handleSummaryLine(String var1) {
      Summary var2 = this.parser.parseSummaryLine(var1);
      this.statsTracker.applySummary(var2, this.state);
      this.bidStrategy.applySummary(var2.getPoints(), var2.getSpent(), this.statsTracker.getRecentWins());
   }
}
