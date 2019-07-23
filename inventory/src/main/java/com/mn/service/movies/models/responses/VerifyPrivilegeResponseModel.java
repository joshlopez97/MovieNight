package com.mn.service.movies.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyPrivilegeResponseModel {
    private int resultCode;
    private String message;

    @JsonCreator
    public VerifyPrivilegeResponseModel(
            @JsonProperty(value="resultCode", required = true) int resultCode,
            @JsonProperty(value="message", required = true) String message
    )
    {
        this.resultCode = resultCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResultCode: " + resultCode + " Message: " + message;
    }


    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }
}