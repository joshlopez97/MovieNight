package com.mn.service.api_gateway.validation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.api_gateway.GatewayService;
import com.mn.service.api_gateway.logger.ServiceLogger;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.lang.reflect.Constructor;

import static com.mn.service.api_gateway.validation.HTTPStatusCodes.*;

public class ModelValidator {
    public static <T> T verifyModel(String jsonText, Class<T> modelType) throws ModelValidationException {
        ServiceLogger.LOGGER.info("Verifying model format " + modelType.getSimpleName());
        ObjectMapper mapper = new ObjectMapper();
        String warning = "";
        T model;

        try {
            model = mapper.readValue(jsonText, modelType);
        } catch (JsonMappingException e) {
            warning = "Unable to map JSON to POJO--request has invalid format.";
            ServiceLogger.LOGGER.warning(GatewayService.ANSI_RED + warning + "\n" + ExceptionUtils.exceptionStackTraceAsString(e) + GatewayService.ANSI_RESET);
            throw new ModelValidationException(warning, e);
        } catch (JsonParseException e) {
            warning = "Unable to parse JSON--text is not in valid JSON format.";
            ServiceLogger.LOGGER.warning(GatewayService.ANSI_RED + warning + "\n" + ExceptionUtils.exceptionStackTraceAsString(e) + GatewayService.ANSI_RESET);
            throw new ModelValidationException(warning, e);
        } catch (IOException e) {
            warning = "IOException while mapping JSON to POJO.";
            ServiceLogger.LOGGER.warning(GatewayService.ANSI_RED + warning + "\n" + ExceptionUtils.exceptionStackTraceAsString(e) + GatewayService.ANSI_RESET);
            throw new ModelValidationException(warning, e);
        }
        ServiceLogger.LOGGER.info("Model has been validated.");
        return model;
    }

    public static Response returnInvalidRequest(ModelValidationException e, Class modelType) {
        try {
            Class<?> model = Class.forName(modelType.getName());
            Constructor<?> constructor;
            constructor = model.getConstructor(Integer.TYPE);
            Object object = null;
            int resultCode;

            if (e.getCause() instanceof JsonMappingException) {
                object = constructor.newInstance(ResultCodes.JSON_MAPPING_EXCEPTION);
                resultCode = ResultCodes.JSON_MAPPING_EXCEPTION;
            } else if (e.getCause() instanceof JsonParseException) {
                object = constructor.newInstance(ResultCodes.JSON_PARSE_EXCEPTION);
                resultCode = ResultCodes.JSON_PARSE_EXCEPTION;
            } else {
                object = constructor.newInstance(ResultCodes.INTERNAL_SERVER_ERROR);
                resultCode = ResultCodes.INTERNAL_SERVER_ERROR;
            }
            return Response.status(setHTTPStatus(resultCode)).entity(object).build();
        } catch (Exception ex) {
            ServiceLogger.LOGGER.warning("Unable to create ResponseModel " + modelType.getName());
            ServiceLogger.LOGGER.warning(GatewayService.ANSI_RED + ExceptionUtils.exceptionStackTraceAsString(e) + GatewayService.ANSI_RESET);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

