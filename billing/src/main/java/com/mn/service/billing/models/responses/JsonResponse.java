package com.mn.service.billing.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonResponse
{
    private int resultCode;
    private String message;

    @JsonCreator
    public JsonResponse(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message
    )
    {
        this.resultCode = resultCode;
        this.message = message;
    }


    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }
}
