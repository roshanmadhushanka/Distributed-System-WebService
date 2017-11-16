package home.message.response;

import java.util.Arrays;
import java.util.List;

public class SearchOKResponse {
    /*
        SearchOKResponse
        ======================
        Response to SearchRequest
        Receiver : SearchOK Controller
     */

    private String ipAddress;
    private int port;
    private int value;
    private long timestamp;
    private String query;
    private int hopsCount;
    private List<String> fileNames;

    public SearchOKResponse() {
    }

    public SearchOKResponse(String ipAddress, int port, int value, long timestamp, String query, int hopsCount, List<String> fileNames) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.value = value;
        this.timestamp = timestamp;
        this.query = query;
        this.hopsCount = hopsCount;
        this.fileNames = fileNames;
    }

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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public int getHopsCount() {
        return hopsCount;
    }

    public void setHopsCount(int hopsCount) {
        this.hopsCount = hopsCount;
    }

    @Override
    public String toString() {
        String files = "[]";

        if(fileNames != null) {
            files = Arrays.toString(fileNames.toArray());
        }

        return "SEARCHOK;" + ipAddress + ";" + String.valueOf(port) + ";" + String.valueOf(value) + ";" +
                String.valueOf(timestamp) + ";" + query + ";" + files;
    }
}
