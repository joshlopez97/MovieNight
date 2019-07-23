package com.mn.service.api_gateway.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseModel
{
    private String transactionid;
    private String email;
    private String sessionid;
    private String response;
    private Integer httpstatus;

    @JsonCreator
    public ResponseModel(
            @JsonProperty(value = "transactionid", required = true) String transactionid,
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "sessionid", required = true) String sessionid,
            @JsonProperty(value = "response", required = true) String response,
            @JsonProperty(value = "httpstatus", required = true) Integer httpstatus
    )
    {
        this.transactionid = transactionid;
        this.email = email;
        this.sessionid = sessionid;
        this.response = response;
        this.httpstatus = httpstatus;
    }

    public String getTransactionid()
    {
        return transactionid;
    }

    public String getEmail()
    {
        return email;
    }

    public String getSessionid()
    {
        return sessionid;
    }

    public String getResponse()
    {
        return response;
    }

    public Integer getHttpstatus()
    {
        return httpstatus;
    }

    public String toString()
    {
        return "TransactionID = " + transactionid +
                ", Email = " + email +
                ", SessionID = " + sessionid +
                ", Response = " + response +
                ", HTTP status = " + httpstatus;
    }
}
