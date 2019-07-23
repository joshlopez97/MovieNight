package com.mn.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditCard
{
    private String id;
    private String firstName;
    private String lastName;
    private String expiration;

    @JsonCreator
    public CreditCard(
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty(value = "firstName", required = true) String firstName,
            @JsonProperty(value = "lastName", required = true) String lastName,
            @JsonProperty(value = "expiration", required = true) String expiration)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    public String getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getExpiration()
    {
        return expiration;
    }
}
