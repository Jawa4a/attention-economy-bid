package main.dataModel;

public final class Bid {
    int startBid;
    int maxBid;

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
