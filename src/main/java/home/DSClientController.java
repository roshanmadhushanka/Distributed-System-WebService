package home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import home.api.JoinOKEndpoint;
import home.api.LeaveOKEndpoint;
import home.api.SearchEndpoint;
import home.api.SearchOKEndpoint;
import home.buffers.MessageBuffer;
import home.buffers.SearchBuffer;
import home.gui.Main;
import home.io.BootstrapConnection;
import home.io.CommunityConnection;
import home.io.Connection;
import home.message.request.*;
import home.message.response.JoinOKResponse;
import home.message.response.LeaveOKResponse;
import home.message.response.SearchOKResponse;
import home.model.Neighbour;
import home.parser.MessageParser;
import home.stat.JoinQueryStat;
import home.stat.LeaveQueryStat;
import home.stat.SearchQueryStat;
import home.stat.Statistics;
import home.system.Configuration;
import home.table.FileTable;
import home.table.FileToLocationTable;
import home.table.SearchResponseTable;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DSClientController {

    private boolean debug = true;
    private int maxTimeout = 3;

    @CrossOrigin
    @RequestMapping(value = "/pulse", method = RequestMethod.GET)
    public ResponseEntity<String> pulse() {
        /*
            Heartbeat verification of the service
         */

        String response = null;
        response = "ACK";
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/bsReg", method = RequestMethod.GET)
    public ResponseEntity<String> bsRegInitiator() {
        /*
            Handle BSRegRequest
         */

        if(debug) {
            System.out.println("BS Reg");
        }

        String response = null;

        // Load system communication configurations
        String systemIPAddress = Configuration.getSystemIPAddress();
        int systemPort = Configuration.getSystemPort();
        String systemName = Configuration.getSystemName();

        // Generate reg message
        BSRegRequest bsRegRequest = new BSRegRequest(systemIPAddress, systemPort, systemName);

        // Create connection to the bootstrap server
        BootstrapConnection bootstrapConnection = new BootstrapConnection();

        // Send reg message and waiting for response
        response = bootstrapConnection.send(bsRegRequest.getMessage());

        // Send to message parser
        MessageParser.responseParser(response);

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/bsUnreg", method = RequestMethod.GET)
    public ResponseEntity<String> bsUnRegInitiator() {
        /*
            Handle BSUnRegRequest
         */

        if(debug) {
            System.out.println("BS Unreg");
        }

        String response = null;

        // Generate message
        BSUnRegRequest bsUnRegRequest = new BSUnRegRequest(Configuration.getSystemIPAddress(), Configuration.getSystemPort(),
                Configuration.getSystemName());

        // Create connection to the bootstrap server
        BootstrapConnection bootstrapConnection = new BootstrapConnection();

        // Send message and waiting for response
        response = bootstrapConnection.send(bsUnRegRequest.getMessage());

        // Send response to message parser
        MessageParser.responseParser(response);

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public ResponseEntity<String> join(@RequestBody JoinRequest joinRequest) {
        /*
            Handle JoinRequest
         */

        if(debug) {
            System.out.println(joinRequest.toString());
        }

        String response = null;

        // Add neighbour to the circle
        Neighbour neighbour = new Neighbour(joinRequest.getIpAddress(), joinRequest.getPort());
        Neighbour.addNeighbour(neighbour);

        // Load system communication configurations
        String systemIPAddress = Configuration.getSystemIPAddress();
        int systemPort = Configuration.getSystemPort();

        // Generate join ok response
        JoinOKResponse joinOKResponse = new JoinOKResponse(systemIPAddress, systemPort, 0,
                joinRequest.getTimestamp());

        // Create an endpoint to the receiver
        JoinOKEndpoint joinOKEndpoint = new JoinOKEndpoint(joinRequest.getIpAddress(), joinRequest.getPort());

        // Establish connection with the community
        Connection connection = new Connection(maxTimeout);

        // Send join ok response to the target client
        connection.send(joinOKResponse, joinOKEndpoint);

        // Stat - Begin
        Statistics.incrementSentMessages();
        // Stat - End

        // GUI
        Main.getForm().appendTerminal(joinRequest.toString());
        Main.getForm().updateNeighbours();

        response = "ACK";

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<String> search(@RequestBody SearchRequest searchRequest) {
        /*
            Handle SearchRequest
         */

        if(debug) {
            System.out.println(searchRequest.toString());
        }

        String response = null;

        if(!SearchResponseTable.isResponded(searchRequest)) {
            // If not responded to the request already

            // Extract file name from the search request
            String fileName = searchRequest.getFileName();

            // Run search query in file table
            List<String> fileList = FileTable.search(fileName);

            if(fileList.size() != 0) {
                // Search OK, send results

                // Load system communication configurations
                String systemIpAddress = Configuration.getSystemIPAddress();
                int systemPort = Configuration.getSystemPort();

                // Generate file not found response
                SearchOKResponse searchOKResponse = new SearchOKResponse();
                searchOKResponse.setQuery(searchRequest.getFileName());
                searchOKResponse.setTimestamp(searchOKResponse.getTimestamp());
                searchOKResponse.setValue(fileList.size());
                searchOKResponse.setIpAddress(systemIpAddress);
                searchOKResponse.setPort(systemPort);
                searchOKResponse.setFileNames(fileList);
                searchOKResponse.setTimestamp(searchRequest.getTimestamp());
                searchOKResponse.setHopsCount(searchRequest.getHops());

                // Establish connection with the community
                Connection connection = new Connection(maxTimeout);

                // Create search ok endpoint to the target client
                SearchOKEndpoint searchOKEndpoint = new SearchOKEndpoint(searchRequest.getIpAddress(),
                        searchRequest.getPort());

                // Send response to the client
                connection.send(searchOKResponse, searchOKEndpoint);

                // Stat - Begin
                Statistics.incrementSentMessages();
                // Stat - End
            }

            // File is not available int the system
            int hops = searchRequest.getHops();

            // Generate forward request
            SearchRequest forwardRequest = new SearchRequest();
            forwardRequest.setFileName(searchRequest.getFileName());
            forwardRequest.setIpAddress(searchRequest.getIpAddress());
            forwardRequest.setPort(searchRequest.getPort());
            forwardRequest.setTimestamp(searchRequest.getTimestamp());

            // Load neighbours
            List<Neighbour> neighbours = Neighbour.getNeighbours();

            // Check file to locations for existing search results
            if(FileToLocationTable.hasLocation(searchRequest.getFileName())) {
                for(Neighbour n: FileToLocationTable.getLocations(searchRequest.getFileName())) {
                    neighbours.add(0, n);
                }
            }

            // Remove originator
            Neighbour originator = new Neighbour(searchRequest.getIpAddress(), searchRequest.getPort());
            neighbours.remove(originator);

            // Establish connection with the community
            CommunityConnection communityConnection = new CommunityConnection();

            --hops;
            boolean hasNeighbours = false;

            if(hops > 0) {
                for(Neighbour neighbour: neighbours) {
                    forwardRequest.setHops(hops);

                    // Create an endpoint to the target neighbour
                    SearchEndpoint searchEndpoint = new SearchEndpoint(neighbour.getIpAddress(),
                            neighbour.getPort());

                    Connection connection = new Connection(maxTimeout);

                    // Send search request to neighbours
                    connection.send(forwardRequest, searchEndpoint);

                    // Stat - Begin
                    Statistics.incrementSentMessages();
                    // Stat - End
                }
            } else {
                // Hops count limit exceeded, send file not found message

                // Load system communication configurations
                String systemIpAddress = Configuration.getSystemIPAddress();
                int systemPort = Configuration.getSystemPort();

                // Generate file not found response
                SearchOKResponse searchOKResponse = new SearchOKResponse();
                searchOKResponse.setQuery(searchRequest.getFileName());
                searchOKResponse.setTimestamp(searchRequest.getTimestamp());
                searchOKResponse.setValue(0);
                searchOKResponse.setIpAddress(systemIpAddress);
                searchOKResponse.setPort(systemPort);
                searchOKResponse.setFileNames(new ArrayList<>());
                searchOKResponse.setHopsCount(hops);

                // Create search ok endpoint to the target client
                SearchOKEndpoint searchOKEndpoint = new SearchOKEndpoint(searchRequest.getIpAddress(),
                        searchRequest.getPort());

                Connection connection = new Connection(maxTimeout);

                // Send search not found to the sender
                connection.send(searchOKResponse, searchOKEndpoint);

                // Stat - Begin
                Statistics.incrementSentMessages();
                // Stat - End
            }
        }

        response = "ACK";

        // GUI
        Main.getForm().appendTerminal(searchRequest.toString());

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/leave", method = RequestMethod.POST)
    public ResponseEntity<String> leave(@RequestBody LeaveRequest leaveRequest) {
        /*
            Handle LeaveRequest
         */

        if(debug) {
            System.out.println(leaveRequest.toString());
        }

        String response = null;

        Neighbour neighbour = new Neighbour(leaveRequest.getIpAddress(), leaveRequest.getPort());
        Neighbour.removeNeighbour(neighbour);

        // Load system communication configurations
        String systemIPAddress = Configuration.getSystemIPAddress();
        int systemPort = Configuration.getSystemPort();

        // Generate leave ok response
        LeaveOKResponse leaveOKResponse = new LeaveOKResponse(systemIPAddress, systemPort, 0,
                leaveRequest.getTimestamp());

        // Create an endpoint to the receiver
        LeaveOKEndpoint leaveOKEndpoint = new LeaveOKEndpoint(leaveRequest.getIpAddress(), leaveRequest.getPort());

        // Establish connection with the community
        Connection connection = new Connection(maxTimeout);

        // Send leave ok response to the target client
        connection.send(leaveOKResponse, leaveOKEndpoint);

        // Stat - Begin
        Statistics.incrementSentMessages();
        // Stat - End

        // GUI
        Main.getForm().appendTerminal(leaveRequest.toString());
        Main.getForm().updateNeighbours();

        response = "ACK";

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public ResponseEntity download(@RequestBody DownloadRequest downloadRequest) {
        /*
            Handle DownloadRequest
         */

        if(debug) {
            System.out.println(downloadRequest.toString());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/joinOK", method = RequestMethod.POST)
    public ResponseEntity joinOK(@RequestBody JoinOKResponse joinOKResponse) {
        /*
            Handle JoinOKResponse
         */

        // Stat - Begin
        Statistics.incrementReceivedMessages();
        JoinQueryStat joinQueryStat = new JoinQueryStat();
        joinQueryStat.setSentTime(joinOKResponse.getTimestamp());
        joinQueryStat.setReceivedTime(new Timestamp(System.currentTimeMillis()).getTime());
        // Stat - End

        if(debug) {
            System.out.println(joinOKResponse.toString());
        }

        String response = null;

        Neighbour neighbour = new Neighbour(joinOKResponse.getIpAddress(), joinOKResponse.getPort());
        Neighbour.addNeighbour(neighbour);

        // GUI
        Main.getForm().appendTerminal(joinOKResponse.toString());
        Main.getForm().updateNeighbours();

        response = "ACK";

        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/searchOK", method = RequestMethod.POST)
    public ResponseEntity searchOK(@RequestBody SearchOKResponse searchOKResponse) {
        /*
            Handle SearchOKResponse
         */

        // Stat - Begin
        Statistics.incrementReceivedMessages();
        SearchQueryStat searchQueryStat = new SearchQueryStat();
        searchQueryStat.setHopsCount(searchOKResponse.getHopsCount());
        searchQueryStat.setSentTime(searchOKResponse.getTimestamp());
        searchQueryStat.setReceivedTime(new Timestamp(System.currentTimeMillis()).getTime());
        // Stat - End

        if(debug) {
            System.out.println(searchOKResponse.toString());
        }

        if(searchOKResponse.getValue() > 0) {
            // Create neighbour
            Neighbour neighbour = new Neighbour(searchOKResponse.getIpAddress(), searchOKResponse.getPort());

            List<String> files = searchOKResponse.getFileNames();

            // Add neighbour to the file to location table
            for(String file: files) {
                FileToLocationTable.add(file, neighbour);
            }

            // Add search OK Response to buffer
            SearchBuffer.put(searchOKResponse);
        }

        // GUI
        Main.getForm().appendTerminal(searchOKResponse.toString());
        Main.getForm().appendSearch(searchOKResponse);
        Main.getForm().updateFileDestinations();

        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/leaveOK", method = RequestMethod.POST)
    public ResponseEntity leaveOK(@RequestBody LeaveOKResponse leaveOKResponse) {
        /*
            Handle LeaveOKResponse
         */

        // Stat - Begin
        Statistics.incrementReceivedMessages();
        LeaveQueryStat leaveQueryStat = new LeaveQueryStat();
        leaveQueryStat.setSentTime(leaveOKResponse.getTimestamp());
        leaveQueryStat.setReceivedTime(new Timestamp(System.currentTimeMillis()).getTime());
        // Stat - End

        if(debug) {
            System.out.println(leaveOKResponse.toString());
        }

        Neighbour neighbour = new Neighbour(leaveOKResponse.getIpAddress(), leaveOKResponse.getPort());
        Neighbour.removeNeighbour(neighbour);

        // GUI
        Main.getForm().appendTerminal(leaveOKResponse.toString());
        Main.getForm().updateNeighbours();

        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/getFiles", method = RequestMethod.GET)
    public ResponseEntity<String> files() {
        /*
            Give files
         */

        String response = null;

        // Convert result in to JSON
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            response = objectMapper.writeValueAsString(FileTable.getFileList());
        } catch (JsonProcessingException e) {
            response = "Error";
        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/getNeighbours", method = RequestMethod.GET)
    public ResponseEntity<String> getNeighbours() {
        /*
            Get neighbours
         */

        String response = null;

        // Convert result in to JSON
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            response = objectMapper.writeValueAsString(Neighbour.getNeighbours());
        } catch (JsonProcessingException e) {
            response = "Error";
        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/getSearchResults", method = RequestMethod.GET)
    public ResponseEntity<String> getSearchResults() {
        /*
            Get search results from buffer
         */

        String response = null;

        // Convert result in to JSON
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            response = objectMapper.writeValueAsString(SearchBuffer.get());
        } catch (JsonProcessingException e) {
            response = "Error";
        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/getMessages", method = RequestMethod.GET)
    public ResponseEntity<String> getMessages() {
        /*
            Get messages from the buffer
         */

        String response = null;

        // Convert result in to JSON
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            response = objectMapper.writeValueAsString(MessageBuffer.get());
        } catch (JsonProcessingException e) {
            response = "Error";
        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/getSystemInfo")
    public ResponseEntity<String> getSystemInfo() {
        /*
            Get system info
         */

        String response = null;

        Document systemInfo = new Document();
        systemInfo.put("ipAddress", Configuration.getSystemIPAddress());
        systemInfo.put("port", Configuration.getSystemPort());

        // Convert result in to JSON
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            response = objectMapper.writeValueAsString(systemInfo);
        } catch (JsonProcessingException e) {
            response = "Error";
        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
}
