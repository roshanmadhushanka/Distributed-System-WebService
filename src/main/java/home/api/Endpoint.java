package home.api;

public abstract class Endpoint {
    protected String ipAddress;
    protected int port;

    public Endpoint(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public abstract String getEndpoint();

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
}
