package home.api;

public class SimulateEndpoint extends  Endpoint{
    public SimulateEndpoint(String ipAddress, int port) {
        super(ipAddress, port);
    }

    @Override
    public String getEndpoint() {
        return "http://" + ipAddress + ":" + String.valueOf(port) + "/simulate";
    }
}
