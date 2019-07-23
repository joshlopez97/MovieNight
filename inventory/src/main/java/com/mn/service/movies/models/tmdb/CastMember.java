package com.mn.service.movies.models.tmdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class CastMember
{
    private String name;

    @JsonCreator
    public CastMember(@JsonProperty(value = "name") String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
