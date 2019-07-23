package com.mn.service.api_gateway.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.api_gateway.GatewayService;
import com.mn.service.api_gateway.logger.ServiceLogger;
import com.mn.service.api_gateway.models.NoContentResponseModel;
import com.mn.service.api_gateway.models.RequestModel;
import com.mn.service.api_gateway.models.idm.VerifySessionResponse;
import com.mn.service.api_gateway.threadpool.ClientRequest;
import com.mn.service.api_gateway.validation.ResultCodes;
import com.mn.service.api_gateway.client.MicroServiceClient;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.io.IOException;

public class ResponseFactory
{
    public static Response createIdmResponse(
            HttpHeaders headers,
            UriInfo uriInfo,
            String jsonText,
            String endpoint,
            String method,
            String ip_address)
    {
        if (endpoint.equals(GatewayService.getIdmConfigs().getEPUserPrivilegeVerify()))
            return createResponse(headers, uriInfo, jsonText, endpoint, method, GatewayService.getIdmConfigs().getIdmUri(), ip_address);
        return createResponseNoVerification(headers, uriInfo, jsonText, endpoint, method, GatewayService.getIdmConfigs().getIdmUri(), ip_address);
    }

    public static Response createMovieResponse(
            HttpHeaders headers,
            UriInfo uriInfo,
            String jsonText,
            String endpoint,
            String method
    )
    {
        return createResponse(headers, uriInfo, jsonText, endpoint, method, GatewayService.getMovieConfigs().getMoviesUri(), null);
    }

    public static Response createBillingResponse(
            HttpHeaders headers,
            UriInfo uriInfo,
            String jsonText,
            String endpoint,
            String method
    )
    {
        return createResponse(headers, uriInfo, jsonText, endpoint, method, GatewayService.getBillingConfigs().getBillingUri(), null);
    }

    private static Response createResponse(
            HttpHeaders headers,
            UriInfo uriInfo,
            String jsonText,
            String endpoint,
            String method,
            String URI,
            String ip_address)
    {
        String email = headers.getHeaderString("email");
        String sessionid = headers.getHeaderString("sessionid");
        if (email == null)
            return ResultCodes.generateResponse(ResultCodes.EMAIL_NOT_FOUND_HEADER);
        if (sessionid == null)
            return ResultCodes.generateResponse(ResultCodes.SESSIONID_NOT_FOUND_HEADER);
        Response verifySessionResponse = MicroServiceClient.verifySession(sessionid, email);

        if (verifySessionResponse == null || verifySessionResponse.getEntity() == null)
        {
            ServiceLogger.LOGGER.warning("Could not verify session. Response was ignored. Returning 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        int httpstatus = verifySessionResponse.getStatus();
        VerifySessionResponse body;
        try
        {
            String json = verifySessionResponse.readEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();
            body = mapper.readValue(json, VerifySessionResponse.class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.warning("Unable to deserialize JSON response. Returning 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        ServiceLogger.LOGGER.info("Session status: " + body.getResultCode() + ", " + body.getMessage());
        if (body.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(httpstatus).entity(body.toString()).build();
        if (body.getSessionID() != null)
            sessionid = body.getSessionID();

        // Generate transaction id.
        ClientRequest cr = new ClientRequest(
                headers.getHeaderString("email"),
                sessionid,
                new RequestModel(method, jsonText, uriInfo, headers),
                URI,
                endpoint,
                null);
        ServiceLogger.LOGGER.info("URI:" + uriInfo.getAbsolutePath().toString());
        ServiceLogger.LOGGER.info("sessionid:" + body.getSessionID());

        // Now that the ClientRequest has been built, we can add it to our queue of requests.
        GatewayService.getThreadPool().add(cr);

        // Generate a NoContentResponseModel to send to the client containing the time to wait before asking for the
        // requested information, and the transactionID.
        return NoContentResponseModel.createResponse(cr.getTransactionID(), cr.getSessionID());
    }

    private static Response createResponseNoVerification(
            HttpHeaders headers,
            UriInfo uriInfo,
            String jsonText,
            String endpoint,
            String method,
            String URI,
            String ip_address)
    {
        // Generate transaction id.
        ClientRequest cr = new ClientRequest(
                headers.getHeaderString("email"),
                headers.getHeaderString("sessionid"),
                new RequestModel(method, jsonText, uriInfo, headers),
                URI,
                endpoint,
                ip_address);

        // Now that the ClientRequest has been built, we can add it to our queue of requests.
        GatewayService.getThreadPool().add(cr);

        // Generate a NoContentResponseModel to send to the client containing the time to wait before asking for the
        // requested information, and the transactionID.
        return NoContentResponseModel.createResponse(cr.getTransactionID(), cr.getSessionID());
    }
}
