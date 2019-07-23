package com.mn.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
