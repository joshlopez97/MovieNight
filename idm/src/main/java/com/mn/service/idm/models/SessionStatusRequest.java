package com.mn.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionStatusRequest
{
    private String sessionID;
    private String email;

    @JsonCreator
    public SessionStatusRequest(
            @JsonProperty(value = "sessionID", required = true) String sessionID,
            @JsonProperty(value = "email", required = true) String email
    )
    {
        this.email = email;
        this.sessionID = sessionID;
    }

    public String getSessionID()
    {
        return sessionID;
    }

    public void setSessionID(String sessionID)
    {
        this.sessionID = sessionID;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
