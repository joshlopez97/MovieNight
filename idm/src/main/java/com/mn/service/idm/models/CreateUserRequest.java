package com.mn.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest
{
    private String email;
    private char[] password;
    private String plevel;

    @JsonCreator
    public CreateUserRequest(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "password", required = true) char[] password,
            @JsonProperty(value = "plevel", required = true) String plevel
    )
    {
        this.email = email;
        this.password = password;
        this.plevel = plevel;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public char[] getPassword()
    {
        return password;
    }

    public void setPassword(char[] password)
    {
        this.password = password;
    }

    public String getPlevel()
    {
        return plevel;
    }

    public void setPlevel(String plevel)
    {
        this.plevel = plevel;
    }
}
