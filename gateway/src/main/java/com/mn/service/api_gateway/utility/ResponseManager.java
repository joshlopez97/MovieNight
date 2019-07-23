package com.mn.service.api_gateway.utility;

import com.mn.service.api_gateway.GatewayService;
import com.mn.service.api_gateway.models.ResponseModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResponseManager
{
    public static void insertResponse(ResponseModel response)
            throws SQLException
    {
        Connection conn = GatewayService.getConPool().requestCon();
        String statement = "INSERT INTO responses VALUES(?, ?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(statement);
        ps.setString(1, response.getTransactionid());
        ps.setString(2, response.getEmail());
        ps.setString(3, response.getSessionid());
        ps.setString(4, response.getResponse());
        ps.setInt(5, response.getHttpstatus());
        ps.executeUpdate();
        GatewayService.getConPool().releaseCon(conn);
    }

    private static void deleteResponse(Connection conn, String transactionID)
            throws SQLException
    {
        String statement = "DELETE FROM responses WHERE transactionid = ?;";
        PreparedStatement ps = conn.prepareStatement(statement);
        ps.setString(1, transactionID);
        ps.executeUpdate();
    }

    public static ResponseModel popResponse(String transactionID)
            throws SQLException
    {
        ResponseModel response = null;
        Connection conn = GatewayService.getConPool().requestCon();
        String statement = "SELECT * FROM responses WHERE transactionid = ?";
        PreparedStatement ps = conn.prepareStatement(statement);
        ps.setString(1, transactionID);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            response = new ResponseModel(
                    rs.getString("transactionid"),
                    rs.getString("email"),
                    rs.getString("sessionid"),
                    rs.getString("response"),
                    rs.getInt("httpstatus")
            );
            deleteResponse(conn, transactionID);
        }
        GatewayService.getConPool().releaseCon(conn);
        return response;
    }
}
