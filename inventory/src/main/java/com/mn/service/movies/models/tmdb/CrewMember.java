package com.mn.service.movies.models.tmdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class CrewMember
{
    private String job;
    private String name;

    @JsonCreator
    public CrewMember(
            @JsonProperty(value="job") String job,
            @JsonProperty(value="name") String name
    )
    {
        this.job = job;
        this.name = name;
    }

    public String getJob()
    {
        return job;
    }

    public String getName()
    {
        return name;
    }
}
