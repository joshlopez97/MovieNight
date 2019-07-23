package com.mn.service.billing.models.requests;

public class PayPalPaymentRequest
{
    private String payerID;
    private String paymentID;
    private String token;

    public PayPalPaymentRequest(String payerID, String paymentID, String token)
    {
        this.payerID = payerID;
        this.paymentID = paymentID;
        this.token = token;
    }

    public String getPayerID()
    {
        return payerID;
    }

    public String getPaymentID()
    {
        return paymentID;
    }

    public String getToken()
    {
        return token;
    }
}
