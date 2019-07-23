package com.mn.service.billing.core;

import com.mn.service.billing.BillingService;
import com.mn.service.billing.models.requests.PayPalPaymentRequest;
import com.mn.service.billing.models.responses.CreatePaymentResponse;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.mn.service.billing.logger.ServiceLogger;
import com.mn.service.billing.models.transactions.AmountModel;
import com.mn.service.billing.models.transactions.TransactionFee;
import com.mn.service.billing.models.transactions.TransactionModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/paypal")
public class PayPalClient
{
    private static String clientId = "AWCCvIBmjVBPYmIiMFX0oXimYhs8syuxS53NbniXLw_T7wGz6i4qpjbiW5m4AZPLTPZAiSgER6NmmlZX";
    private static String clientSecret = "EKm5q-q-shWDftsuRLP-VZhw2_64QEm57so_56fn4smg5GvvtZpGW7Ccb3TZcmTv227iE9hw_eblqj1R";
    private static String state = "sandbox";

    public static CreatePaymentResponse createPayment(String sum, String returnPath){
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(sum);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        System.out.println("Redirect to " + BillingService.getConfigs().getFullURI() + returnPath);
        redirectUrls.setCancelUrl(BillingService.getConfigs().getFullURI() + returnPath);
        redirectUrls.setReturnUrl(BillingService.getConfigs().getFullURI() + returnPath);
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;
        String redirectURL = null;
        try {
            APIContext context = new APIContext(clientId, clientSecret, state);
            createdPayment = payment.create(context);
            if (createdPayment!=null)
            {
                List<Links> links = createdPayment.getLinks();
                for (Links link : links)
                {
                    if (link.getRel().equals("approval_url")){
                        redirectURL = link.getHref();
                        break;
                    }
                }
            }
            if (redirectURL == null)
                throw new PayPalRESTException("no redirect URL provided");
        }
        catch (PayPalRESTException e)
        {
            ServiceLogger.LOGGER.severe(e.getMessage());
            return null;
        }
        return new CreatePaymentResponse(redirectURL);
    }

    public static String completePayment(PayPalPaymentRequest req)
    {
        Payment payment = new Payment();
        payment.setId(req.getPaymentID());

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(req.getPayerID());
        String transactionId = null;
        try
        {
            APIContext context = new APIContext(clientId, clientSecret, state);
            Payment createdPayment = payment.execute(context, paymentExecution);
            if (createdPayment!=null)
            {
                transactionId = createdPayment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
            }
        }
        catch (PayPalRESTException e) {
            ServiceLogger.LOGGER.severe(e.getMessage());
            return null;
        }
        return transactionId;
    }

    public static TransactionModel getTransactionDetails(String transactionID)
    {
        APIContext apiContext = new APIContext(clientId, clientSecret, state);
        try
        {
            Sale sale = Sale.get(apiContext, transactionID);
            return new TransactionModel(
                    sale.getId(),
                    sale.getState(),
                    new AmountModel(
                            sale.getAmount().getTotal(),
                            sale.getAmount().getCurrency()
                    ),
                    new TransactionFee(
                            sale.getTransactionFee().getValue(),
                            sale.getTransactionFee().getCurrency()
                    ),
                    sale.getCreateTime(),
                    sale.getUpdateTime(),
                    null
            );
        }
        catch (PayPalRESTException e)
        {
            ServiceLogger.LOGGER.severe(e.getMessage());
            return null;
        }
    }
}
