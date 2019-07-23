package com.mn.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionStatusResponse
    extends JsonResponse
{
    private String sessionID;

    @JsonCreator
    public SessionStatusResponse(
            @JsonProperty(value="resultCode", required = true) int resultCode,
            @JsonProperty(value="message", required = true) String message,
            @JsonProperty(value="sessionID") String sessionID)
    {
        super(resultCode, message);
        this.sessionID = sessionID;
    }

    public String getSessionID()
    {
        return sessionID;
    }
}
