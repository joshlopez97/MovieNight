package com.mn.service.billing.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaceOrderResponse
    extends JsonResponse
{
    private String redirectUrl;
    private String token;

    @JsonCreator
    public PlaceOrderResponse(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty(value = "redirectURL", required = true) String redirectUrl,
            @JsonProperty(value = "token", required = true) String token)
    {
        super(resultCode, message);
        this.redirectUrl = redirectUrl;
        this.token = token;
    }

    public String getRedirectURL()
    {
        return redirectUrl;
    }

    public String getToken()
    {
        return token;
    }
}
