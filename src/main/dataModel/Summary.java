package main.dataModel;

public class Summary {
    long points;
    long spent;

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
