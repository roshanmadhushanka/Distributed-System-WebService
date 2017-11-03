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
    private long timestamp;

    public SearchRequest() {
    }

    public SearchRequest(String ipAddress, int port, String fileName) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.fileName = fileName;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
