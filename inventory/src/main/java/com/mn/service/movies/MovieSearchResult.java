package com.mn.service.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieSearchResult
{
    // required
    private String movieId;
    private String title;
    private Integer year;
    private String director;

    // optional
    private Float rating;
    private Integer numVotes;
    private Boolean hidden;

    @JsonCreator
    public MovieSearchResult(
            @JsonProperty(value = "movieId", required = true) String movieId,
            @JsonProperty(value = "title", required = true) String title,
            @JsonProperty(value = "director", required = true) String director,
            @JsonProperty(value = "year", required = true) Integer year,
            @JsonProperty(value = "rating") Float rating,
            @JsonProperty(value = "numVotes") Integer numVotes,
            @JsonProperty(value = "hidden") Boolean hidden
    )
    {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.numVotes = numVotes;
        this.hidden = hidden;
    }

    public Integer getYear()
    {
        return year;
    }

    public String getDirector()
    {
        return director;
    }

    public Float getRating()
    {
        return rating;
    }

    public Integer getNumVotes()
    {
        return numVotes;
    }

    public Boolean getHidden()
    {
        return hidden;
    }

    public String getMovieId()
    {
        return movieId;
    }

    public String getTitle()
    {
        return title;
    }

}
