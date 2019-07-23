package com.mn.service.movies.models.headers;

public class RequestHeader
{
    private String email;
    private String sessionID;
    private String transactionID;

    public RequestHeader(String email, String sessionID, String transactionID)
    {
        this.email = email;
        this.sessionID = sessionID;
        this.transactionID = transactionID;
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
}
