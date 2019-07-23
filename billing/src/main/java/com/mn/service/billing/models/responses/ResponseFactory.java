package com.mn.service.billing.models.responses;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.billing.logger.ServiceLogger;
import com.mn.service.billing.validation.ValidationException;

import javax.ws.rs.core.Response;
import java.sql.SQLException;

public class ResponseFactory
{
    public static Response generateResponse(Exception exc)
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            throw exc;
        }
        catch (ValidationException e)
        {
            ServiceLogger.LOGGER.info(e.getMessage());
            JsonResponse response = new JsonResponse(e.getCaseNumber(), e.getMessage());
            Response.Status statusCode = e.getCaseNumber() < 0 ? Response.Status.BAD_REQUEST : Response.Status.OK;
            return Response.status(statusCode).entity(mapper.writeValueAsString(response)).build();
        }
        catch (JsonParseException e)
        {
            ServiceLogger.LOGGER.warning(e.getMessage());
            JsonResponse response = new JsonResponse(-3, "JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(mapper.writeValueAsString(response)).build();
        }
        catch (JsonMappingException e)
        {
            ServiceLogger.LOGGER.warning(e.getMessage());
            JsonResponse response = new JsonResponse(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(mapper.writeValueAsString(response)).build();
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("SQL Err Code: " + e.getErrorCode() + "\n" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (Exception e)
        {
            ServiceLogger.LOGGER.warning(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
