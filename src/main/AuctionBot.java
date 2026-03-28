package main;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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
        this.budgetManager = new BudgetManager(state);
        this.valueEstimator = new ValueEstimator();
        this.bidStrategy = new BidStrategy(valueEstimator, budgetManager);
    }

    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out, true);

        out.println(state.getChosenCategory());

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            try {
                handleLine(line, out);
            } catch (Exception e) {
                System.err.println("Failed to handle line: " + line);
                System.err.println("Reason: " + e.getMessage());

                if (parser.isAuctionLine(line)) {
                    out.println("0 0");
                }
            }
        }
    }

    private void handleLine(String line, PrintWriter out) {
        if (parser.isAuctionLine(line)) {
            handleAuctionLine(line, out);
            return;
        }

        if (parser.isWinLine(line)) {
            handleWinLine(line);
            return;
        }

        if (parser.isLossLine(line)) {
            handleLossLine();
            return;
        }

        if (parser.isSummaryLine(line)) {
            handleSummaryLine(line);
            return;
        }

        System.err.println("Unknown input line ignored: " + line);
    }

    private void handleAuctionLine(String line, PrintWriter out) {
        AuctionInput input = parser.parseAuctionLine(line);

        state.incrementRound();
        state.setLastAuctionCategory(input.getVideo().getCategory());
        statsTracker.recordAuction(input, state.getChosenCategory());

        Bid bid = bidStrategy.decideBid(input, state, statsTracker);

        int startBid = Math.max(0, bid.getStartBid());
        int maxBid = Math.max(0, bid.getMaxBid());

        if (startBid > maxBid) {
            startBid = maxBid;
        }

        if (maxBid > state.getRemainingBudget()) {
            maxBid = state.getRemainingBudget();
        }

        if (startBid > maxBid) {
            startBid = maxBid;
        }

        out.println(startBid + " " + maxBid);
    }

    private void handleWinLine(String line) {
        int cost = parser.parseWinCost(line);
        state.recordWin(cost);
    }

    private void handleLossLine() {
        state.recordLoss();
    }

    private void handleSummaryLine(String line) {
        Summary summary = parser.parseSummaryLine(line);
        statsTracker.applySummary(summary, state);
    }
}