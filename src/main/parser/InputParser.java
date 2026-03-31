package main.parser;

import java.util.Arrays;
import main.dataModel.AuctionInput;
import main.dataModel.Summary;
import main.dataModel.Video;
import main.dataModel.Viewer;

public final class InputParser {
   public boolean isAuctionLine(String line) {
      return line != null && line.contains("video.") && line.contains("viewer.");
   }

   public boolean isWinLine(String line) {
      return line != null && line.startsWith("W ");
   }

   public boolean isLossLine(String line) {
      return "L".equals(line);
   }

   public boolean isSummaryLine(String line) {
      return line != null && line.startsWith("S ");
   }

   public AuctionInput parseAuctionLine(String line) {
      if (!this.isAuctionLine(line)) {
         throw new IllegalArgumentException("Not an auction line: " + line);
      } else {
         String videoCategory = null;
         long viewCount = 0L;
         long commentCount = 0L;
         boolean subscribed = false;
         String age = "";
         String gender = "";
         String[] interests = new String[0];
         String[] parts = line.split(",");
         String[] var12 = parts;
         int var13 = parts.length;

         for(int var14 = 0; var14 < var13; ++var14) {
            String part = var12[var14];
            int eq = part.indexOf(61);
            if (eq > 0 && eq != part.length() - 1) {
               String key = part.substring(0, eq).trim();
               String value = part.substring(eq + 1).trim();
               byte var20 = -1;
               switch(key.hashCode()) {
               case -1350681821:
                  if (key.equals("viewer.age")) {
                     var20 = 4;
                  }
                  break;
               case 603658678:
                  if (key.equals("viewer.subscribed")) {
                     var20 = 3;
                  }
                  break;
               case 699979181:
                  if (key.equals("viewer.interests")) {
                     var20 = 6;
                  }
                  break;
               case 973802627:
                  if (key.equals("video.commentCount")) {
                     var20 = 2;
                  }
                  break;
               case 1119778065:
                  if (key.equals("video.category")) {
                     var20 = 0;
                  }
                  break;
               case 1483493303:
                  if (key.equals("video.viewCount")) {
                     var20 = 1;
                  }
                  break;
               case 1556762141:
                  if (key.equals("viewer.gender")) {
                     var20 = 5;
                  }
               }

               switch(var20) {
               case 0:
                  videoCategory = value;
                  break;
               case 1:
                  viewCount = this.parseLongSafe(value);
                  break;
               case 2:
                  commentCount = this.parseLongSafe(value);
                  break;
               case 3:
                  subscribed = "Y".equalsIgnoreCase(value);
                  break;
               case 4:
                  age = value;
                  break;
               case 5:
                  gender = value;
                  break;
               case 6:
                  if (!value.isEmpty()) {
                     interests = (String[])Arrays.stream(value.split(";")).map(String::trim).filter((s) -> {
                        return !s.isEmpty();
                     }).toArray((x$0) -> {
                        return new String[x$0];
                     });
                  }
               }
            }
         }

         if (videoCategory == null) {
            throw new IllegalArgumentException("Missing video.category in line: " + line);
         } else {
            Video video = new Video(videoCategory, viewCount, commentCount);
            Viewer viewer = new Viewer(subscribed, age, gender, interests);
            return new AuctionInput(video, viewer);
         }
      }
   }

   public int parseWinCost(String line) {
      if (!this.isWinLine(line)) {
         throw new IllegalArgumentException("Not a win line: " + line);
      } else {
         String value = line.substring(2).trim();
         return this.parseIntSafe(value);
      }
   }

   public Summary parseSummaryLine(String line) {
      if (!this.isSummaryLine(line)) {
         throw new IllegalArgumentException("Not a summary line: " + line);
      } else {
         String[] parts = line.trim().split("\\s+");
         if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid summary line: " + line);
         } else {
            long points = this.parseLongSafe(parts[1]);
            long spent = this.parseLongSafe(parts[2]);
            return new Summary(points, spent);
         }
      }
   }

   private int parseIntSafe(String value) {
      try {
         return Integer.parseInt(value);
      } catch (NumberFormatException var3) {
         return 0;
      }
   }

   private long parseLongSafe(String value) {
      try {
         return Long.parseLong(value);
      } catch (NumberFormatException var3) {
         return 0L;
      }
   }
}
