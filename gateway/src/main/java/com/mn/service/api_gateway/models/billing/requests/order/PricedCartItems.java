package com.mn.service.api_gateway.models.billing.requests.order;


import com.mn.service.api_gateway.models.billing.carts.PricedCartItem;

import java.util.List;

public class PricedCartItems
{
    private float totalCost;
    private List<PricedCartItem> items;

    public PricedCartItems(List<PricedCartItem> items)
    {
        this.items = items;
        float total = 0;
        for (PricedCartItem item : items)
        {
            total += item.getPrice();
        }
        this.totalCost = total;
    }

    public float getTotalCost()
    {
        return totalCost;
    }

    public List<PricedCartItem> getItems()
    {
        return items;
    }
}
