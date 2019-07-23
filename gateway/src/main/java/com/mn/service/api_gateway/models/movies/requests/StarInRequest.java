package com.mn.service.api_gateway.models.movies.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StarInRequest
{
    private String starId;
    private String movieId;

    @JsonCreator
    public StarInRequest(
            @JsonProperty(value="starid", required = true) String starId,
            @JsonProperty(value="movieid", required = true) String movieId)
    {
        this.starId = starId;
        this.movieId = movieId;
    }

    public String getStarId()
    {
        return starId;
    }

    public String getMovieId()
    {
        return movieId;
    }
}
