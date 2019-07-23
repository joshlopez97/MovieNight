package com.mn.service.billing.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.billing.models.carts.CartItem;
import com.mn.service.billing.models.requests.cart.CartRequest;
import com.mn.service.billing.models.requests.cart.UpdateCartRequest;
import com.mn.service.billing.models.responses.RetrieveCartResponse;
import com.mn.service.billing.validation.ValidationException;
import com.mn.service.billing.validation.Validator;
import com.mn.service.billing.core.CartManager;
import com.mn.service.billing.models.requests.cart.DeleteCartItemRequest;
import com.mn.service.billing.models.responses.ResponseFactory;
import com.mn.service.billing.models.responses.JsonResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("cart")
public class CartEndpoints
{
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String jsonInput)
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            UpdateCartRequest request = mapper.readValue(jsonInput, UpdateCartRequest.class);
            Validator.validateRequest(request);
            if (!CartManager.insertIntoCart(request))
                throw new ValidationException(311, "Duplicate insertion.");
            JsonResponse response = new JsonResponse(3100, "Shopping cart item inserted successfully.");
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(String jsonInput)
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            UpdateCartRequest request = mapper.readValue(jsonInput, UpdateCartRequest.class);
            Validator.validateRequest(request);
            if (!CartManager.updateCart(request))
                throw new ValidationException(312, "Shopping item does not exist.");
            JsonResponse response = new JsonResponse(3110, "Shopping cart item updated successfully.");
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(String jsonInput)
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            DeleteCartItemRequest request = mapper.readValue(jsonInput, DeleteCartItemRequest.class);
            Validator.validateRequest(request);
            if (!CartManager.deleteCartEntry(request))
                throw new ValidationException(312, "Shopping item does not exist.");
            JsonResponse response = new JsonResponse(3120, "Shopping cart item deleted successfully.");
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
            CartRequest request = mapper.readValue(jsonInput, CartRequest.class);
            Validator.validateRequest(request);
            List<CartItem> cartItems = CartManager.retrieveCart(request);
            if (cartItems.isEmpty())
                throw new ValidationException(312, "Shopping item does not exist.");
            RetrieveCartResponse response = new RetrieveCartResponse(
                    3130,
                    "Shopping cart retrieved successfully.",
                    cartItems.toArray(new CartItem[0])
            );
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clear(String jsonInput)
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            CartRequest request = mapper.readValue(jsonInput, CartRequest.class);
            Validator.validateRequest(request);
            CartManager.clearCart(request);
            JsonResponse response = new JsonResponse(
                    3140,
                    "Shopping cart cleared successfully."
            );
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }
}
