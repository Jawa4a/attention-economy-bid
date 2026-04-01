import main.AuctionBot;

final class Main {
   Main() {
   }

   void main(String[] var1) throws Exception {
      if (var1 != null && var1.length == 1) {
         int var2;
         try {
            var2 = Integer.parseInt(var1[0]);
         } catch (NumberFormatException var4) {
            System.err.println("Invalid budget: " + var1[0]);
            return;
         }

         if (var2 < 0) {
            System.err.println("Budget must be non-negative.");
         } else {
            AuctionBot var3 = new AuctionBot(var2);
            var3.run();
         }
      } else {
         System.err.println("Expected exactly one argument: initial budget");
      }
   }
}
