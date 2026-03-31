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

   public AuctionBot(int initialBudget) {
      CategoryPicker categoryPicker = new CategoryPicker();
      String chosenCategory = categoryPicker.chooseCategory();
      this.state = new BotState(initialBudget, chosenCategory);
      this.parser = new InputParser();
      this.statsTracker = new StatsTracker();
      this.budgetManager = new BudgetManager(this.state);
      this.valueEstimator = new ValueEstimator();
      this.bidStrategy = new BidStrategy(this.valueEstimator, this.budgetManager, initialBudget);
   }

   public void run() throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      PrintWriter out = new PrintWriter(System.out, true);
      out.println(this.state.getChosenCategory());

      while(true) {
         String line;
         do {
            if ((line = reader.readLine()) == null) {
               return;
            }

            line = line.trim();
         } while(line.isEmpty());

         try {
            this.handleLine(line, out);
         } catch (Exception var5) {
            System.err.println("Failed to handle line: " + line);
            System.err.println("Reason: " + var5.getMessage());
            if (this.parser.isAuctionLine(line)) {
               out.println("0 0");
            }
         }
      }
   }

   private void handleLine(String line, PrintWriter out) {
      if (this.parser.isAuctionLine(line)) {
         this.handleAuctionLine(line, out);
      } else if (this.parser.isWinLine(line)) {
         this.handleWinLine(line);
      } else if (this.parser.isLossLine(line)) {
         this.handleLossLine();
      } else if (this.parser.isSummaryLine(line)) {
         this.handleSummaryLine(line);
      } else {
         System.err.println("Unknown input line ignored: " + line);
      }
   }

   private void handleAuctionLine(String line, PrintWriter out) {
      AuctionInput input = this.parser.parseAuctionLine(line);
      this.state.incrementRound();
      this.state.setLastAuctionCategory(input.getVideo().getCategory());
      Bid bid = this.bidStrategy.decideBid(input, this.state, this.statsTracker);
      this.statsTracker.recordAuction(input, this.state.getChosenCategory());
      Bid safeBid = this.budgetManager.clampToBudget(bid);
      int var10001 = safeBid.getStartBid();
      out.println(var10001 + " " + safeBid.getMaxBid());
   }

   private void handleWinLine(String line) {
      int cost = this.parser.parseWinCost(line);
      this.state.recordWin(cost);
      this.statsTracker.recordWin();
   }

   private void handleLossLine() {
      this.state.recordLoss();
      this.statsTracker.recordLoss();
   }

   private void handleSummaryLine(String line) {
      Summary summary = this.parser.parseSummaryLine(line);
      this.statsTracker.applySummary(summary, this.state);
      this.bidStrategy.applySummary(summary.getPoints(), summary.getSpent(), this.statsTracker.getRecentWins());
   }
}
