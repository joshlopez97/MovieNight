package com.mn.service.api_gateway.threadpool;

import com.mn.service.api_gateway.models.RequestModel;
import com.mn.service.api_gateway.utility.TransactionIDGenerator;

public class ClientRequest {
    private String email;
    private String sessionID;
    private String transactionID;
    private RequestModel request;
    private String URI;
    private String endpoint;
    private String ip_address;

    public ClientRequest(String email, String sessionID, RequestModel request, String URI, String endpoint, String ip_address)
    {
        this.email = email;
        this.sessionID = sessionID;
        this.ip_address = ip_address;
        this.transactionID = TransactionIDGenerator.generateTransactionID();
        this.request = request;
        this.URI = URI;
        this.endpoint = endpoint;
    }

    public String getEmail()
    {
        return email;
    }

    public String getSessionID()
    {
        return sessionID;
    }

    public String getTransactionID()
    {
        return transactionID;
    }

    public RequestModel getRequest()
    {
        return request;
    }

    public String getURI()
    {
        return URI;
    }

    public String getEndpoint()
    {
        return endpoint;
    }

    public String getIp_address()
    {
        return ip_address;
    }

    public void setIp_address(String ip_address)
    {
        this.ip_address = ip_address;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setSessionID(String sessionID)
    {
        this.sessionID = sessionID;
    }

    public void setTransactionID(String transactionID)
    {
        this.transactionID = transactionID;
    }

    public void setRequest(RequestModel request)
    {
        this.request = request;
    }

    public void setURI(String URI)
    {
        this.URI = URI;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }
}
