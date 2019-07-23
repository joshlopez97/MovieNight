package com.mn.service.api_gateway.models.movies.requests;

public class MovieSearchRequest
    extends SearchRequest
{
    private String title;
    private String genre;
    private Integer year;
    private String director;
    private Boolean hidden;

    public MovieSearchRequest(String title, String genre, Integer year, String director, Boolean hidden, Integer offset, Integer limit, String sortby, String orderby)
    {
        super(offset, limit, sortby, orderby, new String[]{"id", "title", "year", "director", "backdrop_path", "budget", "overview", "poster_path", "revenue", "rating"});
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.director = director;
        this.hidden = hidden;

    }

    public String getTitle()
    {
        return title;
    }

    public String getGenre()
    {
        return genre;
    }

    public Integer getYear()
    {
        return year;
    }

    public String getDirector()
    {
        return director;
    }

    public Boolean getHidden()
    {
        return hidden;
    }
}
