package com.mn.service.movies.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.movies.models.Genre;

public class GetGenresResponse
    extends JsonResponse
{
    private Genre[] genres;

    @JsonCreator
    public GetGenresResponse(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty(value = "genres", required = true) Genre[] genres
    )
    {
        super(resultCode, message);
        this.genres = genres;
    }

    public Genre[] getGenres()
    {
        return genres;
    }
}
