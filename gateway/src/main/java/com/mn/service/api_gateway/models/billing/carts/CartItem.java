package com.mn.service.api_gateway.models.billing.carts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItem
{
    private String email;
    private String movieId;
    private Integer quantity;

    @JsonCreator
    public CartItem(
            @JsonProperty(value="email", required = true) String email,
            @JsonProperty(value="movieId", required = true) String movieId,
            @JsonProperty(value="quantity", required = true) Integer quantity
    )
    {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    public String getEmail()
    {
        return email;
    }

    public String getMovieId()
    {
        return movieId;
    }

    public Integer getQuantity()
    {
        return quantity;
    }
}
