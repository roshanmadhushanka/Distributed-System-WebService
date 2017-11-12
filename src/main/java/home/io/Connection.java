package home.io;

import home.api.*;
import home.message.request.JoinRequest;
import home.message.request.LeaveRequest;
import home.message.request.SearchRequest;
import home.message.response.JoinOKResponse;
import home.message.response.LeaveOKResponse;
import home.message.response.SearchOKResponse;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Connection {
    private int timeout;
    private Object message;
    private Object endpoint;
    private String response;
    private RestTemplate restTemplate;

    public Connection(int timeout) {
        restTemplate = new RestTemplate();
        this.timeout = timeout;
    }


    private void send() {
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        final Runnable sender = new Runnable() {
            public void run() {
                if(message instanceof JoinRequest && endpoint instanceof JoinEndpoint) {
                    JoinRequest joinRequest = (JoinRequest) message;
                    JoinEndpoint joinEndpoint = (JoinEndpoint) endpoint;
                    response = restTemplate.postForObject(joinEndpoint.getEndpoint(), joinRequest, String.class);
                } else if(message instanceof SearchRequest && endpoint instanceof SearchEndpoint) {
                    SearchRequest searchRequest = (SearchRequest) message;
                    SearchEndpoint searchEndpoint = (SearchEndpoint) endpoint;
                    response = restTemplate.postForObject(searchEndpoint.getEndpoint(), searchRequest, String.class);
                } else if(message instanceof LeaveRequest && endpoint instanceof LeaveEndpoint) {
                    LeaveRequest leaveRequest = (LeaveRequest) message;
                    LeaveEndpoint leaveEndpoint = (LeaveEndpoint) endpoint;
                    response = restTemplate.postForObject(leaveEndpoint.getEndpoint(), leaveRequest, String.class);
                } else if(message instanceof JoinOKResponse && endpoint instanceof JoinOKEndpoint) {
                    JoinOKResponse joinOKResponse = (JoinOKResponse) message;
                    JoinOKEndpoint joinOKEndpoint = (JoinOKEndpoint) endpoint;
                    response = restTemplate.postForObject(joinOKEndpoint.getEndpoint(), joinOKResponse, String.class);
                } else if(message instanceof SearchOKResponse && endpoint instanceof SearchOKEndpoint) {
                    SearchOKResponse searchOKResponse = (SearchOKResponse) message;
                    SearchOKEndpoint searchOKEndpoint = (SearchOKEndpoint) endpoint;
                    response = restTemplate.postForObject(searchOKEndpoint.getEndpoint(), searchOKResponse, String.class);
                } else if(message instanceof LeaveOKResponse && endpoint instanceof LeaveEndpoint) {
                    LeaveOKResponse leaveOKResponse = (LeaveOKResponse) message;
                    LeaveOKEndpoint leaveOKEndpoint = (LeaveOKEndpoint) endpoint;
                    response = restTemplate.postForObject(leaveOKEndpoint.getEndpoint(), leaveOKResponse, String.class);
                }
            }
        };

        final ScheduledFuture<?> beeperHandle = scheduler.schedule (sender, 0, TimeUnit.SECONDS);

        scheduler.schedule(new Runnable() {
            public void run() {
                beeperHandle.cancel(true);
                response = "Error";
            }
        }, timeout, TimeUnit.SECONDS);
        scheduler.shutdown();
    }

    public void send(Object message, Object endpoint) {
        this.message = message;
        this.endpoint = endpoint;
        send();
    }
}
