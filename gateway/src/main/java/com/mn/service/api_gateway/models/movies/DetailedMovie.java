package com.mn.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedMovie
{
    // required
    private String movieId;
    private String title;
    private Integer year;
    private String director;
    private Genre[] genres;
    private Star[] stars;

    // optional
    private String backdrop_path;
    private Integer budget;
    private String overview;
    private String poster_path;
    private Integer revenue;
    private Float rating;
    private Integer numVotes;
    private Boolean hidden;

    @JsonCreator
    public DetailedMovie(
            @JsonProperty(value = "movieId", required = true) String movieId,
            @JsonProperty(value = "title", required = true) String title,
            @JsonProperty(value = "director", required = true) String director, @JsonProperty(value = "year", required = true) Integer year,
            @JsonProperty(value = "backdrop_path") String backdrop_path, @JsonProperty(value = "budget") Integer budget, @JsonProperty(value = "revenue") Integer revenue, @JsonProperty(value = "overview") String overview, @JsonProperty(value = "poster_path") String poster_path, @JsonProperty(value = "rating") Float rating, @JsonProperty(value = "numVotes") Integer numVotes, @JsonProperty(value = "genres", required = true) Genre[] genres,
            @JsonProperty(value = "stars", required = true) Star[] stars,
            @JsonProperty(value = "hidden") Boolean hidden
    )
    {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
        this.director = director;
        this.genres = genres;
        this.stars = stars;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        this.rating = rating;
        this.numVotes = numVotes;
        this.hidden = hidden;
    }

    public Integer getYear()
    {
        return year;
    }

    public String getDirector()
    {
        return director;
    }

    public Genre[] getGenres()
    {
        return genres;
    }

    public Star[] getStars()
    {
        return stars;
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

    public Float getRating()
    {
        return rating;
    }

    public Integer getNumVotes()
    {
        return numVotes;
    }

    public Boolean getHidden()
    {
        return hidden;
    }

    public String getMovieId()
    {
        return movieId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setGenres(Genre[] genres)
    {
        this.genres = genres;
    }

    public void setStars(Star[] stars)
    {
        this.stars = stars;
    }
}
