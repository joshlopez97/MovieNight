package com.mn.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPrivilegeRequest
{
    private int plevel;
    private String email;

    @JsonCreator
    public UserPrivilegeRequest(
            @JsonProperty(value = "plevel", required = true) int plevel,
            @JsonProperty(value = "email", required = true) String email
    )
    {
        this.email = email;
        this.plevel = plevel;
    }

    public int getPlevel()
    {
        return plevel;
    }

    public void setPlevel(int plevel)
    {
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
}
