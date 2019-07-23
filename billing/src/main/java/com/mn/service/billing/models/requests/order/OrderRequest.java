package com.mn.service.billing.models.requests.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest
{
    private String email;

    @JsonCreator
    public OrderRequest(
            @JsonProperty(value = "email", required = true) String email
    )
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
}
