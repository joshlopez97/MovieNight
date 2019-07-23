package com.mn.service.billing.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.billing.models.CreditCard;

public class RetrieveCreditCardResponse
    extends JsonResponse
{
    private CreditCard creditcard;

    public RetrieveCreditCardResponse(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty(value = "creditcard", required = true) CreditCard creditcard)
    {
        super(resultCode, message);
        this.creditcard = creditcard;
    }

    public CreditCard getCreditcard()
    {
        return creditcard;
    }
}
