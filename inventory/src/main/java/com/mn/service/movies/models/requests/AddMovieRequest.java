package com.mn.service.movies.models.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.movies.models.Genre;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddMovieRequest
{
    // required
    private String title;
    private String director;
    private Integer year;
    private Genre[] genres;

    // optional
    private String backdrop_path;
    private Integer budget;
    private String overview;
    private String poster_path;
    private Integer revenue;

    @JsonCreator
    public AddMovieRequest(
            @JsonProperty(value = "title", required = true) String title,
            @JsonProperty(value = "director", required = true) String director,
            @JsonProperty(value = "year", required = true) Integer year,
            @JsonProperty(value = "genres", required = true) Genre[] genres,
            @JsonProperty(value = "backdrop_path") String backdrop_path,
            @JsonProperty(value = "budget") Integer budget,
            @JsonProperty(value = "overview") String overview,
            @JsonProperty(value = "poster_path") String poster_path,
            @JsonProperty(value = "revenue") Integer revenue)
    {
        this.title = title;
        this.director = director;
        this.year = year;
        this.genres = genres;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDirector()
    {
        return director;
    }

    public Integer getYear()
    {
        return year;
    }

    public Genre[] getGenres()
    {
        return genres;
    }

    public String getBackdrop_path()
    {
        return backdrop_path;
    }

    public Integer getBudget()
    {
        return budget;
    }

    public String getOverview()
    {
        return overview;
    }

    public String getPoster_path()
    {
        return poster_path;
    }

    public Integer getRevenue()
    {
        return revenue;
    }

    public void setGenres(Genre[] genres)
    {
        this.genres = genres;
    }
}
