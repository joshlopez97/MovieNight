package com.mn.service.movies.models.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RatingRequest
{
    private Float rating;
    private String movieId;

    @JsonCreator
    public RatingRequest(
            @JsonProperty(value = "rating", required = true) Float rating,
            @JsonProperty(value = "id", required = true) String movieId
    )
    {
        this.rating = rating;
        this.movieId = movieId;
    }

    public Float getRating()
    {
        return rating;
    }

    public String getMovieId()
    {
        return movieId;
    }
}
