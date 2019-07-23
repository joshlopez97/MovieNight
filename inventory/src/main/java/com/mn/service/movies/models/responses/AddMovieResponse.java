package com.mn.service.movies.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddMovieResponse
    extends JsonResponse
{
    private String movieid;
    private Integer[] genreid;

    @JsonCreator
    public AddMovieResponse(int resultCode, String message, String movieid, Integer[] genreid)
    {
        super(resultCode, message);
        this.movieid = movieid;
        this.genreid = genreid;
    }

    @JsonProperty(value = "movieid")
    public String getMovieid()
    {
        return movieid;
    }

    @JsonProperty(value = "genreid")
    public Integer[] getGenreid()
    {
        return genreid;
    }
}
