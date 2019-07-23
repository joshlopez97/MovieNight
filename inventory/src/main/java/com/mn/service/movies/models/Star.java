package com.mn.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Star
{
    private String id;
    private String name;

    @JsonCreator
    public Star(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    @JsonProperty(value = "id", required = true)
    public String getId()
    {
        return id;
    }

    @JsonProperty(value = "name", required = true)
    public String getName()
    {
        return name;
    }
}
