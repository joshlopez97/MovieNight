package com.mn.service.billing.models.requests.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerRequest
{
    private String email;

    @JsonCreator
    public CustomerRequest(
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
