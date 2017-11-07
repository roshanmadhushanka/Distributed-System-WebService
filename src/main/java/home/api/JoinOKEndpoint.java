package home.api;

public class JoinOKEndpoint extends Endpoint {
    public JoinOKEndpoint(String ipAddress, int port) {
        super(ipAddress, port);
    }

    @Override
    public String getEndpoint() {
        return "http://" + ipAddress + ":" + String.valueOf(port) + "/joinOK";
    }
}
