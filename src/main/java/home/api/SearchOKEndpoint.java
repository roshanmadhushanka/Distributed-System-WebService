package home.api;

public class SearchOKEndpoint extends Endpoint {

    public SearchOKEndpoint(String ipAddress, int port) {
        super(ipAddress, port);
    }

    @Override
    public String getEndpoint() {
        return "http://" + ipAddress + ":" + String.valueOf(port) + "/searchOK";
    }
}
