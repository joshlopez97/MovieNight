package com.mn.service.billing.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.billing.models.transactions.TransactionModel;

public class RetrieveOrderResponse
    extends JsonResponse
{
    private TransactionModel[] transactions;

    @JsonCreator
    public RetrieveOrderResponse(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty(value = "transactions", required = true) TransactionModel[] transactions)
    {
        super(resultCode, message);
        this.transactions = transactions;
    }

    public TransactionModel[] getTransactions()
    {
        return transactions;
    }
}
