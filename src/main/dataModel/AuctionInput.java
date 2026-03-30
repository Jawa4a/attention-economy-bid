package main.dataModel;

public class AuctionInput {
    private final Video video;
    private final Viewer viewer;

    public AuctionInput(Video video, Viewer viewer) {
        this.video = video;
        this.viewer = viewer;
    }

    public Video getVideo() {
        return video;
    }
    public Viewer getViewer() {
        return viewer;
    }
}
