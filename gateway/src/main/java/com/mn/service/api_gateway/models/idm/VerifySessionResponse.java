package com.mn.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.api_gateway.models.JsonResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifySessionResponse
    extends JsonResponse
{
    private String sessionID;

    @JsonCreator
    public VerifySessionResponse(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty(value = "sessionID") String sessionID
    )
    {
        super(resultCode, message);
        this.sessionID = sessionID;
    }

    public String getSessionID()
    {
        return sessionID;
    }
}
