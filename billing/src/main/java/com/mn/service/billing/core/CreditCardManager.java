package com.mn.service.billing.core;

import com.mn.service.billing.models.requests.creditcard.CreditCardRequest;
import com.mn.service.billing.models.requests.creditcard.UpdateCreditCardRequest;
import com.mn.service.billing.BillingService;
import com.mn.service.billing.logger.ServiceLogger;
import com.mn.service.billing.models.CreditCard;
import com.mn.service.billing.validation.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class CreditCardManager
{
    public static boolean insertCreditCard(UpdateCreditCardRequest request)
            throws SQLException
    {
        String statement = "INSERT INTO creditcards VALUES (?, ?, ?, ?)";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getId());
        ps.setString(2, request.getFirstName());
        ps.setString(3, request.getLastName());
        ps.setDate(4, request.getExpiration());

        try
        {
            ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
            ps.executeUpdate();
            ServiceLogger.LOGGER.info("Update succeeded.");
            return true;
        }
        catch (SQLException e)
        {
            if (e.getErrorCode() == Validator.DUPLICATE_ENTRY_ERR_CODE)
                return false;
            throw e;
        }
    }

    public static boolean updateCreditCard(UpdateCreditCardRequest request)
            throws SQLException
    {
        String statement = "UPDATE creditcards SET firstName = ?, lastName = ?, expiration = ? WHERE id = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getFirstName());
        ps.setString(2, request.getLastName());
        ps.setDate(3, request.getExpiration());
        ps.setString(4, request.getId());
        ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
        int rowsChanged = ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded.");
        return rowsChanged > 0;
    }

    public static boolean deleteCreditCard(CreditCardRequest request)
            throws SQLException
    {
        String statement = "DELETE FROM creditcards WHERE id = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getId());
        ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
        int rowsChanged = ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded.");
        return rowsChanged > 0;
    }

    public static CreditCard retrieveCreditCard(CreditCardRequest request)
            throws SQLException
    {
        String statement = "SELECT * FROM creditcards WHERE id = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getId());

        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return new CreditCard(
                    rs.getString("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("expiration"))
            );
        }
        return null;
    }

    public static boolean creditCardExists(String ccid)
            throws SQLException
    {
        String statement = "SELECT id FROM creditcards WHERE id = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, ccid);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
