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
import home.message.request.*;
import home.message.response.JoinOKResponse;
import home.message.response.LeaveOKResponse;
import home.message.response.SearchOKResponse;
import home.model.Neighbour;
import home.parser.MessageParser;
import home.system.Configuration;
import home.table.FileTable;
import home.table.FileToLocationTable;
import home.table.SearchResponseTable;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class DSClientController {

    private boolean debug = true;

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
        BSUnRegRequest bsUnRegRequest = new BSUnRegRequest(Configuration.getBsIpAddress(), Configuration.getSystemPort(),
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
        CommunityConnection communityConnection = new CommunityConnection();

        // Send join ok response to the target client
        response = communityConnection.joinOK(joinOKResponse, joinOKEndpoint);

        // GUI
        Main.getForm().appendTerminal(joinRequest.toString());
        Main.getForm().appendNeighbour(neighbour);

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

            if(fileList.size() == 0) {
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

                for(Neighbour neighbour: neighbours) {
                    if (hops > 0) {
                        // Forward search request to neighbours within the circle

                        // Update hops count
                        --hops;
                        forwardRequest.setHops(hops);

                        // Create an endpoint to the target neighbour
                        SearchEndpoint searchEndpoint = new SearchEndpoint(neighbour.getIpAddress(),
                                neighbour.getPort());

                        // Forward search request to the community
                        try {
                            communityConnection.search(forwardRequest, searchEndpoint);
                        } catch (org.springframework.web.client.ResourceAccessException e) {
                            // Update file to location table
                            FileToLocationTable.removeLocation(fileName, neighbour);

                            // Update neighbours
                            Neighbour.removeNeighbour(neighbour);
                        }
                    } else {
                        // Hops count limit exceeded, send file not found message

                        // Load system communication configurations
                        String systemIpAddress = Configuration.getSystemIPAddress();
                        int systemPort = Configuration.getSystemPort();

                        // Generate file not found response
                        SearchOKResponse searchOKResponse = new SearchOKResponse();
                        searchOKResponse.setQuery(searchRequest.getFileName());
                        searchOKResponse.setTimestamp(searchOKResponse.getTimestamp());
                        searchOKResponse.setValue(0);
                        searchOKResponse.setIpAddress(systemIpAddress);
                        searchOKResponse.setPort(systemPort);
                        searchOKResponse.setFileNames(new ArrayList<>());

                        // Create search ok endpoint to the target client
                        SearchOKEndpoint searchOKEndpoint = new SearchOKEndpoint(searchRequest.getIpAddress(),
                                searchRequest.getPort());

                        // Send response to the client
                        response = communityConnection.searchOK(searchOKResponse, searchOKEndpoint);

                        break;
                    }
                }
            } else {
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

                // Establish connection with the community
                CommunityConnection communityConnection = new CommunityConnection();

                // Create search ok endpoint to the target client
                SearchOKEndpoint searchOKEndpoint = new SearchOKEndpoint(searchRequest.getIpAddress(),
                        searchRequest.getPort());

                // Send response to the client
                response = communityConnection.searchOK(searchOKResponse, searchOKEndpoint);
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
        LeaveOKEndpoint leaveOKEndpoint = new LeaveOKEndpoint(leaveOKResponse.getIpAddress(), leaveOKResponse.getPort());

        // Establish connection with the community
        CommunityConnection communityConnection = new CommunityConnection();

        // Send leave ok response to the target client
        communityConnection.leaveOK(leaveOKResponse, leaveOKEndpoint);

        response = "ACK";

        // GUI
        Main.getForm().appendTerminal(leaveRequest.toString());

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

        if(debug) {
            System.out.println(joinOKResponse.toString());
        }

        String response = null;

        Neighbour neighbour = new Neighbour(joinOKResponse.getIpAddress(), joinOKResponse.getPort());
        Neighbour.addNeighbour(neighbour);

        // GUI
        Main.getForm().appendTerminal(joinOKResponse.toString());
        Main.getForm().appendNeighbour(neighbour);

        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/searchOK", method = RequestMethod.POST)
    public ResponseEntity searchOK(@RequestBody SearchOKResponse searchOKResponse) {
        /*
            Handle SearchOKResponse
         */

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

        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/leaveOK", method = RequestMethod.POST)
    public ResponseEntity leaveOK(@RequestBody LeaveOKResponse leaveOKResponse) {
        /*
            Handle LeaveOKResponse
         */

        if(debug) {
            System.out.println(leaveOKResponse.toString());
        }

        Neighbour neighbour = new Neighbour(leaveOKResponse.getIpAddress(), leaveOKResponse.getPort());
        Neighbour.removeNeighbour(neighbour);

        // GUI
        Main.getForm().appendTerminal(leaveOKResponse.toString());

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

    @CrossOrigin
    @RequestMapping(value = "/initSearch/{file_name}", method = RequestMethod.GET)
    public void initSearch(@PathVariable("file_name") String fileName) {
        /*
            Handle developer search request
         */

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
            CommunityConnection communityConnection = new CommunityConnection();

            for(Neighbour neighbour: neighbours) {
                // Create an endpoint to the target neighbour
                SearchEndpoint searchEndpoint = new SearchEndpoint(neighbour.getIpAddress(),
                        neighbour.getPort());
                try {
                    communityConnection.search(searchRequest, searchEndpoint);
                } catch (org.springframework.web.client.ResourceAccessException e) {

                }
            }
        } else {
            System.out.println("Files : " + Arrays.toString(files.toArray()));
        }
    }
}
