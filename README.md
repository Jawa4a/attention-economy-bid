# TaskPvP Auction Bot

Automated bidding bot for video viewer auctions. It uses a modular architecture to parse auction input, score viewers, and manage a budget across thousands of rounds.

## Features
- **Dynamic Scoring**: Evaluates viewers based on interests, category match, and video engagement.
- **Adaptive Aggression**: Adjusts bidding aggressiveness based on historical performance (efficiency).
- **Budget Management**: Prevents premature depletion of funds using "safe" bidding ceilings.
- **Urgency Boost**: Increases bidding activity if falling behind spending targets late in the run.

## Project Structure
- `src/main/Main.java`: Entry point.
- `src/main/AuctionBot.java`: Orchestrates input/output and strategy.
- `src/main/strategy/`: Contains `ValueEstimator` (scoring) and `BidStrategy`.
- `src/main/state/`: Tracks bot budget and performance stats.
- `src/main/parser/`: Converts text protocol to Java objects.
- `src/test/`: Contains `LocalHarness` for simulation.

## Usage
Run the bot by providing an initial budget as an argument:
```cmd
java -cp out/production/TaskPvP main.Main 1000000
```
The bot communicates via `System.in` and `System.out` following the auction protocol.
