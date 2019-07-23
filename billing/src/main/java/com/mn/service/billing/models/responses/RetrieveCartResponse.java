package com.mn.service.billing.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.billing.models.carts.CartItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveCartResponse
    extends JsonResponse
{
    private CartItem[] items;

    @JsonCreator
    public RetrieveCartResponse(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty(value = "items", required = true) CartItem[] items)
    {
        super(resultCode, message);
        this.items = items;
    }

    public CartItem[] getItems()
    {
        return items;
    }
}
