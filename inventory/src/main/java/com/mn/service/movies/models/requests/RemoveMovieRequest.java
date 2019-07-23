package com.mn.service.movies.models.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoveMovieRequest
{
    private String movieId;

    @JsonCreator
    public RemoveMovieRequest(
            @JsonProperty(value = "movieId", required = true) String movieId
    )
    {
        this.movieId = movieId;
    }

    public String getMovieId()
    {
        return movieId;
    }
}
