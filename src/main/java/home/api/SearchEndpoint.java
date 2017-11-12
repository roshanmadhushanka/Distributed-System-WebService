package home.api;

public class SearchEndpoint extends Endpoint {

    public SearchEndpoint(String ipAddress, int port) {
        super(ipAddress, port);
    }

    @Override
    public String getEndpoint() {
        return "http://" + ipAddress + ":" + String.valueOf(port) + "/search";
    }
}
