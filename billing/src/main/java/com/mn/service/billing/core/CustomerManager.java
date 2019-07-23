package com.mn.service.billing.core;

import com.mn.service.billing.BillingService;
import com.mn.service.billing.logger.ServiceLogger;
import com.mn.service.billing.models.Customer;
import com.mn.service.billing.models.requests.customer.CustomerRequest;
import com.mn.service.billing.models.requests.customer.UpdateCustomerRequest;
import com.mn.service.billing.validation.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerManager
{
    public static boolean insertCustomer(UpdateCustomerRequest request)
            throws SQLException
    {
        String statement = "INSERT INTO customers VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getEmail());
        ps.setString(2, request.getFirstName());
        ps.setString(3, request.getLastName());
        ps.setString(4, request.getCcId());
        ps.setString(5, request.getAddress());

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

    public static boolean updateCustomer(UpdateCustomerRequest request)
            throws SQLException
    {
        String statement = "UPDATE customers SET firstName = ?, lastName = ?, ccId = ?, address = ? WHERE email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getFirstName());
        ps.setString(2, request.getLastName());
        ps.setString(3, request.getCcId());
        ps.setString(4, request.getAddress());
        ps.setString(5, request.getEmail());

        ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
        int rowsChanged = ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded.");
        return rowsChanged > 0;
    }

    public static Customer retrieveCustomer(CustomerRequest request)
            throws SQLException
    {
        String statement = "SELECT * FROM customers WHERE email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getEmail());

        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return new Customer(
                    rs.getString("email"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("ccId"),
                    rs.getString("address")
            );
        }
        return null;
    }

    public static boolean customerExists(String email)
            throws SQLException
    {
        String statement = "SELECT email FROM customers WHERE email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
