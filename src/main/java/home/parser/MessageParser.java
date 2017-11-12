package home.parser;

import home.api.JoinEndpoint;
import home.api.LeaveEndpoint;
import home.gui.Main;
import home.io.CommunityConnection;
import home.io.Connection;
import home.message.request.JoinRequest;
import home.message.request.LeaveRequest;
import home.model.Neighbour;
import home.system.Configuration;
import java.util.List;

public class MessageParser {
    public static void responseParser(String response) {

        /*
            Parse responses received by the bootstrap server
         */

        if(response == null || response.equals("Timeout") || response.equals("Error")) {
            return;
        }

        String[] message = response.split(" ");

        // GUI
        Main.getForm().appendTerminal(response);
        System.out.println(response);

        if(message[1].equals("REGOK")) {
            // Register to bootstrap
            int value = Integer.parseInt(message[2]);
            if(value == 0) {
                // No users
                System.out.println("Register BS : No user");
            } else if(value == 1) {
                // 1 user
                System.out.println("Register BS : 1 User");
                register1User(message);
            } else if(value == 2) {
                // 2 users
                System.out.println("Register BS : 2 Users");
                register2Users(message);
            } else if(value == 9999) {
                // Error in command
                System.out.println("Register BS : Error in command");
            } else if(value == 9998) {
                // Already registered to you
                System.out.println("Register BS : Already registered to you");
            } else if(value == 9997) {
                // Registered to another user
                System.out.println("Register BS : Registered to another user");
            } else if(value == 9996) {
                // BS is full
                System.out.println("Register BS : Full");
            }
        } else if(message[1].equals("UNROK")) {
            // Unregister from bootstrap
            int value = Integer.parseInt(message[2]);
            if(value == 0) {
                // Success
                System.out.println("Unregister from BS : Success");
                unregisterUser(message);
            } else if(value == 9999) {
                // Error
                System.out.println("Unregister from BS : Fail");
            }
        } else if(message[1].equals("JOINOK")) {
            // Join distributed system
            int value = Integer.parseInt(message[2]);
            if(value == 0) {
                // Success
                System.out.println("Register DS : Success");
            } else if(value == 9999) {
                // Fail
                System.out.println("Register DS : Fail");
            }
        } else if(message[1].equals("LEAVEOK")) {
            // Leave from distributed system
            int value = Integer.parseInt(message[2]);
            if(value == 0) {
                // Success
                System.out.println("Leave DS : Success");
            } else if(value == 1) {
                // Fail
                System.out.println("Leave DS : Fail");
            }
        } else if(message[1].equals("SEROK")) {
            int value = Integer.parseInt(message[2]);
            if(value == 9999) {
                // Unreachable node
                System.out.println("Search : Unreachable node");
            } else if(value == 9998) {
                // other error
                System.out.println("Search : Other error");
            } else if(value == 0) {
                // No result
                System.out.println("Search : No result");
            } else if (value > 0){
                // has result
                System.out.println("Search : Has result");
            }
        } else if(message[1].equals("ERROR")) {
            // Error
            System.out.println("Error");
        }
    }

    private static void register1User(String[] message) {
        /*
            Register to a single user
         */

        // Create a neighbour node
        Neighbour neighbour = new Neighbour(message[3], Integer.parseInt(message[4]));

        // Load system communication configurations
        String systemIpAddress = Configuration.getSystemIPAddress();
        int systemPort = Configuration.getSystemPort();

        // Generate join request and target endpoint
        JoinRequest joinRequest = new JoinRequest(systemIpAddress, systemPort);
        JoinEndpoint joinEndpoint = new JoinEndpoint(neighbour.getIpAddress(), neighbour.getPort());

        // Send request to the community
        Connection connection = new Connection(3);

        connection.send(joinRequest, joinEndpoint);
    }

    private static void register2Users(String[] message) {
        /*
            Register to multiple users
         */

        // Create neighbour nodes
        Neighbour neighbourA = new Neighbour(message[3], Integer.parseInt(message[4]));
        Neighbour neighbourB = new Neighbour(message[5], Integer.parseInt(message[6]));

        // Load system communication configurations
        String systemIPAddress = Configuration.getSystemIPAddress();
        int systemPort = Configuration.getSystemPort();

        // Generate join request and target endpoints
        JoinRequest joinRequest = new JoinRequest(systemIPAddress, systemPort);
        JoinEndpoint joinEndpointA = new JoinEndpoint(neighbourA.getIpAddress(), neighbourA.getPort());
        JoinEndpoint joinEndpointB = new JoinEndpoint(neighbourB.getIpAddress(), neighbourB.getPort());

        // Send request to the community
        Connection connection = new Connection(3);

        connection.send(joinRequest, joinEndpointB);
        connection.send(joinRequest, joinEndpointA);

    }

    private static void unregisterUser(String[] message) {
        /*
            Unregister user from the bootstrap server and circle
         */

        // Load system communication configurations
        String systemIPAddress = Configuration.getSystemIPAddress();
        int systemPort = Configuration.getSystemPort();

        // Generate leave request
        LeaveRequest leaveRequest = new LeaveRequest(systemIPAddress, systemPort);

        // Gracefully departure from each node
        Connection connection = new Connection(3);

        List<Neighbour> neighbours = Neighbour.getNeighbours();

        for(Neighbour n: neighbours) {

            // Generate leave endpoint
            LeaveEndpoint leaveEndpoint = new LeaveEndpoint(n.getIpAddress(), n.getPort());

            // Send leave request to the community
            connection.send(leaveRequest, leaveEndpoint);
        }
    }
}
