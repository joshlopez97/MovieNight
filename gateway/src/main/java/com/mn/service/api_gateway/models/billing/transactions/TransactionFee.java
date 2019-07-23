package com.mn.service.api_gateway.models.billing.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionFee
{
    private String total;
    private String value;

    @JsonCreator
    public TransactionFee(
            @JsonProperty(value = "total", required = true) String total,
            @JsonProperty(value = "value", required = true) String value)
    {
        this.total = total;
        this.value = value;
    }

    public String getTotal()
    {
        return total;
    }

    public String getValue()
    {
        return value;
    }
}
