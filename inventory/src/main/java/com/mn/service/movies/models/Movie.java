package com.mn.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie
{
    private String id;
    private String title;

    @JsonCreator
    public Movie(
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty(value = "title", required = true) String title
    )
    {
        this.id = id;
        this.title = title;
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }
}
