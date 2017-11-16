package home.stat;

public class SearchQueryStat {
    private long sentTime;
    private long receivedTime;
    private int hopsCount;

    public SearchQueryStat() {
    }

    public SearchQueryStat(long sentTime, long receivedTime, int hopsCount) {
        this.sentTime = sentTime;
        this.receivedTime = receivedTime;
        this.hopsCount = hopsCount;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public int getHopsCount() {
        return hopsCount;
    }

    public void setHopsCount(int hopsCount) {
        this.hopsCount = hopsCount;
    }

    public void display() {
        long elapsed = receivedTime - sentTime;
        System.out.println(sentTime + "\t" + receivedTime + "\t" + hopsCount + "\t" + elapsed);
    }
}
