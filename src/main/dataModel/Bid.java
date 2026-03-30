package main.dataModel;

public class Bid {
    private final int startBid;
    private final int maxBid;

    public Bid(int startBid, int maxBid) {
        this.startBid = startBid;
        this.maxBid = maxBid;
    }

    public int getStartBid() {
        return startBid;
    }
    public int getMaxBid() {
        return maxBid;
    }
}
