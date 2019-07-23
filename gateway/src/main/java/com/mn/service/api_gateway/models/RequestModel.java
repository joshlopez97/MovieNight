package com.mn.service.api_gateway.models;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

public class RequestModel
{
    private String method;
    private String body;
    private UriInfo uriInfo;
    private HttpHeaders headers;

    public RequestModel(String method)
    {
        this.method = method;
    }

    public RequestModel(String method, String body)
    {
        this.method = method;
        this.body = body;
    }

    public RequestModel(String method, String jsonText, UriInfo uriInfo, HttpHeaders headers)
    {
        this.method = method;
        this.body = jsonText;
        this.uriInfo = uriInfo;
        this.headers = headers;
    }

    public String getMethod()
    {
        return method;
    }

    public String getBody()
    {
        return body;
    }

    public UriInfo getUriInfo()
    {
        return uriInfo;
    }

    public HttpHeaders getHeaders()
    {
        return headers;
    }
}
