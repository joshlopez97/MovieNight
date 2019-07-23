package com.mn.service.api_gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mn.service.api_gateway.GatewayService;

import javax.ws.rs.core.Response;

public class NoContentResponseModel {
    private String message;
    private int delay;
    private String transactionID;

    public NoContentResponseModel(String transactionID) {
        this.message = "Request received.";
        this.delay = GatewayService.getGatewayConfigs().getRequestDelay();
        this.transactionID = transactionID;
    }

    public static Response createResponse(String transactionID, String sessionID)
    {
        return Response.status(Response.Status.NO_CONTENT)
                .header("message", "Request received.")
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("transactionid", transactionID)
                .header("sessionid", sessionID)
                .build();
    }

    public static Response createResponse(String transactionID)
    {
        return Response.status(Response.Status.NO_CONTENT)
                .header("message", "Request received.")
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("transactionid", transactionID)
                .build();
    }

    @JsonProperty(value = "message", required = true)
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "delay", required = true)
    public int getDelay() {
        return delay;
    }

    @JsonProperty(value = "transactionID", required = true)
    public String getTransactionID() {
        return transactionID;
    }
}
