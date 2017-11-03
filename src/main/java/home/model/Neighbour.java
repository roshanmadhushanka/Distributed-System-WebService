package home.model;

import java.util.ArrayList;
import java.util.List;

public class Neighbour {

    private static List<Neighbour> neighbours = new ArrayList<>();

    private String ipAddress;
    private int port;

    public Neighbour(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public static void addNeighbour(Neighbour neighbour) {
        /*
            Add neighbour
         */

        if(!neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
        }
    }

    public static List<Neighbour> getNeighbours() {
        /*
            Get neighbours
        */

        return neighbours;
    }
}
