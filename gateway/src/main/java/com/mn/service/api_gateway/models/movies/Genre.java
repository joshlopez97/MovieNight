package com.mn.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Genre
{
    private Integer id;
    private String name;

    @JsonCreator
    public Genre(
            @JsonProperty(value = "id") Integer id,
            @JsonProperty(value = "name", required = true) String name
    )
    {
        this.id = id;
        this.name = name;
    }

    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
