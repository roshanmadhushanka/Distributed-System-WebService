package home.message.response;

public class JoinOKResponse {
    /*
        JinOKMessage
        ============
        Response to JoinRequest
        Receiver : Default Controller
     */

    private String ipAddress;
    private int port;
    private int status;
    private long timestamp;

    public JoinOKResponse() {
    }

    public JoinOKResponse(String ipAddress, int port, int status, long timestamp) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.status = status;
        this.timestamp = timestamp;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        return "JOINOK;" + ipAddress + ";" + String.valueOf(port) + ";" + String.valueOf(status) + ";" +
                String.valueOf(timestamp);
    }
}
