package com.mn.service.movies.configs;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class IDMConfigs
{
    IDMConfigs(String scheme, String hostname, int port, String path, String privilege)
    {
        this.scheme = scheme;
        this.hostname = hostname;
        this.port = port;
        this.path = path;
        this.privilege = privilege;
    }

    private String scheme;

    public String getScheme()
    {
        return scheme;
    }

    public String getHostname()
    {
        return hostname;
    }

    public int getPort()
    {
        return port;
    }

    public String getPath()
    {
        return path;
    }

    public URI getUri()
    {
        return UriBuilder.fromUri(scheme + hostname + path).port(port).build();
    }

    public String getPrivilege()
    {
        return privilege;
    }

    private String hostname;
    private int port;
    private String path;
    private String privilege;
}
