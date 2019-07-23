package com.mn.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedStar
    extends Star
{
    private int birthyear;
    private Movie[] movies;

    @JsonCreator
    public DetailedStar(String id, String name, int birthyear, Movie[] movies)
    {
        super(id, name);
        this.birthyear = birthyear;
        this.movies = movies;
    }

    @JsonProperty(value = "birthYear", required = true)
    public int getBirthyear()
    {
        return birthyear;
    }

    @JsonProperty(value = "movies", required = true)
    public Movie[] getMovies()
    {
        return movies;
    }

    public void setMovies(Movie[] movies)
    {
        this.movies = movies;
    }
}
