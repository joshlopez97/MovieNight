package com.mn.service.idm.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mn.service.idm.logger.ServiceLogger;
import com.mn.service.idm.validation.ValidationException;
import com.mn.service.idm.IDMService;
import com.mn.service.idm.models.RegisterRequest;
import com.mn.service.idm.security.Crypto;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserManager
{
    public static void insertUser(RegisterRequest registerRequest)
            throws SQLException
    {
        insertUser(registerRequest.getEmail(), registerRequest.getPassword(), "user");
    }

    public static void insertUser(String email, char[] password, String plevel)
            throws SQLException
    {
        byte[] salt = Crypto.genSalt();
        byte[] passwordHash = Crypto.hashPassword(password, salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);

        String statement = "INSERT INTO users(email, status, plevel, salt, pword) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setString(1, email);
        ps.setInt(2, 1);
        ps.setInt(3, convertPlevel(plevel));
        ps.setString(4, Hex.encodeHexString(salt));

        ps.setString(5, Hex.encodeHexString(passwordHash));

        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ps.executeUpdate();
        ServiceLogger.LOGGER.info("Query succeeded.");
    }

    public static boolean userExists(String email)
            throws SQLException
    {
        String statement = "SELECT * FROM `users` WHERE email = ?;";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static boolean authenticateUser(String email, char[] password)
            throws SQLException, ValidationException
    {
        String statement = "SELECT * FROM `users` WHERE email = ?;";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            byte[] decodedSalt;
            try
            {
                String salt = rs.getString("salt");
                decodedSalt = Hex.decodeHex(salt);
            }
            catch (DecoderException e)
            {
                ServiceLogger.LOGGER.config("Unable to decode salt for user with email " + email);
                return false;
            }
            try
            {
                byte[] passwordHash = Crypto.hashPassword(
                        password,
                        decodedSalt,
                        Crypto.ITERATIONS,
                        Crypto.KEY_LENGTH
                );
                byte[] decodedPassword = Hex.decodeHex(rs.getString("pword"));
                if (!Arrays.equals(passwordHash, decodedPassword))
                    throw new ValidationException("Passwords do not match.", 11);
                return true;
            }
            catch (DecoderException e)
            {
                ServiceLogger.LOGGER.config("Unable to decode password for user with email " + email);
                return false;
            }
        }
        else
            throw new ValidationException("User not found.", 14);
    }

    public static int getUserPlevel(String email)
            throws SQLException
    {
        String statement = "SELECT plevel FROM `users` WHERE email = ?;";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getInt("plevel");
        }
        return -1;
    }

    public static void updatePassword(String email, char[] newPword)
            throws SQLException
    {
        byte[] salt = Crypto.genSalt();
        byte[] passwordHash = Crypto.hashPassword(newPword, salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);
        String statement = "UPDATE `users` SET pword = ?, salt = ? WHERE email = ?;";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setString(1, Hex.encodeHexString(passwordHash));
        ps.setString(2, Hex.encodeHexString(salt)); // status is ACTIVE
        ps.setString(3, email); // plevel is USER

        ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
        ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded.");
    }

    public static ArrayNode queryUsers(Integer id, String email, String plevel)
            throws SQLException
    {
        String statement = "SELECT id, email, plevel FROM `users`";
        if (id != null || email != null || plevel != null)
        {
            statement += " WHERE ";
            ArrayList<String> constraints = new ArrayList<>();
            if (id != null)
                constraints.add("id = ?");
            if (email != null)
                constraints.add("email = ?");
            if (plevel != null)
                constraints.add("plevel = ?");
            statement += String.join(" AND ", constraints);
        }
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);

        int lastIndex = 1;
        if (id != null)
            ps.setInt(lastIndex++, id);
        if (email != null)
            ps.setString(lastIndex++, email);
        if (plevel != null)
            ps.setInt(lastIndex, convertPlevel(plevel));

        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode results = mapper.createArrayNode();
        while (rs.next())
        {
            ObjectNode userNode = mapper.createObjectNode();
            userNode.put("id", rs.getInt("id"));
            userNode.put("email", rs.getString("email"));
            userNode.put("plevel", rs.getString("plevel"));
            results.add(userNode);
        }
        ServiceLogger.LOGGER.info(results.size() + " user results found.");
        return results;
    }

    private static int convertPlevel(String plevel)
    {
        plevel = plevel.toLowerCase();
        if (plevel.equals("root"))
            return 1;
        if (plevel.equals("admin"))
            return 2;
        if (plevel.equals("employee"))
            return 3;
        if (plevel.equals("service"))
            return 4;
        if (plevel.equals("user"))
            return 5;
        ServiceLogger.LOGGER.warning("Plevel " + plevel + " is invalid.");
        return -1;
    }
}
