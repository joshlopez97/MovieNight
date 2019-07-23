package com.mn.service.movies.models.requests;

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

    public SearchRequest(
            Integer offset,
            Integer limit,
            String sortby,
            String orderby,
            String[] sortby_allowed,
            String default_sortby,
            String default_orderby
    )
    {
        System.out.println(sortby);
        System.out.println(orderby);
        if (sortby_allowed.length > 0)
            System.out.println(sortby_allowed[0]);
        this.sortby = sortby == null || (sortby_allowed.length > 0 && !Arrays.asList(sortby_allowed).contains(sortby.toLowerCase())) ?
                default_sortby : sortby;
        this.limit = limit == null || !Arrays.asList(ALLOWED_LIMITS).contains(limit) ? DEFAULT_LIMIT : limit;
        this.offset = offset == null || offset < 0 || offset % this.limit != 0 ? DEFAULT_OFFSET : offset;
        if (orderby == null)
            this.orderby = default_orderby;
        else
        {
            orderby = orderby.toUpperCase();
            if (orderby.equals("DESC") || orderby.equals("ASC"))
                this.orderby = orderby;
            else
                this.orderby = default_orderby;
        }
    }

    public SearchRequest(
            Integer offset,
            Integer limit,
            String sortby,
            String orderby,
            String[] sortby_allowed
    )
    {
        this.sortby = sortby == null || !Arrays.asList(sortby_allowed).contains(sortby.toLowerCase()) ?
                DEFAULT_SORTBY : sortby;
        this.limit = limit == null || !Arrays.asList(ALLOWED_LIMITS).contains(limit) ? DEFAULT_LIMIT : limit;
        this.offset = offset == null || offset < 0 || offset % this.limit != 0 ? DEFAULT_OFFSET : offset;
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
