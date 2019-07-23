package com.mn.service.movies.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.movies.models.DetailedStar;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetStarResponse
    extends JsonResponse
{
    private DetailedStar stars;

    @JsonCreator
    public GetStarResponse(int resultCode, String message, DetailedStar stars)
    {
        super(resultCode, message);
        this.stars = stars;
    }

    @JsonProperty(value = "stars")
    public DetailedStar getStars()
    {
        return stars;
    }
}
