package com.mn.service.movies.models.requests;

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
        super(offset, limit, sortby, orderby, new String[]{"title", "rating"});
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

    public String toString()
    {
        return "SearchRequest(title=" + this.getTitle() +
                ", genre=" + this.getGenre() +
                ", hidden=" + this.getHidden() +
                ", year=" + this.getYear() +
                ", limit=" + this.getLimit() +
                ", offset=" + this.getOffset() +
                ", sortby=" + this.getSortby() +
                ", orderby=" + this.getOrderby() + ")";

    }
}
