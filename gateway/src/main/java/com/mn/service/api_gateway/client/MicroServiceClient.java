package com.mn.service.api_gateway.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.api_gateway.GatewayService;
import com.mn.service.api_gateway.logger.ServiceLogger;
import com.mn.service.api_gateway.models.idm.SessionStatusRequest;
import com.mn.service.api_gateway.models.idm.UserPrivilegeRequest;
import com.mn.service.api_gateway.threadpool.ClientRequest;
import com.mn.service.api_gateway.validation.ResultCodes;
import com.mn.service.api_gateway.models.ResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

public class MicroServiceClient
{
    private static Client client = ClientBuilder.newClient().register(JacksonFeature.class);

    public static Response verifyPrivilege(String email, int privilege)
    {
        ServiceLogger.LOGGER.info("Verifying user " + email + " has at least plevel " + privilege);
        WebTarget webTarget = client
                .target(GatewayService.getIdmConfigs().getIdmUri())
                .path(GatewayService.getIdmConfigs().getEPUserPrivilegeVerify());

        // Create an InvocationBuilder to create the HTTP request
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        UserPrivilegeRequest requestModel = new UserPrivilegeRequest(privilege, email);
        String request;
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            request = mapper.writeValueAsString(requestModel);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
            return null;
        }

        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request to " + webTarget.getUri());
        Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Received status " + response.getStatus());

        // Check that status code of the request
        if (response.getStatus() != 200 && response.getStatus() != 400)
            return null;

        return response;
    }

    public static Response verifySession(String sessionID, String email)
    {
        ServiceLogger.LOGGER.info("Verifying session " + sessionID);
        if (email == null)
            return ResultCodes.generateResponse(ResultCodes.EMAIL_NOT_FOUND_HEADER);
        if (sessionID == null)
            return ResultCodes.generateResponse(ResultCodes.SESSIONID_NOT_FOUND_HEADER);

        String IDM_URI = GatewayService.getIdmConfigs().getIdmUri();
        String IDM_ENDPOINT_PATH = GatewayService.getIdmConfigs().getEPSessionVerify();

        // Create a WebTarget to send a request at
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT_PATH);
        System.out.println(webTarget.getUri());

        // Create an InvocationBuilder to create the HTTP request
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Set the payload
        SessionStatusRequest requestModel = new SessionStatusRequest(sessionID, email);
        String request;
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            request = mapper.writeValueAsString(requestModel);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
            return null;
        }

        // Send the request and save it to a Response
        Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));

        // Check that status code of the request
        if (response.getStatus() == 200 || response.getStatus() == 400)
        {
            return response;
        }
        ServiceLogger.LOGGER.info("Received status code " + response.getStatus() + " from IDM service");
        return null;
    }

    public static ResponseModel invoke(ClientRequest clientRequest)
    {
        ServiceLogger.LOGGER.info(
                "Trying to invoke " + clientRequest.getRequest().getMethod() +
                " request to " + clientRequest.getRequest().getUriInfo().getAbsolutePath()
        );
        String method = clientRequest.getRequest().getMethod();
        // Create a WebTarget to send a request at
        WebTarget webTarget = client.target(clientRequest.getURI()).path(clientRequest.getEndpoint());

        // Set query params
        for (Map.Entry<String, List<String>> entry : clientRequest.getRequest().getUriInfo().getQueryParameters().entrySet()) {
            for (String value : entry.getValue())
            {
                ServiceLogger.LOGGER.info("Adding query param (" + entry.getKey() + ", " + value + ")");
                webTarget = webTarget.queryParam(entry.getKey(), value);
            }
        }

        // Set path params
        for (Map.Entry<String, List<String>> entry : clientRequest.getRequest().getUriInfo().getPathParameters().entrySet()) {
            for (String value : entry.getValue())
            {
                ServiceLogger.LOGGER.info("Adding path param (" + entry.getKey() + ", " + value + ")");
                webTarget = webTarget.resolveTemplate(entry.getKey(), value);
            }
        }

        // Create an InvocationBuilder to create the HTTP request
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Set headers
        for (Map.Entry<String, List<String>> entry : clientRequest.getRequest().getHeaders().getRequestHeaders().entrySet())
        {
            for (String value : entry.getValue())
            {
                ServiceLogger.LOGGER.info("Adding header (" + entry.getKey() + ", " + value + ")");
                invocationBuilder.header(entry.getKey(), value);
            }
        }
        if (clientRequest.getIp_address() != null)
        {
            ServiceLogger.LOGGER.info("Adding IP Address as header (source_ip, " + clientRequest.getIp_address() + ")");
            invocationBuilder.header("source_ip", clientRequest.getIp_address());
        }


        // Send the request and save it to a Response
        Response response;
        if (method.toUpperCase().equals("POST"))
        {
            // Set the payload
            ServiceLogger.LOGGER.info("with payload " + clientRequest.getRequest().getBody());
            response = invocationBuilder.post(Entity.entity(clientRequest.getRequest().getBody(), MediaType.APPLICATION_JSON));
        }
        else
        {
            response = invocationBuilder.build(method).invoke();
        }

        ResponseModel responseModel = new ResponseModel(
                clientRequest.getTransactionID(),
                clientRequest.getEmail(),
                clientRequest.getSessionID(),
                response.readEntity(String.class),
                response.getStatus()
        );
        System.out.println(responseModel.toString());
        return responseModel;
    }
}
