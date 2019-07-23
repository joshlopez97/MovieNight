package com.mn.service.billing.core;

import com.mn.service.billing.BillingService;
import com.mn.service.billing.logger.ServiceLogger;
import com.mn.service.billing.models.carts.CartItem;
import com.mn.service.billing.models.requests.order.PricedCartItems;
import com.mn.service.billing.models.transactions.ItemModel;
import com.mn.service.billing.models.transactions.TransactionModel;
import com.mn.service.billing.models.requests.order.OrderRequest;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderManager
{
    public static void placeOrder(PricedCartItems cartItems, String token)
            throws SQLException
    {
        CallableStatement cStmt = BillingService.getCon().prepareCall("{call insert_sales_transactions(?, ?, ?, ?, ?)}");
        for (CartItem cartItem : cartItems.getItems())
        {
            cStmt.setString(1, cartItem.getEmail());
            cStmt.setString(2, cartItem.getMovieId());
            cStmt.setInt(3, cartItem.getQuantity());
            cStmt.setDate(4, new Date(Calendar.getInstance().getTimeInMillis()));
            cStmt.setString(5, token);
            ServiceLogger.LOGGER.info("Adding batch: " + cStmt.toString());
            cStmt.addBatch();
        }
        ServiceLogger.LOGGER.info("Executing batch update.");
        cStmt.executeBatch();
        ServiceLogger.LOGGER.info("Update succeeded.");
    }

    public static TransactionModel[] retrieveOrders(OrderRequest request)
            throws SQLException
    {
        String statement = "SELECT * FROM sales INNER JOIN movie_prices ON sales.movieId = movie_prices.movieId WHERE email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getEmail());

        ServiceLogger.LOGGER.info("Executing query: " + ps.toString());
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");

        Map<Integer, ItemModel> sales = new HashMap<>();
        while(rs.next())
        {
            sales.put(
                    rs.getInt("id"),
                    new ItemModel(
                            rs.getString("email"),
                            rs.getString("movieId"),
                            rs.getInt("quantity"),
                            new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("saleDate")),
                            rs.getFloat("unit_price"),
                            rs.getFloat("discount")
            ));
        }
        return TransactionManager.getTransactionsForSales(sales);
    }
}
