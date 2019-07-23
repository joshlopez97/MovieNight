package com.mn.service.api_gateway.models.billing.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.api_gateway.models.billing.carts.SoldCartItem;

public class ItemModel
        extends SoldCartItem
{
    private Float unitPrice;
    private Float discount;

    @JsonCreator
    public ItemModel(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "movieId", required = true) String movieId,
            @JsonProperty(value = "quantity", required = true) Integer quantity,
            @JsonProperty(value = "saleDate", required = true) String saleDate,
            @JsonProperty(value = "unit_price", required = true) Float unitPrice,
            @JsonProperty(value = "discount", required = true) Float discount)
    {
        super(email, movieId, quantity, saleDate);
        this.unitPrice = unitPrice;
        this.discount = discount;
    }

    public Float getUnitPrice()
    {
        return unitPrice;
    }

    public Float getDiscount()
    {
        return discount;
    }
}
