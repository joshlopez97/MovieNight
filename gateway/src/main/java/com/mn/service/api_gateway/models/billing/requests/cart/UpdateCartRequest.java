package com.mn.service.api_gateway.models.billing.requests.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateCartRequest
    extends CartRequest
{
    private String movieId;
    private Integer quantity;

    @JsonCreator
    public UpdateCartRequest(
            @JsonProperty(value="email", required = true) String email,
            @JsonProperty(value="movieId", required = true) String movieId,
            @JsonProperty(value="quantity", required = true) Integer quantity
    )
    {
        super(email);
        this.movieId = movieId;
        this.quantity = quantity;
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
