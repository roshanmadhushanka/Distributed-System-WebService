package home.message.request;

import java.sql.Timestamp;

public class SearchRequest {
    /*
        SearchRequest
        =============
        Request to search for a file within the community
        Connection : CommunityConnection
     */

    private String ipAddress;
    private int port;
    private String fileName;
    private int hops;
    private long timestamp;

    private final static int maxHops = 5;

    public SearchRequest() {
    }

    public SearchRequest(String ipAddress, int port, String fileName) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.fileName = fileName;
        this.hops = maxHops;
        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SEARCH;" + ipAddress + ";" + String.valueOf(port) + ";" + fileName + ";" + String.valueOf(timestamp);
    }
}
