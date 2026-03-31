import main.AuctionBot;

final class Main {
   void main(String[] args) throws Exception {
      if (args != null && args.length == 1) {
         int initialBudget;
         try {
            initialBudget = Integer.parseInt(args[0]);
         } catch (NumberFormatException var4) {
            System.err.println("Invalid budget: " + args[0]);
            return;
         }

         if (initialBudget < 0) {
            System.err.println("Budget must be non-negative.");
         } else {
            AuctionBot bot = new AuctionBot(initialBudget);
            bot.run();
         }
      } else {
         System.err.println("Expected exactly one argument: initial budget");
      }
   }
}
