package com.mn.service.api_gateway.models.movies;

public class Ratings
{
    private Float rating;
    private Integer numVotes;
    private String movieId;

    public Ratings(Float rating, Integer numVotes, String movieId)
    {
        this.rating = rating;
        this.numVotes = numVotes;
        this.movieId = movieId;
    }

    public Float getRating()
    {
        return rating;
    }

    public Integer getNumVotes()
    {
        return numVotes;
    }

    public String getMovieId()
    {
        return movieId;
    }
}
