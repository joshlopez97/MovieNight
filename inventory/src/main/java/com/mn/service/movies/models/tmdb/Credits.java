package com.mn.service.movies.models.tmdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class Credits
{
    private CastMember[] cast;
    private CrewMember[] crew;

    @JsonCreator
    public Credits(
            @JsonProperty(value="cast") CastMember[] cast,
            @JsonProperty(value="crew") CrewMember[] crew
    )
    {
        this.cast = cast;
        this.crew = crew;
    }

    public CastMember[] getCast()
    {
        return cast;
    }

    public CrewMember[] getCrew()
    {
        return crew;
    }
}
