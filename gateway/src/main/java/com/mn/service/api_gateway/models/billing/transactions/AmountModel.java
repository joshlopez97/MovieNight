package com.mn.service.api_gateway.models.billing.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AmountModel
{
    private String total;
    private String currency;

    @JsonCreator
    public AmountModel(
            @JsonProperty(value = "total", required = true) String total,
            @JsonProperty(value = "value", required = true) String currency)
    {
        this.total = total;
        this.currency = currency;
    }

    public String getTotal()
    {
        return total;
    }

    public String getCurrency()
    {
        return currency;
    }
}
