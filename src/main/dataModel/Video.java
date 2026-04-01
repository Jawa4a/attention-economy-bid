package main.dataModel;

public class Video {
   private final String category;
   private final long viewCount;
   private final long commentCount;

   public Video(String var1, long var2, long var4) {
      this.category = var1;
      this.viewCount = var2;
      this.commentCount = var4;
   }

   public String getCategory() {
      return this.category;
   }

   public long getViewCount() {
      return this.viewCount;
   }

   public long getCommentCount() {
      return this.commentCount;
   }
}
