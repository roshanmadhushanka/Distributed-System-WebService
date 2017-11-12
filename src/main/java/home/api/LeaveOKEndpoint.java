package home.api;

public class LeaveOKEndpoint extends Endpoint {

    public LeaveOKEndpoint(String ipAddress, int port) {
        super(ipAddress, port);
    }

    @Override
    public String getEndpoint() {
        return "http://" + ipAddress + ":" + String.valueOf(port) + "/leaveOK";
    }
}
