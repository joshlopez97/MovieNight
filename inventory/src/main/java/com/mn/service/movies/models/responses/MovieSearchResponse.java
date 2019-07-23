package com.mn.service.movies.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.movies.models.DetailedMovie;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieSearchResponse
    extends JsonResponse
{
    private DetailedMovie[] movies;

    @JsonCreator
    public MovieSearchResponse(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true)String message,
            @JsonProperty(value = "movies") DetailedMovie[] movies)
    {
        super(resultCode, message);
        this.movies = movies;
    }


    public DetailedMovie[] getMovies()
    {
        return movies;
    }
}
