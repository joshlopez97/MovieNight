package com.mn.service.billing.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.billing.core.CreditCardManager;
import com.mn.service.billing.core.CustomerManager;
import com.mn.service.billing.models.Customer;
import com.mn.service.billing.models.responses.RetrieveCustomerResponse;
import com.mn.service.billing.validation.ValidationException;
import com.mn.service.billing.validation.Validator;
import com.mn.service.billing.models.requests.customer.CustomerRequest;
import com.mn.service.billing.models.requests.customer.UpdateCustomerRequest;
import com.mn.service.billing.models.responses.JsonResponse;
import com.mn.service.billing.models.responses.ResponseFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("customer")
public class CustomerEndpoints
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
            UpdateCustomerRequest request = mapper.readValue(jsonInput, UpdateCustomerRequest.class);
            Validator.validateRequest(request);
            if (!CreditCardManager.creditCardExists(request.getCcId()))
                throw new ValidationException(331, "Credit card ID not found.");
            if (!CustomerManager.insertCustomer(request))
                throw new ValidationException(333, "Duplicate insertion.");
            JsonResponse response = new JsonResponse(3300, "Customer inserted successfully.");
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
            UpdateCustomerRequest request = mapper.readValue(jsonInput, UpdateCustomerRequest.class);
            Validator.validateRequest(request);
            if (!CreditCardManager.creditCardExists(request.getCcId()))
                throw new ValidationException(331, "Credit card ID not found.");
            if (!CustomerManager.updateCustomer(request))
                throw new ValidationException(332, "Customer does not exist.");
            JsonResponse response = new JsonResponse(3310, "Customer updated successfully.");
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
            CustomerRequest request = mapper.readValue(jsonInput, CustomerRequest.class);
            Validator.validateRequest(request);
            Customer customer = CustomerManager.retrieveCustomer(request);
            if (customer == null)
                throw new ValidationException(332, "Customer does not exist.");
            RetrieveCustomerResponse response = new RetrieveCustomerResponse(
                    3320,
                    "Customer retrieved successfully.",
                    customer
            );
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }
}
