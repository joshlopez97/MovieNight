package com.mn.service.billing.models.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionFee
{
    private String value;
    private String currency;

    @JsonCreator
    public TransactionFee(
            @JsonProperty(value = "value", required = true) String value,
            @JsonProperty(value = "currency", required = true) String currency)
    {
        this.value = value;
        this.currency = currency;
    }

    public String getValue()
    {
        return value;
    }

    public String getCurrency()
    {
        return currency;
    }
}
