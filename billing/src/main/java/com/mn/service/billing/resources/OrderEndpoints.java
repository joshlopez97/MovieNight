package com.mn.service.billing.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.billing.core.*;
import com.mn.service.billing.logger.ServiceLogger;
import com.mn.service.billing.models.requests.PayPalPaymentRequest;
import com.mn.service.billing.models.requests.cart.CartRequest;
import com.mn.service.billing.models.requests.order.PricedCartItems;
import com.mn.service.billing.models.responses.*;
import com.mn.service.billing.models.transactions.TransactionModel;
import com.mn.service.billing.validation.ValidationException;
import com.mn.service.billing.validation.Validator;
import com.mn.service.billing.models.requests.order.OrderRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("order")
public class OrderEndpoints
{
    @Path("place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response place(String jsonInput)
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            OrderRequest request = mapper.readValue(jsonInput, OrderRequest.class);
            Validator.validateRequest(request);
            if (!CustomerManager.customerExists(request.getEmail()))
                throw new ValidationException(332, "Customer does not exist.");
            PricedCartItems cartItems = CartManager.retrievePricedCart(new CartRequest(request.getEmail()));
            if (cartItems.getItems().isEmpty())
                throw new ValidationException(341, "Shopping cart for this customer not found.");

            CreatePaymentResponse paypalResponse = PayPalClient.createPayment(String.format("%.02f", cartItems.getTotalCost()), "/order/complete");
            if (paypalResponse == null)
                throw new ValidationException(342, "Create payment failed.");
            OrderManager.placeOrder(cartItems, paypalResponse.getToken());
            CartManager.clearCart(new CartRequest(request.getEmail()));

            PlaceOrderResponse response = new PlaceOrderResponse(3400, "Order placed successfully.", paypalResponse.getRedirectUrl(), paypalResponse.getToken());
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("complete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response complete(
            @QueryParam("paymentId") String paymentId,
            @QueryParam("token") String token,
            @QueryParam("PayerID") String payerId
    )
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            PayPalPaymentRequest request = new PayPalPaymentRequest(payerId, paymentId, token);
            String transactionID = PayPalClient.completePayment(request);
            if (transactionID == null)
                throw new ValidationException(3422, "Payment can not be completed.");
            if (!TransactionManager.setTransactionId(transactionID, token))
                throw new ValidationException(3421, "Token not found.");

            JsonResponse response = new JsonResponse(3420, "Payment is completed successfully.");
            ServiceLogger.LOGGER.info("Returning " + mapper.writeValueAsString(response));
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(String jsonInput)
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            OrderRequest request = mapper.readValue(jsonInput, OrderRequest.class);
            Validator.validateRequest(request);
            if (!CustomerManager.customerExists(request.getEmail()))
                throw new ValidationException(332, "Customer does not exist.");
            TransactionModel[] transactions = OrderManager.retrieveOrders(request);
            RetrieveOrderResponse response = new RetrieveOrderResponse(
                    3410,
                    "Orders retrieved successfully.",
                    transactions
            );
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }
}
