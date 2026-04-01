package main.parser;

import java.util.Arrays;
import main.dataModel.AuctionInput;
import main.dataModel.Summary;
import main.dataModel.Video;
import main.dataModel.Viewer;

public final class InputParser {
   public InputParser() {
   }

   public boolean isAuctionLine(String var1) {
      return var1 != null && var1.contains("video.") && var1.contains("viewer.");
   }

   public boolean isWinLine(String var1) {
      return var1 != null && var1.startsWith("W ");
   }

   public boolean isLossLine(String var1) {
      return "L".equals(var1);
   }

   public boolean isSummaryLine(String var1) {
      return var1 != null && var1.startsWith("S ");
   }

   public AuctionInput parseAuctionLine(String var1) {
      if (!this.isAuctionLine(var1)) {
         throw new IllegalArgumentException("Not an auction line: " + var1);
      } else {
         String var2 = null;
         long var3 = 0L;
         long var5 = 0L;
         boolean var7 = false;
         String var8 = "";
         String var9 = "";
         String[] var10 = new String[0];
         String[] var11 = var1.split(",");

         for(String var15 : var11) {
            int var16 = var15.indexOf(61);
            if (var16 > 0 && var16 != var15.length() - 1) {
               String var17 = var15.substring(0, var16).trim();
               String var18 = var15.substring(var16 + 1).trim();
               switch (var17) {
                  case "video.category":
                     var2 = var18;
                     break;
                  case "video.viewCount":
                     var3 = this.parseLongSafe(var18);
                     break;
                  case "video.commentCount":
                     var5 = this.parseLongSafe(var18);
                     break;
                  case "viewer.subscribed":
                     var7 = "Y".equalsIgnoreCase(var18);
                     break;
                  case "viewer.age":
                     var8 = var18;
                     break;
                  case "viewer.gender":
                     var9 = var18;
                     break;
                  case "viewer.interests":
                     if (!var18.isEmpty()) {
                        var10 = (String[])Arrays.stream(var18.split(";")).map(String::trim).filter((var0) -> !var0.isEmpty()).toArray((var0) -> new String[var0]);
                     }
               }
            }
         }

         if (var2 == null) {
            throw new IllegalArgumentException("Missing video.category in line: " + var1);
         } else {
            Video var21 = new Video(var2, var3, var5);
            Viewer var22 = new Viewer(var7, var8, var9, var10);
            return new AuctionInput(var21, var22);
         }
      }
   }

   public int parseWinCost(String var1) {
      if (!this.isWinLine(var1)) {
         throw new IllegalArgumentException("Not a win line: " + var1);
      } else {
         String var2 = var1.substring(2).trim();
         return this.parseIntSafe(var2);
      }
   }

   public Summary parseSummaryLine(String var1) {
      if (!this.isSummaryLine(var1)) {
         throw new IllegalArgumentException("Not a summary line: " + var1);
      } else {
         String[] var2 = var1.trim().split("\\s+");
         if (var2.length < 3) {
            throw new IllegalArgumentException("Invalid summary line: " + var1);
         } else {
            long var3 = this.parseLongSafe(var2[1]);
            long var5 = this.parseLongSafe(var2[2]);
            return new Summary(var3, var5);
         }
      }
   }

   private int parseIntSafe(String var1) {
      try {
         return Integer.parseInt(var1);
      } catch (NumberFormatException var3) {
         return 0;
      }
   }

   private long parseLongSafe(String var1) {
      try {
         return Long.parseLong(var1);
      } catch (NumberFormatException var3) {
         return 0L;
      }
   }
}
