package com.mn.service.idm.core;

import com.mn.service.idm.logger.ServiceLogger;
import com.mn.service.idm.IDMService;
import com.mn.service.idm.security.Session;
import com.mn.service.idm.security.Token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class SessionManager
{
    public static Session createSession(String email)
            throws SQLException
    {
        Session newSession = Session.createSession(email);

        String statement = "INSERT INTO `sessions` VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setString(1, email);
        ps.setString(2, newSession.getSessionID().toString());
        ps.setInt(3, 1);
        ps.setTimestamp(4, newSession.getTimeCreated());
        ps.setTimestamp(5, newSession.getLastUsed());
        ps.setTimestamp(6, newSession.getExprTime());
        ServiceLogger.LOGGER.info("Creating session " + newSession.getSessionID().toString() + "for email " + newSession.getEmail());
        ps.executeUpdate();

        return newSession;
    }

    public static Session getActiveSession(String email)
            throws SQLException
    {
        ServiceLogger.LOGGER.info("Looking for existing active session for " + email);
        String statement = "SELECT * FROM `sessions` WHERE email = ? AND status = ?;";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setString(1, email);
        ps.setInt(2, Session.ACTIVE);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            ServiceLogger.LOGGER.info("Found existing session " + rs.getString("sessionID"));
            return verifySessionStatus(email, Session.rebuildSession(
                    rs.getString("email"),
                    Token.rebuildToken(rs.getString("sessionID")),
                    rs.getTimestamp("timeCreated"),
                    rs.getTimestamp("lastUsed"),
                    rs.getTimestamp("exprTime"),
                    rs.getInt("status")
            ));
        }
        ServiceLogger.LOGGER.info("Could not find existing active session.");
        return null;
    }

    private static Session expireSession(Session session)
            throws SQLException
    {
        String statement = "UPDATE `sessions` s SET s.status = ?, s.lastUsed = ? WHERE email = ? AND sessionID = ?;";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setInt(1, Session.EXPIRED);
        ps.setTimestamp(2, new Timestamp(new Date().getTime()));
        ps.setString(3, session.getEmail());
        ps.setString(4, session.getSessionID().toString());

        ServiceLogger.LOGGER.info("Expiring session " + session.getSessionID().toString() + "for email " + session.getEmail());
        ps.executeUpdate();
        session.setStatus(Session.EXPIRED);
        return session;
    }

    private static Session revokeSession(Session session)
            throws SQLException
    {
        String statement = "UPDATE `sessions` s SET s.status = ?, s.lastUsed = ? WHERE email = ? AND sessionID = ?;";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setInt(1, Session.REVOKED);
        ps.setTimestamp(2, new Timestamp(new Date().getTime()));
        ps.setString(3, session.getEmail());
        ps.setString(4, session.getSessionID().toString());

        ServiceLogger.LOGGER.info("Revoking session " + session.getSessionID().toString() + "for email " + session.getEmail());
        ps.executeUpdate();
        session.setStatus(Session.REVOKED);
        return session;
    }

    private static Session updateSession(Session session)
            throws SQLException
    {
        String statement = "UPDATE `sessions` s SET s.lastUsed = ? WHERE email = ? AND sessionID = ?;";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setTimestamp(1, new Timestamp(new Date().getTime()));
        ps.setString(2, session.getEmail());
        ps.setString(3, session.getSessionID().toString());

        ServiceLogger.LOGGER.info("Updating session " + session.getSessionID().toString() + "for email " + session.getEmail());
        ps.executeUpdate();
        session.renew();
        return session;
    }

    public static Session getSession(String email, String sessionID)
            throws SQLException
    {
        ServiceLogger.LOGGER.info("Looking for session " + sessionID + " with email " + email);
        String statement = "SELECT * FROM `sessions` WHERE email = ? AND sessionID = ?;";
        PreparedStatement ps = IDMService.getCon().prepareStatement(statement);
        ps.setString(1, email);
        ps.setString(2, sessionID);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            Session session = Session.rebuildSession(
                    rs.getString("email"),
                    Token.rebuildToken(rs.getString("sessionID")),
                    rs.getTimestamp("timeCreated"),
                    rs.getTimestamp("lastUsed"),
                    rs.getTimestamp("exprTime"),
                    rs.getInt("status")
            );
            ServiceLogger.LOGGER.info("Session status: " + session.getStatusString());
            if (session.getStatus() == Session.ACTIVE)
            {
                return verifySessionStatus(email, session);
            }
            return session;
        }
        ServiceLogger.LOGGER.config("Session " + sessionID + " not found.");
        return null;
    }

    private static Session verifySessionStatus(String email, Session session)
            throws SQLException
    {
        ServiceLogger.LOGGER.info("Verifying session " + session.getSessionID().toString());
        Timestamp now = new Timestamp(new Date().getTime());
        ServiceLogger.LOGGER.info(
                "\nnow = " + now.getTime()
                        + "\nexpire = " + session.getExprTime().getTime() + ", now > expire? " + (now.getTime() > session.getExprTime().getTime())
                        + "\nlastused = " + session.getLastUsed().getTime()
                        + "\nnow - lastused = " + (now.getTime() - session.getLastUsed().getTime())
                        + "\ntimeout = " + Session.SESSION_TIMEOUT
        );
//        if (now.getTime() > session.getExprTime().getTime())
//        {
//            return expireSession(session);
//        }
//        if (now.getTime() - session.getLastUsed().getTime() > Session.SESSION_TIMEOUT)
//        {
//            return revokeSession(session);
//        }
//        if (now.getTime() - session.getLastUsed().getTime() < Session.SESSION_TIMEOUT)
//        {
//            revokeSession(session);
//            return createSession(email);
//        }
        return updateSession(session);
    }

    private static int convertStatus(String status)
    {
        status = status.toLowerCase();
        if (status.equals("active"))
            return 1;
        if (status.equals("closed"))
            return 2;
        if (status.equals("expired"))
            return 3;
        if (status.equals("revoked"))
            return 4;
        return -1;
    }
}
