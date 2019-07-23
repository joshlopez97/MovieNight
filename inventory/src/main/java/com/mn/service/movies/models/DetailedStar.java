package com.mn.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedStar
    extends Star
{
    private int birthyear;
    private DetailedMovie[] movies;

    @JsonCreator
    public DetailedStar(String id, String name, int birthyear, DetailedMovie[] movies)
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
    public DetailedMovie[] getMovies()
    {
        return movies;
    }

    public void setMovies(DetailedMovie[] movies)
    {
        this.movies = movies;
    }
}
