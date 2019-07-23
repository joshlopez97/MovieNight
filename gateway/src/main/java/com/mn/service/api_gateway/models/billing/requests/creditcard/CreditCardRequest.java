package com.mn.service.api_gateway.models.billing.requests.creditcard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditCardRequest
{
    private String id;

    @JsonCreator
    public CreditCardRequest(
            @JsonProperty(value = "id", required = true) String id
    )
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
}
