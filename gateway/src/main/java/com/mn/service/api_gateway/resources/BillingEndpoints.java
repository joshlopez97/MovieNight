package com.mn.service.api_gateway.resources;

import com.mn.service.api_gateway.GatewayService;
import com.mn.service.api_gateway.models.billing.requests.cart.CartRequest;
import com.mn.service.api_gateway.models.billing.requests.cart.UpdateCartRequest;
import com.mn.service.api_gateway.models.billing.requests.creditcard.CreditCardRequest;
import com.mn.service.api_gateway.models.billing.requests.creditcard.UpdateCreditCardRequest;
import com.mn.service.api_gateway.models.billing.requests.customer.CustomerRequest;
import com.mn.service.api_gateway.models.billing.requests.customer.UpdateCustomerRequest;
import com.mn.service.api_gateway.models.billing.requests.order.OrderRequest;
import com.mn.service.api_gateway.models.billing.requests.cart.DeleteCartItemRequest;
import com.mn.service.api_gateway.utility.ResponseFactory;
import com.mn.service.api_gateway.validation.ModelValidationException;
import com.mn.service.api_gateway.validation.ModelValidator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

@Path("billing")
public class BillingEndpoints {
    @Path("cart/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToCartRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, UpdateCartRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCartInsert(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, UpdateCartRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("cart/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, UpdateCartRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCartUpdate(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, UpdateCartRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("cart/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCartRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, DeleteCartItemRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCartDelete(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, DeleteCartItemRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("cart/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCartRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, CartRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCartRetrieve(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, CartRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("cart/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCartRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, CartRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCartClear(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, CartRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("creditcard/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCardRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, UpdateCreditCardRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCcInsert(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, UpdateCreditCardRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("creditcard/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCardRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, UpdateCreditCardRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCcUpdate(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, UpdateCreditCardRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("creditcard/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCardRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, CreditCardRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCcDelete(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, CreditCardRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("creditcard/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, CreditCardRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCcRetrieve(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, CreditCardRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("customer/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomerRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, UpdateCustomerRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCustomerInsert(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, UpdateCustomerRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("customer/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, UpdateCustomerRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCustomerUpdate(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, UpdateCustomerRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("customer/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, CustomerRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPCustomerRetrieve(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, CustomerRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("order/place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrderRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, OrderRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPOrderPlace(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, OrderRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("order/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrderRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, OrderRequest.class);
            return ResponseFactory.createBillingResponse(headers, uriInfo, jsonText, GatewayService.getBillingConfigs().getEPOrderRetrieve(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, OrderRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
