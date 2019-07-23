package com.mn.service.movies.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.movies.models.DetailedMovie;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMovieResponse
    extends JsonResponse
{
    private DetailedMovie movie;

    @JsonProperty(value = "movie", required = true)
    public DetailedMovie getMovie()
    {
        return movie;
    }

    @JsonCreator
    public GetMovieResponse(int resultCode, String message, DetailedMovie movie)
    {
        super(resultCode, message);
        this.movie = movie;
    }
}
