package com.mn.service.api_gateway.models.billing.carts;

public class PricedCartItem
    extends CartItem
{
    private float price;

    public PricedCartItem(String email, String movieId, Integer quantity, float price)
    {
        super(email, movieId, quantity);
        this.price = price;
    }

    public float getPrice()
    {
        return price;
    }
}
