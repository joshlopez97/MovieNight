package com.mn.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mn.service.idm.core.UserManager;
import com.mn.service.idm.validation.ValidationException;
import com.mn.service.idm.validation.Validator;
import com.mn.service.idm.models.CreateUserRequest;
import com.mn.service.idm.models.UpdatePasswordRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

import static com.mn.service.idm.validation.Validator.validateFields;

@Path("user")
public class User
{
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(
            @QueryParam("id") Integer id,
            @QueryParam("email") String email,
            @QueryParam("plevel") String plevel
    )
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            Validator.validateUserQueryParams(id, email, plevel);
            ArrayNode userResults = UserManager.queryUsers(id, email, plevel);
            String response;
            if (userResults.size() == 0)
            {
                response = mapper.createObjectNode()
                        .put("resultCode", 1)
                        .put("message", "User not found.")
                        .toString();
            }
            else
            {
                response = mapper.createObjectNode()
                        .put("resultCode", 1)
                        .put("message", "User successfully retrieved.")
                        .set("user", userResults)
                        .toString();
            }
            return Response.status(Response.Status.OK).entity(response).build();
        }
        catch (ValidationException e)
        {
            String response = mapper.createObjectNode()
                    .put("resultCode", e.getCaseNumber())
                    .put("message", e.getMessage())
                    .toString();
            Response.Status statusCode = e.getCaseNumber() < 0 ? Response.Status.BAD_REQUEST
                    : Response.Status.OK;
            return Response.status(statusCode).entity(response).build();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String requestJsonData)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            CreateUserRequest createUserRequest = mapper.readValue(requestJsonData, CreateUserRequest.class);
            Validator.validateFields(createUserRequest);
            if (UserManager.userExists(createUserRequest.getEmail()))
                throw new ValidationException("Email already in use.", 16);
            if (createUserRequest.getPlevel().toLowerCase().equals("root"))
                throw new ValidationException("Creating user with “ROOT” privilege is not allowed.", 171);
            UserManager.insertUser(
                    createUserRequest.getEmail(),
                    createUserRequest.getPassword(),
                    createUserRequest.getPlevel()
            );
            String response = mapper.createObjectNode()
                    .put("resultCode", 170)
                    .put("message", "User Created.")
                    .toString();
            return Response.status(Response.Status.OK).entity(response).build();
        }
        catch (ValidationException e)
        {
            String response = mapper.createObjectNode()
                    .put("resultCode", e.getCaseNumber())
                    .put("message", e.getMessage())
                    .toString();

            Response.Status statusCode = e.getCaseNumber() < 0 ? Response.Status.BAD_REQUEST
                    : Response.Status.OK;
            return Response.status(statusCode).entity(response).build();
        }
        catch (JsonParseException e)
        {
            String response = mapper.createObjectNode()
                    .put("resultCode", -3)
                    .put("message", "JSON Parse Exception.")
                    .toString();
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        catch (JsonMappingException e)
        {
            String response = mapper.createObjectNode()
                    .put("resultCode", -2)
                    .put("message", "JSON Mapping Exception.")
                    .toString();
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        catch (IOException | SQLException e)
        {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("updatePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePassword(String requestJsonData)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            UpdatePasswordRequest updatePasswordRequest = mapper.readValue(requestJsonData, UpdatePasswordRequest.class);
            Validator.validateFields(updatePasswordRequest);
            if (!UserManager.authenticateUser(updatePasswordRequest.getEmail(), updatePasswordRequest.getOldPword()))
                throw new ValidationException("Password mismatch.", 11);

            UserManager.updatePassword(updatePasswordRequest.getEmail(), updatePasswordRequest.getNewPword());

            String response = mapper.createObjectNode()
                    .put("resultCode", 150)
                    .put("message", "Password updated successfully.")
                    .toString();

            return Response.status(Response.Status.OK).entity(response).build();
        }
        catch (ValidationException e)
        {
            String response = mapper.createObjectNode()
                    .put("resultCode", e.getCaseNumber())
                    .put("message", e.getMessage())
                    .toString();
            Response.Status statusCode = e.getCaseNumber() < 0 ? Response.Status.BAD_REQUEST
                    : Response.Status.OK;
            return Response.status(statusCode).entity(response).build();
        }
        catch (JsonParseException e)
        {
            String response = mapper.createObjectNode()
                    .put("resultCode", -3)
                    .put("message", "JSON Parse Exception.")
                    .toString();
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        catch (JsonMappingException e)
        {
            String response = mapper.createObjectNode()
                    .put("resultCode", -2)
                    .put("message", "JSON Mapping Exception.")
                    .toString();
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        catch (IOException | SQLException e)
        {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
