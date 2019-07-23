package com.mn.service.billing.models.carts;

public class PricedCartItem
    extends CartItem
{
    private float price;
    private float discount;

    public PricedCartItem(String email, String movieId, Integer quantity, float price, float discount)
    {
        super(email, movieId, quantity);
        this.price = price;
        this.discount = discount;
    }

    public float getPrice()
    {
        return price;
    }

    public float getDiscount()
    {
        return discount;
    }
}
