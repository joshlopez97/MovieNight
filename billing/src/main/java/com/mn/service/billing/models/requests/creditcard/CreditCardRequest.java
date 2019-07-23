package com.mn.service.billing.models.requests.creditcard;

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
