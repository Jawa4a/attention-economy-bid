package main.dataModel;

public class Bid {
   private final int startBid;
   private final int maxBid;

   public Bid(int var1, int var2) {
      this.startBid = var1;
      this.maxBid = var2;
   }

   public int getStartBid() {
      return this.startBid;
   }

   public int getMaxBid() {
      return this.maxBid;
   }
}
