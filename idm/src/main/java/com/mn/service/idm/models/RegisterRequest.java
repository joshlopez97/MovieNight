package com.mn.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequest
    extends LoginRequest
{
    @JsonCreator
    public RegisterRequest(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "password", required = true) char[] password
    )
    {
        super(email, password);
    }
}
