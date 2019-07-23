package com.mn.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonResponse
{
    private int resultCode;
    private String message;

    @JsonCreator
    public JsonResponse(int resultCode, String message)
    {
        this.resultCode = resultCode;
        this.message = message;
    }

    @JsonProperty(value = "resultCode", required = true)
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message", required = true)
    public String getMessage() {
        return message;
    }
}
