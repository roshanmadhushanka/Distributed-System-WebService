package home.stat;

public class JoinQueryStat {
    private long sentTime;
    private long receivedTime;

    public JoinQueryStat() {
    }

    public JoinQueryStat(long sentTime, long receivedTime) {
        this.sentTime = sentTime;
        this.receivedTime = receivedTime;
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

    public void display() {
        long elapsed = receivedTime - sentTime;
        System.out.println(sentTime + "\t" + receivedTime + "\t" + elapsed);
    }

}
