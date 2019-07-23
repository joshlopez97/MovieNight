package com.mn.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdatePasswordRequest
{
    private String email;

    private char[] oldPword;
    private char[] newPword;

    @JsonCreator
    public UpdatePasswordRequest(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "oldpword", required = true) char[] oldPword,
            @JsonProperty(value = "newpword", required = true) char[] newPword
    )
    {
        this.email = email;
        this.oldPword = oldPword;
        this.newPword = newPword;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public char[] getOldPword()
    {
        return oldPword;
    }

    public void setOldPword(char[] oldPword)
    {
        this.oldPword = oldPword;
    }

    public char[] getNewPword()
    {
        return newPword;
    }

    public void setNewPword(char[] newPword)
    {
        this.newPword = newPword;
    }
}
