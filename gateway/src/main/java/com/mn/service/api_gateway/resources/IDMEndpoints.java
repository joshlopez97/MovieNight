package com.mn.service.api_gateway.resources;

import com.mn.service.api_gateway.GatewayService;
import com.mn.service.api_gateway.models.idm.*;
import com.mn.service.api_gateway.utility.ResponseFactory;
import com.mn.service.api_gateway.validation.ModelValidationException;
import com.mn.service.api_gateway.validation.ModelValidator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

@Path("idm")
public class IDMEndpoints {
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUserRequest(@Context org.glassfish.grizzly.http.server.Request req, @Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText) {
        try {
            ModelValidator.verifyModel(jsonText, RegisterRequest.class);
            return ResponseFactory.createIdmResponse(headers, uriInfo, jsonText, GatewayService.getIdmConfigs().getEPUserRegister(), "POST", req.getRemoteAddr());
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, RegisterRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUserRequest(@Context org.glassfish.grizzly.http.server.Request req, @Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText) {
        try {
            ModelValidator.verifyModel(jsonText, LoginRequest.class);
            return ResponseFactory.createIdmResponse(headers, uriInfo, jsonText, GatewayService.getIdmConfigs().getEPUserLogin(), "POST", req.getRemoteAddr());
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, LoginRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("user/updatePassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePasswordRequest(@Context org.glassfish.grizzly.http.server.Request req, @Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText) {
        try {
            ModelValidator.verifyModel(jsonText, UpdatePasswordRequest.class);
            return ResponseFactory.createIdmResponse(headers, uriInfo, jsonText, GatewayService.getIdmConfigs().getEPUserLogin(), "POST", req.getRemoteAddr());
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, LoginRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifySessionRequest(@Context org.glassfish.grizzly.http.server.Request req, @Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText) {
        try {
            ModelValidator.verifyModel(jsonText, SessionStatusRequest.class);
            return ResponseFactory.createIdmResponse(headers, uriInfo, jsonText, GatewayService.getIdmConfigs().getEPSessionVerify(), "POST", req.getRemoteAddr());
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, SessionStatusRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyUserPrivilegeRequest(@Context org.glassfish.grizzly.http.server.Request req, @Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText) {
        try {
            ModelValidator.verifyModel(jsonText, UserPrivilegeRequest.class);
            return ResponseFactory.createIdmResponse(headers, uriInfo, jsonText, GatewayService.getIdmConfigs().getEPUserPrivilegeVerify(), "POST", req.getRemoteAddr());
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, UserPrivilegeRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
