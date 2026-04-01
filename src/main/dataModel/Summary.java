package main.dataModel;

public class Summary {
   private final long points;
   private final long spent;

   public Summary(long var1, long var3) {
      this.points = var1;
      this.spent = var3;
   }

   public long getPoints() {
      return this.points;
   }

   public long getSpent() {
      return this.spent;
   }
}
