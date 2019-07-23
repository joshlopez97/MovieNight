package com.mn.service.billing.models.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.billing.models.carts.SoldCartItem;

public class ItemModel
        extends SoldCartItem
{
    private Float unit_price;
    private Float discount;

    @JsonCreator
    public ItemModel(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "movieId", required = true) String movieId,
            @JsonProperty(value = "quantity", required = true) Integer quantity,
            @JsonProperty(value = "saleDate", required = true) String saleDate,
            @JsonProperty(value = "unit_price", required = true) Float unit_price,
            @JsonProperty(value = "discount", required = true) Float discount)
    {
        super(email, movieId, quantity, saleDate);
        this.unit_price = unit_price;
        this.discount = discount;
    }

    public Float getUnit_price()
    {
        return unit_price;
    }

    public Float getDiscount()
    {
        return discount;
    }
}
