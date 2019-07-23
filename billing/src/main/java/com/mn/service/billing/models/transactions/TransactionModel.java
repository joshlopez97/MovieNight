package com.mn.service.billing.models.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionModel
{
    private String transactionId;
    private String state;
    private AmountModel amount;
    private TransactionFee transaction_fee;
    private String create_time;
    private String update_time;
    private ItemModel[] items;

    @JsonCreator
    public TransactionModel(
            @JsonProperty(value = "transactionId", required = true) String transactionId,
            @JsonProperty(value = "state", required = true)  String state,
            @JsonProperty(value = "amount", required = true) AmountModel amount,
            @JsonProperty(value = "transaction_fee", required = true) TransactionFee transaction_fee,
            @JsonProperty(value = "create_time", required = true) String create_time,
            @JsonProperty(value = "update_time", required = true) String update_item,
            @JsonProperty(value = "transactions", required = true) ItemModel[] items
    )
    {
        this.transactionId = transactionId;
        this.state = state;
        this.amount = amount;
        this.transaction_fee = transaction_fee;
        this.create_time = create_time;
        this.update_time = update_item;
        this.items = items;
    }

    public String getTransactionId()
    {
        return transactionId;
    }

    public String getState()
    {
        return state;
    }

    public AmountModel getAmount()
    {
        return amount;
    }

    public TransactionFee getTransaction_fee()
    {
        return transaction_fee;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public String getUpdate_time()
    {
        return update_time;
    }

    public ItemModel[] getItems()
    {
        return items;
    }

    public void setItems(ItemModel[] items)
    {
        this.items = items;
    }
}

