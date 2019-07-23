package com.mn.service.movies.models.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

public class AddStarToMovieRequest
{
    private String starID;
    private String movieID;

    @JsonCreator
    public AddStarToMovieRequest(
            @JsonProperty(value="starID", required = true) String starID,
            @JsonProperty(value="movieID", required = true) String movieID)
    {
        this.starID = starID;
        this.movieID = movieID;
    }

    public String getStarID()
    {
        return starID;
    }

    public String getMovieID()
    {
        return movieID;
    }
}
