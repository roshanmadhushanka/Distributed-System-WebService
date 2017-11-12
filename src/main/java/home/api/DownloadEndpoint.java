package home.api;

public class DownloadEndpoint extends Endpoint {
    public DownloadEndpoint(String ipAddress, int port) {
        super(ipAddress, port);
    }

    @Override
    public String getEndpoint() {
        return "http://" + ipAddress + ":" + String.valueOf(port) + "/download";
    }
}
