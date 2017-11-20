package home.api;

public class StatEndpoint extends Endpoint {

    public StatEndpoint(String ipAddress, int port) {
        super(ipAddress, port);
    }

    @Override
    public String getEndpoint() {
        return "http://" + ipAddress + ":" + String.valueOf(port) + "/stat";
    }
}
