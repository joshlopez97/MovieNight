package com.mn.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer
{
    private String email;
    private String firstName;
    private String lastName;
    private String ccId;
    private String address;

    @JsonCreator
    public Customer(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "firstName", required = true) String firstName,
            @JsonProperty(value = "lastName", required = true) String lastName,
            @JsonProperty(value = "ccId", required = true) String ccId,
            @JsonProperty(value = "address", required = true) String address)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccId = ccId;
        this.address = address;
    }

    public String getEmail()
    {
        return email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getCcId()
    {
        return ccId;
    }

    public String getAddress()
    {
        return address;
    }
}
