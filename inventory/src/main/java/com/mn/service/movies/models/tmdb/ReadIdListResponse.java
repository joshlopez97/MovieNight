package com.mn.service.movies.models.tmdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReadIdListResponse
{
    private Integer numMovies;

    @JsonCreator
    public ReadIdListResponse(
            @JsonProperty(value = "numMovies", required = true) Integer numMovies)
    {
        this.numMovies = numMovies;
    }

    public Integer getNumMovies()
    {
        return numMovies;
    }
}
