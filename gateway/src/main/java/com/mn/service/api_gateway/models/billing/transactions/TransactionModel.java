package com.mn.service.api_gateway.models.billing.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionModel
{
    private String transactionId;
    private String state;
    private AmountModel amount;
    private TransactionFee transactionFee;
    private String createTime;
    private String updateTime;
    private ItemModel[] items;

    @JsonCreator
    public TransactionModel(
            @JsonProperty(value = "transactionId", required = true) String transactionId,
            @JsonProperty(value = "state", required = true)  String state,
            @JsonProperty(value = "amount", required = true) AmountModel amount,
            @JsonProperty(value = "transaction_fee", required = true) TransactionFee transactionFee,
            @JsonProperty(value = "create_time", required = true) String createTime,
            @JsonProperty(value = "update_time", required = true) String updateTime,
            @JsonProperty(value = "transactions", required = true) ItemModel[] items
    )
    {
        this.transactionId = transactionId;
        this.state = state;
        this.amount = amount;
        this.transactionFee = transactionFee;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public TransactionFee getTransactionFee()
    {
        return transactionFee;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public String getUpdateTime()
    {
        return updateTime;
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

