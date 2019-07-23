package com.mn.service.movies.core;

import com.mn.service.movies.MovieService;
import com.mn.service.movies.logger.ServiceLogger;
import com.mn.service.movies.models.Ratings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingManager
{
    public static void setRatings(float rating, int numVotes, String movieId)
            throws SQLException
    {
        String statement = "INSERT INTO ratings VALUES (?, ?, ?);";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setFloat(1, rating);
        ps.setInt(2, numVotes);
        ps.setString(3, movieId);
        ServiceLogger.LOGGER.info("Trying insertion: " + statement);
        ps.executeUpdate();
        ServiceLogger.LOGGER.info("Insertion succeeded.");
    }

    public static Ratings getRatings(String movieId)
            throws SQLException
    {
        String statement = "SELECT * FROM ratings WHERE movieId = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setString(1, movieId);
        ServiceLogger.LOGGER.info("Trying query: " + statement);
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");
        if (rs.next())
            return new Ratings(
                    rs.getFloat("rating"),
                    rs.getInt("numVotes"),
                    rs.getString("movieId")
            );
        return null;
    }

    public static boolean addRating(float rating, String movieId)
            throws SQLException
    {
        if (rating < 0.0 || rating > 10.0)
            return false;
        Ratings currentRatings = getRatings(movieId);
        if (currentRatings == null)
            return false;
        int numVotes = currentRatings.getNumVotes();
        float newRating = ((currentRatings.getRating() * numVotes) + rating) / (float)(numVotes + 1);

        String statement = "UPDATE ratings SET rating = ?, numVotes = ? WHERE movieId = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setFloat(1, newRating);
        ps.setInt(2, numVotes + 1);
        ps.setString(3, movieId);
        ServiceLogger.LOGGER.info("Trying update: " + statement);
        ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded.");
        return true;
    }
}
