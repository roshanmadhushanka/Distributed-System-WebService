package home.table;

import home.model.Neighbour;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileToLocationTable {
    private static HashMap<String, List<Neighbour>> fileToLocation = new HashMap<>();

    private static boolean hasNeighbour(String fileName, Neighbour neighbour){
        List<Neighbour> neighbours = fileToLocation.get(fileName);
        for(Neighbour n: neighbours) {
            if(n.getIpAddress().equals(neighbour.getIpAddress()) && n.getPort() == neighbour.getPort()) {
                return true;
            }
        }
        return false;
    }

    public static void add(String fileName, Neighbour neighbour) {
        /*
            Add location to a given file
         */

        if(fileToLocation.containsKey(fileName)) {
            if(!hasNeighbour(fileName, neighbour)) {
                fileToLocation.get(fileName).add(neighbour);
            }
        } else {
            List<Neighbour> neighbours = new ArrayList<>();
            neighbours.add(neighbour);
            fileToLocation.put(fileName, neighbours);
        }
    }

    public static boolean hasLocation(String fileName) {
        /*
            Check whether the given file has previous node locations
         */

        return fileToLocation.containsKey(fileName);
    }

    public static List<Neighbour> getLocations(String fileName) {
        /*
            Return available locations for a given file
         */

        return fileToLocation.get(fileName);
    }

    public static void removeLocation(String fileName, Neighbour neighbour) {
        /*
            Remove locations for a given file
         */

        if(fileToLocation.containsKey(fileName)) {
            if(fileToLocation.get(fileName).contains(neighbour)) {
                fileToLocation.get(fileName).remove(neighbour);
            }
        }
    }

    public static HashMap<String, List<Neighbour>> getFileToLocation() {
        /*
            Get file to location table
         */

        return fileToLocation;
    }
}
