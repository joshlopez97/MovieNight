package com.mn.service.billing.core;

import com.mn.service.billing.BillingService;
import com.mn.service.billing.logger.ServiceLogger;
import com.mn.service.billing.models.carts.CartItem;
import com.mn.service.billing.models.carts.PricedCartItem;
import com.mn.service.billing.models.requests.cart.CartRequest;
import com.mn.service.billing.models.requests.cart.UpdateCartRequest;
import com.mn.service.billing.models.requests.order.PricedCartItems;
import com.mn.service.billing.validation.Validator;
import com.mn.service.billing.models.requests.cart.DeleteCartItemRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class CartManager
{
    public static boolean insertIntoCart(UpdateCartRequest request)
            throws SQLException
    {
        String statement = "INSERT INTO carts VALUES (NULL, ?, ?, ?);";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getEmail());
        ps.setString(2, request.getMovieId());
        ps.setInt(3, request.getQuantity());
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

    public static boolean updateCart(UpdateCartRequest request)
            throws SQLException
    {
        String statement = "UPDATE carts SET quantity = ? WHERE movieId = ? AND email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setInt(1, request.getQuantity());
        ps.setString(2, request.getMovieId());
        ps.setString(3, request.getEmail());
        ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
        int rowsChanged = ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded.");
        return rowsChanged > 0;
    }

    public static boolean deleteCartEntry(DeleteCartItemRequest request)
            throws SQLException
    {
        String statement = "DELETE FROM carts WHERE movieId = ? AND email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getMovieId());
        ps.setString(2, request.getEmail());
        ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
        int rowsChanged = ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded.");
        return rowsChanged > 0;
    }

    public static List<CartItem> retrieveCart(CartRequest request)
            throws SQLException
    {
        String statement = "SELECT * FROM carts WHERE email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getEmail());

        ResultSet rs = ps.executeQuery();
        LinkedList<CartItem> cart = new LinkedList<>();
        while(rs.next())
        {
            cart.add(new CartItem(
                    rs.getString("email"),
                    rs.getString("movieId"),
                    rs.getInt("quantity")
            ));
        }
        return cart;
    }

    public static PricedCartItems retrievePricedCart(CartRequest request)
            throws SQLException
    {
        String statement = "SELECT * FROM carts INNER JOIN movie_prices ON carts.movieId = movie_prices.movieId WHERE email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getEmail());

        ResultSet rs = ps.executeQuery();
        LinkedList<PricedCartItem> cart = new LinkedList<>();
        while(rs.next())
        {
            cart.add(new PricedCartItem(
                    rs.getString("email"),
                    rs.getString("movieId"),
                    rs.getInt("quantity"),
                    rs.getFloat("unit_price"),
                    rs.getFloat("discount")
            ));
        }
        return new PricedCartItems(cart);
    }

    public static void clearCart(CartRequest request)
            throws SQLException
    {
        String statement = "DELETE FROM carts WHERE email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(statement);
        ps.setString(1, request.getEmail());
        ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
        int rowsChanged = ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded. Cleared " + rowsChanged + " items from cart.");
    }
}
