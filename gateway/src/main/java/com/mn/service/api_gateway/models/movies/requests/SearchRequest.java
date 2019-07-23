package com.mn.service.api_gateway.models.movies.requests;

import java.util.Arrays;

public class SearchRequest
{
    private final Integer DEFAULT_OFFSET = 0;
    private final Integer DEFAULT_LIMIT = 10;
    private final Integer[] ALLOWED_LIMITS = new Integer[]{10, 25, 50, 100};
    private final String DEFAULT_SORTBY = "rating";
    private final String DEFAULT_ORDERBY = "DESC";

    private Integer offset;
    private Integer limit;
    private String sortby;
    private String orderby;

    public SearchRequest(Integer offset, Integer limit, String sortby, String orderby, String[] sortby_allowed)
    {
        this.sortby = sortby == null || !Arrays.asList(sortby_allowed).contains(sortby.toLowerCase()) ?
                DEFAULT_SORTBY : sortby;
        this.offset = offset == null ? DEFAULT_OFFSET : offset;
        this.limit = limit == null || !Arrays.asList(ALLOWED_LIMITS).contains(limit) ? DEFAULT_LIMIT : limit;
        if (orderby == null)
            this.orderby = DEFAULT_ORDERBY;
        else
        {
            orderby = orderby.toUpperCase();
            if (orderby.equals("DESC") || orderby.equals("ASC"))
                this.orderby = orderby;
            else
                this.orderby = DEFAULT_ORDERBY;
        }
    }

    public SearchRequest(Integer offset, Integer limit, String sortby, String orderby)
    {
        this.sortby = sortby == null ? DEFAULT_SORTBY : sortby;
        this.offset = offset == null ? DEFAULT_OFFSET : offset;
        this.limit = limit == null || !Arrays.asList(ALLOWED_LIMITS).contains(limit) ? DEFAULT_LIMIT : limit;
        if (orderby == null)
            this.orderby = DEFAULT_ORDERBY;
        else
        {
            orderby = orderby.toUpperCase();
            if (orderby.equals("DESC") || orderby.equals("ASC"))
                this.orderby = orderby;
            else
                this.orderby = DEFAULT_ORDERBY;
        }
    }

    public Integer getOffset()
    {
        return offset;
    }

    public Integer getLimit()
    {
        return limit;
    }

    public String getSortby()
    {
        return sortby;
    }

    public String getOrderby()
    {
        return orderby;
    }
}
