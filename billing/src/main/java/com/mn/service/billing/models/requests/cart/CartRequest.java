package com.mn.service.billing.models.requests.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartRequest
{
    private String email;

    @JsonCreator
    public CartRequest(
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
