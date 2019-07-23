package com.mn.service.billing.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.billing.core.CreditCardManager;
import com.mn.service.billing.models.CreditCard;
import com.mn.service.billing.models.requests.creditcard.CreditCardRequest;
import com.mn.service.billing.models.requests.creditcard.UpdateCreditCardRequest;
import com.mn.service.billing.models.responses.JsonResponse;
import com.mn.service.billing.models.responses.ResponseFactory;
import com.mn.service.billing.models.responses.RetrieveCreditCardResponse;
import com.mn.service.billing.validation.ValidationException;
import com.mn.service.billing.validation.Validator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("creditcard")
public class CreditCardEndpoints
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
            UpdateCreditCardRequest request = mapper.readValue(jsonInput, UpdateCreditCardRequest.class);
            Validator.validateRequest(request);
            if (!CreditCardManager.insertCreditCard(request))
                throw new ValidationException(325, "Duplicate insertion.");
            JsonResponse response = new JsonResponse(3200, "Credit card inserted successfully.");
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
            UpdateCreditCardRequest request = mapper.readValue(jsonInput, UpdateCreditCardRequest.class);
            Validator.validateRequest(request);
            if (!CreditCardManager.updateCreditCard(request))
                throw new ValidationException(324, "Credit card does not exist.");
            JsonResponse response = new JsonResponse(3210, "Credit card updated successfully.");
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
            CreditCardRequest request = mapper.readValue(jsonInput, CreditCardRequest.class);
            Validator.validateRequest(request);
            if (!CreditCardManager.deleteCreditCard(request))
                throw new ValidationException(324, "Credit card does not exist.");
            JsonResponse response = new JsonResponse(3220, "Credit card deleted successfully.");
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
            CreditCardRequest request = mapper.readValue(jsonInput, CreditCardRequest.class);
            Validator.validateRequest(request);
            CreditCard creditCard = CreditCardManager.retrieveCreditCard(request);
            if (creditCard == null)
                throw new ValidationException(324, "Credit card does not exist.");
            RetrieveCreditCardResponse response = new RetrieveCreditCardResponse(
                    3230,
                    "Credit card retrieved successfully.",
                    creditCard
            );
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }
}
