package com.mn.service.api_gateway.resources;

import com.mn.service.api_gateway.logger.ServiceLogger;
import com.mn.service.api_gateway.models.NoContentResponseModel;
import com.mn.service.api_gateway.models.ResponseModel;
import com.mn.service.api_gateway.utility.ResponseManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public class GatewayEndpoints
{
    @Path("report")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response report(@Context HttpHeaders headers)
    {
        try
        {
            String transactionid = headers.getHeaderString("transactionid");
            if (transactionid == null)
            {
                ServiceLogger.LOGGER.info("No transactionid provided. Returning 500.");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

            ServiceLogger.LOGGER.info("Retrieving response for transaction " + transactionid);
            ResponseModel response = ResponseManager.popResponse(transactionid);

            if (response != null)
            {
                ServiceLogger.LOGGER.info("Retrieved response: " + response.toString());
                return Response.status(response.getHttpstatus()).header("sessionid", response.getSessionid()).entity(response.getResponse()).build();
            }
            ServiceLogger.LOGGER.info("No response found in DB.");
            return NoContentResponseModel.createResponse(transactionid);

        }
        catch (Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
