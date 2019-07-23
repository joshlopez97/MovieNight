package com.mn.service.movies.models.tmdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TMDBMovie
{
    private Boolean adult;
    private String id;
    private String original_title;
    private Float popularity;
    private Boolean video;

    @JsonCreator
    public TMDBMovie(
            @JsonProperty(value = "adult", required = true) Boolean adult,
            @JsonProperty(value = "id", required = true) String id,
            @JsonProperty(value = "original_title", required = true) String original_title,
            @JsonProperty(value = "popularity", required = true) Float popularity,
            @JsonProperty(value = "video", required = true) Boolean video
    )
    {
        this.adult = adult;
        this.id = id;
        this.original_title = original_title;
        this.popularity = popularity;
        this.video = video;
    }

    public Boolean getAdult()
    {
        return adult;
    }

    public String getId()
    {
        return id;
    }

    public String getOriginal_title()
    {
        return original_title;
    }

    public Float getPopularity()
    {
        return popularity;
    }

    public Boolean getVideo()
    {
        return video;
    }
}
