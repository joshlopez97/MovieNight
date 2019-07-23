package com.mn.service.movies.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieCountResponse
    extends JsonResponse
{
    private Integer count;

    @JsonCreator
    public MovieCountResponse(
            @JsonProperty(value="resultCode", required = true) int resultCode,
            @JsonProperty(value="message", required = true) String message,
            @JsonProperty(value="count", required = true) Integer count
    )
    {
        super(resultCode, message);
        this.count = count;
    }

    public Integer getCount()
    {
        return count;
    }
}
