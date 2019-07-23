package com.mn.service.api_gateway.models.movies.requests;

import com.fasterxml.jackson.annotation.JsonCreator;

public class StarSearchRequest
    extends SearchRequest
{
    private String name;
    private Integer birthYear;
    private String movieTitle;

    @JsonCreator
    public StarSearchRequest(Integer offset, Integer limit, String sortby, String orderby, String name, Integer birthYear, String movieTitle)
    {
        super(offset, limit, sortby, orderby);
        this.name = name;
        this.birthYear = birthYear;
        this.movieTitle = movieTitle;
    }

    public String getName()
    {
        return name;
    }

    public Integer getBirthYear()
    {
        return birthYear;
    }

    public String getMovieTitle()
    {
        return movieTitle;
    }
}
