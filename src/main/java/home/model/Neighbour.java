package home.model;

import home.api.Endpoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Neighbour {
    /*
        Neighbour
        =========
        Represent a neighbour node in the circle
     */


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

    public static synchronized void addNeighbour(Neighbour neighbour) {
        /*
            Add neighbour to the circle
         */

        if(!neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
        }
    }

    public static synchronized void removeNeighbour(Neighbour neighbour) {
        /*
            Remove neighbour from the circle
         */

        int i = 0;
        while(i < neighbours.size()) {
            if(neighbours.get(i).ipAddress.equals(neighbour.ipAddress) && neighbours.get(i).port == neighbour.port) {
                break;
            }
            i++;
        }

        if(i<neighbours.size()) {
            neighbours.remove(i);
        }
    }

    public static List<Neighbour> getNeighbours() {
        /*
            Get neighbours in the circle
        */

        return new ArrayList<>(neighbours);
    }

    public static boolean hasNeighbour(Neighbour neighbour) {
        /*
            Check whether a given neighbour exits within the circle
         */

        for(Neighbour n: neighbours) {
            if(n.ipAddress.equals(neighbour.ipAddress) && n.port == neighbour.port) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "@" + ipAddress + ":" + String.valueOf(port);
    }

    public static void display() {
        System.out.println("> Neighbours");
        System.out.println("------------");
        for(Neighbour neighbour: neighbours) {
            System.out.println(neighbour.toString());
        }
        System.out.println();
    }


}
