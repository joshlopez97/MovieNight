package com.mn.service.api_gateway.models.billing.requests.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteCartItemRequest
    extends CartRequest
{
    private String movieId;

    @JsonCreator
    public DeleteCartItemRequest(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "movieId", required = true) String movieId
    )
    {
        super(email);
        this.movieId = movieId;
    }

    public String getMovieId()
    {
        return movieId;
    }
}
