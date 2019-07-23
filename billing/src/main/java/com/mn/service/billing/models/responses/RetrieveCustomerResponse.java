package com.mn.service.billing.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.billing.models.Customer;

public class RetrieveCustomerResponse
    extends JsonResponse
{
    private Customer customer;

    @JsonCreator
    public RetrieveCustomerResponse(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty(value = "customer", required = true) Customer customer)
    {
        super(resultCode, message);
        this.customer = customer;
    }

    public Customer getCustomer()
    {
        return customer;
    }
}
