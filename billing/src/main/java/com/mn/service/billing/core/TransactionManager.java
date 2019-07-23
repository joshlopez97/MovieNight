package com.mn.service.billing.core;

import com.mn.service.billing.BillingService;
import com.mn.service.billing.logger.ServiceLogger;
import com.mn.service.billing.models.transactions.ItemModel;
import com.mn.service.billing.models.transactions.TransactionModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransactionManager
{
    public static boolean setTransactionId(String transactionId, String token)
            throws SQLException
    {
        String statement = "UPDATE transactions SET transactionId = ? WHERE token = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, transactionId);
        ps.setString(2, token);

        ServiceLogger.LOGGER.info("Executing update: " + ps.toString());
        int rowsUpdated = ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded.");
        return rowsUpdated > 0;
    }

    public static TransactionModel[] getTransactionsForSales(Map<Integer, ItemModel> sales)
            throws SQLException
    {
        Integer[] saleIDs = sales.keySet().toArray(new Integer[0]);
        StringBuilder statementBuilder = new StringBuilder("SELECT * FROM transactions WHERE sId IN (?");
        for (int i = 1; i < saleIDs.length; i++)
            statementBuilder.append(", ?");
        statementBuilder.append(");");

        String statement = statementBuilder.toString();
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        for (int i = 0; i < sales.size(); i++)
            ps.setInt(i + 1, saleIDs[i]);

        ServiceLogger.LOGGER.info("Executing query: " + ps.toString());
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");

        Map<String, List<ItemModel>> transactionSales = new HashMap<>();
        Map<String, TransactionModel> transactions = new HashMap<>();
        while (rs.next())
        {
            Integer saleId = rs.getInt("sId");
            String transactionId = rs.getString("transactionId");
            if (transactionId == null)
                continue;

            transactionSales.putIfAbsent(transactionId, new LinkedList<>());
            transactionSales.get(transactionId).add(sales.get(saleId));
            transactions.put(transactionId, PayPalClient.getTransactionDetails(transactionId));
        }

        for (String transactionId : transactions.keySet())
        {
            transactions.get(transactionId).setItems(
                    transactionSales.get(transactionId).toArray(new ItemModel[0])
            );
        }
        return transactions.values().toArray(new TransactionModel[0]);
    }
}
