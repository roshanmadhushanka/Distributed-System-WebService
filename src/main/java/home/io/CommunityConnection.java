package home.io;

import home.api.*;
import home.message.request.JoinRequest;
import home.message.request.LeaveRequest;
import home.message.request.SearchRequest;
import home.message.response.JoinOKResponse;
import home.message.response.LeaveOKResponse;
import home.message.response.SearchOKResponse;
import org.springframework.web.client.RestTemplate;

public class CommunityConnection {
    /*
        CommunityConnection
        ===================
        Handle the interaction between the community and the system
     */

    RestTemplate restTemplate;

    public CommunityConnection() {
        restTemplate = new RestTemplate();
    }

    public String join(JoinRequest joinRequest, JoinEndpoint joinEndpoint) {
        String response;

        try {
            response = restTemplate.postForObject(joinEndpoint.getEndpoint(), joinRequest, String.class);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            response = "Error";
        }

        return response;
    }

    public String search(SearchRequest searchRequest, SearchEndpoint searchEndpoint) {
        String response;

        try {
            response = restTemplate.postForObject(searchEndpoint.getEndpoint(), searchRequest, String.class);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            response = "Error";
        }

        return response;
    }

    public String leave(LeaveRequest leaveRequest, LeaveEndpoint leaveEndpoint) {
        String response;

        try {
            response = restTemplate.postForObject(leaveEndpoint.getEndpoint(), leaveRequest, String.class);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            response = "Error";
        }

        return response;
    }

    public String joinOK(JoinOKResponse joinOKResponse, JoinOKEndpoint joinOKEndpoint) {
        String response;

        try {
            response = restTemplate.postForObject(joinOKEndpoint.getEndpoint(), joinOKResponse, String.class);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            response = "Error";
        }

        return response;
    }

    public String searchOK(SearchOKResponse searchOKResponse, SearchOKEndpoint searchOKEndpoint) {
        String response;

        try {
            response = restTemplate.postForObject(searchOKEndpoint.getEndpoint(), searchOKResponse, String.class);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            response = "Error";
        }

        return response;
    }

    public String leaveOK(LeaveOKResponse leaveOKResponse, LeaveOKEndpoint leaveOKEndpoint) {
        String response;

        try {
            response = restTemplate.postForObject(leaveOKEndpoint.getEndpoint(), leaveOKResponse, String.class);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            response = "Error";
        }

        return response;
    }
}
