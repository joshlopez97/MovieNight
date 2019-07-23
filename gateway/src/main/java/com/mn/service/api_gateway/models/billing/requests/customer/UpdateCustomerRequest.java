package com.mn.service.api_gateway.models.billing.requests.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateCustomerRequest
    extends CustomerRequest
{
    private String firstName;
    private String lastName;
    private String ccId;
    private String address;

    @JsonCreator
    public UpdateCustomerRequest(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "firstName", required = true) String firstName,
            @JsonProperty(value = "lastName", required = true) String lastName,
            @JsonProperty(value = "ccId", required = true) String ccId,
            @JsonProperty(value = "address", required = true) String address)
    {
        super(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccId = ccId;
        this.address = address;
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
