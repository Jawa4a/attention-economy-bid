package main.dataModel;

public class Video {
    private final String category;
    private final long viewCount;
    private final long commentCount;

    public Video(String category, long viewCount, long commentCount) {
        this.category = category;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
    }

    public String getCategory() {
        return category;
    }

    public long getViewCount() {
        return viewCount;
    }

    public long getCommentCount() {
        return commentCount;
    }
}
