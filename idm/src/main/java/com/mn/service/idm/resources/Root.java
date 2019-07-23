package com.mn.service.idm.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.idm.core.SessionManager;
import com.mn.service.idm.core.UserManager;
import com.mn.service.idm.logger.ServiceLogger;
import com.mn.service.idm.models.*;
import com.mn.service.idm.security.Session;
import com.mn.service.idm.validation.ValidationException;
import com.mn.service.idm.validation.Validator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.mn.service.idm.validation.Validator.validateFields;

@Path("")
public class Root
{
    @POST
    @Path("privilege")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response privilege(String requestJsonData)
            throws JsonProcessingException
    {
        ServiceLogger.LOGGER.info("/idm/privilege with data " + requestJsonData);
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            UserPrivilegeRequest userPrivilegeRequest = mapper.readValue(requestJsonData, UserPrivilegeRequest.class);
            Validator.validateFields(userPrivilegeRequest);

            int plevel = UserManager.getUserPlevel(userPrivilegeRequest.getEmail());
            if (plevel == -1 || plevel > userPrivilegeRequest.getPlevel())
                throw new ValidationException("User has insufficient privilege level.", 141);

            String response = mapper.createObjectNode()
                    .put("resultCode", 140)
                    .put("message", "User has sufficient privilege level.")
                    .toString();
            ServiceLogger.LOGGER.info(response);
            return Response.status(Response.Status.OK).entity(response).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }


    @POST
    @Path("session")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response session(String requestJsonData)
            throws JsonProcessingException
    {
        ServiceLogger.LOGGER.info("/idm/session with data " + requestJsonData);
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            SessionStatusRequest sessionStatusRequest = mapper.readValue(requestJsonData, SessionStatusRequest.class);
            Validator.validateFields(sessionStatusRequest);
            if (!UserManager.userExists(sessionStatusRequest.getEmail()))
                throw new ValidationException("User not found.", 14);

            Session session = SessionManager.getSession(sessionStatusRequest.getEmail(), sessionStatusRequest.getSessionID());
            if (session == null)
                throw new ValidationException("Session not found.", 134);
            String status = session.getStatusString().toLowerCase();
            String message = "Session is " + status + ".";
            int resultCode;
            String sessionid = null;
            switch (status)
            {
                case "active":
                    resultCode = 130;
                    sessionid = session.getSessionID().toString();
                    break;
                case "expired":
                    resultCode = 131;
                    break;
                case "closed":
                    resultCode = 132;
                    break;
                case "revoked":
                    resultCode = 133;
                    break;
                default:
                    ServiceLogger.LOGGER.severe("Status " + status + " is invalid.");
                    throw new IOException();
            }
            ServiceLogger.LOGGER.info("returning sessionid = " + sessionid);

            SessionStatusResponse responseObj = new SessionStatusResponse(resultCode, message, sessionid);
            String response = mapper.writeValueAsString(responseObj);

            ServiceLogger.LOGGER.info(response);

            return Response.status(Response.Status.OK).entity(response).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String requestJsonData)
            throws JsonProcessingException
    {
        ServiceLogger.LOGGER.info("/idm/login with data " + requestJsonData);
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            LoginRequest loginRequest = mapper.readValue(requestJsonData, LoginRequest.class);
            Validator.validateFields(loginRequest);
            if (!UserManager.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword()))
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

            Session session = SessionManager.getActiveSession(loginRequest.getEmail());
            if (session == null || session.getStatus() != Session.ACTIVE)
                session = SessionManager.createSession(loginRequest.getEmail());
            ServiceLogger.LOGGER.info("User logged in successfully");
            String response = mapper.createObjectNode()
                    .put("resultCode", 120)
                    .put("message", "User logged in successfully.")
                    .put("sessionID", session.getSessionID().toString())
                    .toString();

            return Response.status(Response.Status.OK).entity(response).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(String requestJsonData)
            throws JsonProcessingException
    {
        ServiceLogger.LOGGER.info("/idm/register with data " + requestJsonData);
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            RegisterRequest registerRequest = mapper.readValue(requestJsonData, RegisterRequest.class);
            validateFields(registerRequest);
            if (UserManager.userExists(registerRequest.getEmail()))
                throw new ValidationException("Email already in use.", 16);
            UserManager.insertUser(registerRequest);

            String response = mapper.createObjectNode()
                    .put("resultCode", 110)
                    .put("message", "User registered successfully.")
                    .toString();
            ServiceLogger.LOGGER.info("User registered successfully");

            return Response.status(Response.Status.OK).entity(response).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }
}
