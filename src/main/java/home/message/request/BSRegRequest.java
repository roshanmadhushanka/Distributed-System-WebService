package home.message.request;

public class BSRegRequest {
    /*
        BSRegRequest
        ============
        Request to register the bootstrap server
        Connection : BootstrapConnection
     */

    private String ipAddress;
    private int port;
    private String userName;

    public BSRegRequest() {
    }

    public BSRegRequest(String ipAddress, int port, String userName) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        String message = "REG " + ipAddress + " " + String.valueOf(port) + " " + userName;
        int messageLength = message.length() + 5;
        String length = String.format("%04d", messageLength);
        message = length + " " + message;
        return message;
    }

}
