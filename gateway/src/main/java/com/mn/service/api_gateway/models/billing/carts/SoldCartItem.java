package com.mn.service.api_gateway.models.billing.carts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SoldCartItem
    extends CartItem
{
    private String saleDate;

    @JsonCreator
    public SoldCartItem(
            @JsonProperty(value="email", required = true) String email,
            @JsonProperty(value="movieId", required = true) String movieId,
            @JsonProperty(value="quantity", required = true) Integer quantity,
            @JsonProperty(value="saleDate", required = true) String saleDate
    )
    {
        super(email, movieId, quantity);
        this.saleDate = saleDate;
    }

    public String getSaleDate()
    {
        return saleDate;
    }
}
