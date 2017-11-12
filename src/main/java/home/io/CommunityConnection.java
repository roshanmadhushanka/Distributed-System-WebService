package home.io;

import home.api.*;
import home.message.request.JoinRequest;
import home.message.request.LeaveRequest;
import home.message.request.SearchRequest;
import home.message.response.JoinOKResponse;
import home.message.response.LeaveOKResponse;
import home.message.response.SearchOKResponse;
import home.model.Neighbour;
import home.system.Configuration;
import home.table.FileToLocationTable;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CommunityConnection {
    /*
        CommunityConnection
        ===================
        Handle the interaction between the community and the system
     */

//    private RestTemplate restTemplate;
//    private int timeout;
//    private String response;
//
//    public CommunityConnection(int timeout) {
//        this.restTemplate = new RestTemplate();
//        this.timeout = timeout;
//    }
//
//    public String join(JoinRequest joinRequest, JoinEndpoint joinEndpoint) {
//        ScheduledExecutorService scheduler =
//                Executors.newScheduledThreadPool(1);
//
//        final Runnable beeper = new Runnable() {
//            public void run() {
//                try {
//                    response = restTemplate.postForObject(joinEndpoint.getEndpoint(), joinRequest, String.class);
//                } catch (org.springframework.web.client.ResourceAccessException e) {
//                    response = "Error";
//                }
//            }
//        };
//
//        final ScheduledFuture<?> beeperHandle = scheduler.schedule (beeper, 0, TimeUnit.SECONDS);
//
//        scheduler.schedule(new Runnable() {
//            public void run() {
//                beeperHandle.cancel(true);
//                response = "Error";
//            }
//        }, timeout, TimeUnit.SECONDS);
//
//
//        return response;
//    }
//
//    public String search(SearchRequest searchRequest, SearchEndpoint searchEndpoint) {
//        ScheduledExecutorService scheduler =
//                Executors.newScheduledThreadPool(1);
//
//        final Runnable beeper = new Runnable() {
//            public void run() {
//                try {
//                    response = restTemplate.postForObject(searchEndpoint.getEndpoint(), searchRequest, String.class);
//                } catch (org.springframework.web.client.ResourceAccessException e) {
//                    response = "Error";
//                }
//            }
//        };
//
//        final ScheduledFuture<?> beeperHandle = scheduler.schedule (beeper, 0, TimeUnit.SECONDS);
//
//        scheduler.schedule(new Runnable() {
//            public void run() {
//                beeperHandle.cancel(true);
//                response = "Error";
//            }
//        }, timeout, TimeUnit.SECONDS);
//        scheduler.shutdown();
//
//        return response;
//    }
//
//    public String leave(LeaveRequest leaveRequest, LeaveEndpoint leaveEndpoint) {
//        String response;
//
//        try {
//            response = restTemplate.postForObject(leaveEndpoint.getEndpoint(), leaveRequest, String.class);
//        } catch (org.springframework.web.client.ResourceAccessException e) {
//            response = "Error";
//        }
//
//        return response;
//    }
//
//    public String joinOK(JoinOKResponse joinOKResponse, JoinOKEndpoint joinOKEndpoint) {
//        String response;
//
//        try {
//            response = restTemplate.postForObject(joinOKEndpoint.getEndpoint(), joinOKResponse, String.class);
//        } catch (org.springframework.web.client.ResourceAccessException e) {
//            response = "Error";
//        }
//
//        return response;
//    }
//
//    public String searchOK(SearchOKResponse searchOKResponse, SearchOKEndpoint searchOKEndpoint) {
//        String response;
//
//        try {
//            response = restTemplate.postForObject(searchOKEndpoint.getEndpoint(), searchOKResponse, String.class);
//        } catch (org.springframework.web.client.ResourceAccessException e) {
//            response = "Error";
//        }
//
//        return response;
//    }
//
//    public String leaveOK(LeaveOKResponse leaveOKResponse, LeaveOKEndpoint leaveOKEndpoint) {
//        String response;
//
//        try {
//            response = restTemplate.postForObject(leaveOKEndpoint.getEndpoint(), leaveOKResponse, String.class);
//        } catch (org.springframework.web.client.ResourceAccessException e) {
//            response = "Error";
//        }
//
//        return response;
//    }
}
