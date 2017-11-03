package home;

import home.io.BootstrapConnection;
import home.message.request.BSRegRequest;
import home.message.request.BSUnRegRequest;
import home.system.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DSClientController {

    @RequestMapping(value = "/pulse", method = RequestMethod.GET)
    public ResponseEntity<String> pulse() {
        /*
            Heartbeat verification of the service
         */

        String response = null;
        response = "ACK";
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/bsReg", method = RequestMethod.GET)
    public ResponseEntity<String> bsRegInitiator() {
        String response = null;

        // Generate message
        BSRegRequest bsRegRequest = new BSRegRequest(Configuration.getBsIpAddress(), Configuration.getSystemPort(),
                Configuration.getSystemName());

        // Create connection to the bootstrap server
        BootstrapConnection bootstrapConnection = new BootstrapConnection();

        // Send message and waiting for response
        response = bootstrapConnection.send(bsRegRequest.getMessage());

        // Send to message parser


        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> bsUnRegInitiator() {
        String response = null;

        // Generate message
        BSUnRegRequest bsUnRegRequest = new BSUnRegRequest(Configuration.getBsIpAddress(), Configuration.getSystemPort(),
                Configuration.getSystemName());

        // Create connection to the bootstrap server
        BootstrapConnection bootstrapConnection = new BootstrapConnection();

        // Send message and waiting for response
        response = bootstrapConnection.send(bsUnRegRequest.getMessage());

        // Send to message parser

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

}
