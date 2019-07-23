package com.mn.service.billing.models.responses;

import com.mn.service.billing.logger.ServiceLogger;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

public class CreatePaymentResponse
{
    private String redirectUrl;
    private String token;

    public CreatePaymentResponse(String redirectUrl)
    {
        try
        {
            this.redirectUrl = redirectUrl;
            String[] queryParams = URI.create(redirectUrl).getQuery().split("&");
            for (String pair : queryParams)
            {
                int idx = pair.indexOf("=");
                if (URLDecoder.decode(pair.substring(0, idx), "UTF-8").equals("token"))
                {
                    this.token = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                    break;
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            ServiceLogger.LOGGER.severe(e.getMessage());
        }
    }

    public String getRedirectUrl()
    {
        return redirectUrl;
    }

    public String getToken()
    {
        return token;
    }
}
