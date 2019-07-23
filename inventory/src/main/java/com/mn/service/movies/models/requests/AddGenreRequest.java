package com.mn.service.movies.models.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddGenreRequest
{
    private String name;

    @JsonCreator
    public AddGenreRequest(
            @JsonProperty(value = "name", required = true) String name
    )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
