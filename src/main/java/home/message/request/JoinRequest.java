package home.message.request;

import java.sql.Timestamp;

public class JoinRequest {
    /*
        JoinRequest
        ===========
        Request to join the community
        Connection : CommunityConnection
     */

    private String ipAddress;
    private int port;
    private long timestamp;

    public JoinRequest() {
    }

    public JoinRequest(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
