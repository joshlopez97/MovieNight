package com.mn.service.movies.models.tmdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.movies.models.Genre;

@JsonIgnoreProperties
public class TMDBDetailedMovie
    extends TMDBMovie
{
    private String backdrop_path;
    private Integer budget;
    private Genre[] genres;
    private String overview;
    private String poster_path;
    private Integer revenue;
    private String title;
    private Float vote_average;
    private Integer vote_count;
    private String release_date;
    private Credits credits;

    @JsonCreator
    public TMDBDetailedMovie(
            @JsonProperty(value = "adult") Boolean adult,
            @JsonProperty(value = "id") String id,
            @JsonProperty(value = "original_title") String original_title,
            @JsonProperty(value = "popularity") Float popularity,
            @JsonProperty(value = "video") Boolean video,
            @JsonProperty(value = "backdrop_path") String backdrop_path,
            @JsonProperty(value = "budget") Integer budget,
            @JsonProperty(value = "genres") Genre[] genres,
            @JsonProperty(value = "overview") String overview,
            @JsonProperty(value = "poster_path") String poster_path,
            @JsonProperty(value = "revenue") Integer revenue,
            @JsonProperty(value = "title") String title,
            @JsonProperty(value = "vote_average") Float vote_average,
            @JsonProperty(value = "vote_count") Integer vote_count,
            @JsonProperty(value = "release_date") String release_date,
            @JsonProperty(value = "credits") Credits credits)
    {
        super(adult, id, original_title, popularity, video);
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.genres = genres;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        this.title = title;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.release_date = release_date;
        this.credits = credits;
    }

    public String getBackdrop_path()
    {
        return backdrop_path;
    }

    public Integer getBudget()
    {
        return budget;
    }

    public Genre[] getGenres()
    {
        return genres;
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

    public String getTitle()
    {
        return title;
    }

    public Float getVote_average()
    {
        return vote_average;
    }

    public Integer getVote_count()
    {
        return vote_count;
    }

    public String getRelease_date()
    {
        return release_date;
    }

    public Credits getCredits()
    {
        return credits;
    }
}
