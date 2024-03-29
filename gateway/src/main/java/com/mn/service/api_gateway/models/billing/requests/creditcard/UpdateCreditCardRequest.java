package com.mn.service.api_gateway.models.billing.requests.creditcard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

public class UpdateCreditCardRequest
    extends CreditCardRequest
{
    private String firstName;
    private String lastName;
    private Date expiration;

    @JsonCreator
    public UpdateCreditCardRequest(
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty(value = "firstName", required = true) String firstName,
            @JsonProperty(value = "lastName", required = true) String lastName,
            @JsonProperty(value = "expiration", required = true) Date expiration)
    {
        super(id);
        expiration.setTime(expiration.getTime() + 36000000);
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public Date getExpiration()
    {
        return expiration;
    }
}
