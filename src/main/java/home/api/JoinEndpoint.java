package home.api;

public class JoinEndpoint extends Endpoint {

    public JoinEndpoint(String ipAddress, int port) {
        super(ipAddress, port);
    }

    @Override
    public String getEndpoint() {
        return "http://" + ipAddress + ":" + String.valueOf(port) + "/join";
    }
}
