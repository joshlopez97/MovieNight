package com.mn.service.api_gateway.models.movies.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddStarRequest
{
    private String name;
    private Integer birthYear;

    @JsonCreator
    public AddStarRequest(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "birthYear") Integer birthYear
    )
    {
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getName()
    {
        return name;
    }

    public Integer getBirthYear()
    {
        return birthYear;
    }
}
