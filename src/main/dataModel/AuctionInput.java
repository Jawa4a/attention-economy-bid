package main.dataModel;

public class AuctionInput {
   private final Video video;
   private final Viewer viewer;

   public AuctionInput(Video var1, Viewer var2) {
      this.video = var1;
      this.viewer = var2;
   }

   public Video getVideo() {
      return this.video;
   }

   public Viewer getViewer() {
      return this.viewer;
   }
}
