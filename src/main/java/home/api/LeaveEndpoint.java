package home.api;

public class LeaveEndpoint extends Endpoint {

    public LeaveEndpoint(String ipAddress, int port) {
        super(ipAddress, port);
    }

    @Override
    public String getEndpoint() {
        return "http://" + ipAddress + ":" + String.valueOf(port) + "/leave";
    }
}
