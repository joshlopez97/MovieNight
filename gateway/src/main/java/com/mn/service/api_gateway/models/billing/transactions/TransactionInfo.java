package com.mn.service.api_gateway.models.billing.transactions;

public class TransactionInfo
{
    private Integer saleId;
    private String token;
    private String transactionId;

    public TransactionInfo(Integer saleId, String token, String transactionId)
    {
        this.saleId = saleId;
        this.token = token;
        this.transactionId = transactionId;
    }

    public Integer getSaleId()
    {
        return saleId;
    }

    public String getToken()
    {
        return token;
    }

    public String getTransactionId()
    {
        return transactionId;
    }
}
