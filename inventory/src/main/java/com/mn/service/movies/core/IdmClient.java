package com.mn.service.movies.core;

import com.mn.service.movies.MovieService;
import com.mn.service.movies.logger.ServiceLogger;
import com.mn.service.movies.models.requests.VerifyPrivilegeRequestModel;
import com.mn.service.movies.models.responses.VerifyPrivilegeResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;

public class IdmClient
{
    public static boolean checkUserPrivilege(String email, int plevel)
            throws IOException
    {
        ServiceLogger.LOGGER.info("Verifying privilege level with IDM...");

        // Create a new Client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        // Get the URI for the IDM
        ServiceLogger.LOGGER.info("Building URI...");
        URI IDM_URI = MovieService.getMovieConfigs().getIdmConfigs().getUri();

        ServiceLogger.LOGGER.info("Setting path to endpoint...");
        String IDM_ENDPOINT_PATH = MovieService.getMovieConfigs().getIdmConfigs().getPrivilege();

        // Create a WebTarget to send a request at
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT_PATH);
        System.out.println(webTarget.getUri());

        // Create an InvocationBuilder to create the HTTP request
        ServiceLogger.LOGGER.info("Starting invocation builder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Set the payload
        ServiceLogger.LOGGER.info("Setting payload of the request");
        VerifyPrivilegeRequestModel requestModel = new VerifyPrivilegeRequestModel(email, plevel);

        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Sent!");

        // Check that status code of the request
        if (response.getStatus() == 200)
        {
            ServiceLogger.LOGGER.info("Received Status 200");
            VerifyPrivilegeResponseModel responseModel = response.readEntity(VerifyPrivilegeResponseModel.class);
            System.out.println(responseModel.getResultCode());
            return responseModel.getResultCode() == 140;
        }
        else
        {
            ServiceLogger.LOGGER.info("Received Status " + response.getStatus());
            throw new IOException("Unable to reach IDM Service");
        }
    }
}
