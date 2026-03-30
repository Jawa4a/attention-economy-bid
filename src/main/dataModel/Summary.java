package main.dataModel;

public class Summary {
    private final long points;
    private final long spent;

    public Summary(long points, long spent) {
        this.points = points;
        this.spent = spent;
    }

    public long getPoints() {
        return points;
    }

    public long getSpent() {
        return spent;
    }
}
