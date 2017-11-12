package home.simulation;

import home.api.SearchEndpoint;
import home.io.CommunityConnection;
import home.io.Connection;
import home.message.request.SearchRequest;
import home.model.Neighbour;
import home.system.Configuration;
import home.table.FileTable;
import home.table.FileToLocationTable;
import java.util.Arrays;
import java.util.List;

public class RandomQueryGenerator extends Thread {
    private boolean isAlive;

    public RandomQueryGenerator() {
        isAlive = true;
    }

    private void sendQuery(String fileName) {
        List<String> files = FileTable.search(fileName);

        if(files.size() == 0) {
            // Load neighbours
            List<Neighbour> neighbours = Neighbour.getNeighbours();

            // Check file to locations for existing search results
            if(FileToLocationTable.hasLocation(fileName)) {
                for(Neighbour n: FileToLocationTable.getLocations(fileName)) {
                    neighbours.add(0, n);
                }
            }

            // Load system communication configurations
            String systemIPAddress = Configuration.getSystemIPAddress();
            int systemPort = Configuration.getSystemPort();

            // Initiate search request
            SearchRequest searchRequest = new SearchRequest(systemIPAddress, systemPort, fileName);

            // Establish connection with the community
            Connection connection = new Connection(3);

            for(Neighbour neighbour: neighbours) {
                // Create an endpoint to the target neighbour
                SearchEndpoint searchEndpoint = new SearchEndpoint(neighbour.getIpAddress(),
                        neighbour.getPort());

                connection.send(searchRequest, searchEndpoint);
                System.out.println("> Search");
                System.out.println("--------");
                System.out.println(searchRequest.toString());
                System.out.println(searchEndpoint.getEndpoint());
            }
        } else {
            System.out.println("Files : " + Arrays.toString(files.toArray()));
        }
    }

    @Override
    public void run() {
        while (isAlive) {
            try {
                sendQuery(Query.getRandomQuery());
                Thread.sleep(10000);
            } catch (InterruptedException e) {

            }
        }
    }
}
