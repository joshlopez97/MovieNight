package com.mn.service.movies.core;

import com.mn.service.movies.MovieService;
import com.mn.service.movies.logger.ServiceLogger;
import com.mn.service.movies.models.DetailedMovie;
import com.mn.service.movies.models.DetailedStar;
import com.mn.service.movies.models.Movie;
import com.mn.service.movies.models.Star;
import com.mn.service.movies.models.*;
import com.mn.service.movies.models.requests.StarSearchRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StarManager
{
    public static DetailedStar[] searchForStars(StarSearchRequest request, boolean includeHidden)
            throws SQLException
    {
        String statement = "SELECT * FROM `stars` INNER JOIN (stars_in_movies sim INNER JOIN movies m on sim.movieId = m.id) ON sim.starId = stars.id";

        ArrayList<String> constraints = new ArrayList<>();
        if (request.getName() != null)
            constraints.add("name LIKE ?");
        if (request.getMovieTitle() != null)
            constraints.add("m.title LIKE ?");
        if (request.getBirthYear() != null)
            constraints.add("birthYear = ?");
        if (!constraints.isEmpty())
        {
            statement += " WHERE ";
            statement += String.join(" AND ", constraints);
        }
        statement += " GROUP BY stars.id ORDER BY " + request.getSortby() + " " + request.getOrderby()+", birthYear ASC LIMIT ? OFFSET ? ";

        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);

        int currIndex = 1;
        if (request.getName() != null)
            ps.setString(currIndex++, "%" + request.getName() + "%");
        if (request.getBirthYear() != null)
            ps.setInt(currIndex++, request.getBirthYear());
        if (request.getMovieTitle() != null)
            ps.setString(currIndex++, "%" + request.getMovieTitle() + "%");
        ps.setInt(currIndex++, request.getLimit());
        ps.setInt(currIndex, request.getOffset());

        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");

        LinkedHashMap<String, DetailedStar> stars = new LinkedHashMap<>();
        while (rs.next())
        {
            String id = rs.getString("id");
            stars.putIfAbsent(id, new DetailedStar(
                    id,
                    rs.getString("name"),
                    rs.getInt("birthYear"),
                    null
            ));
        }
        ServiceLogger.LOGGER.info(stars.values().size() + " stars found!");

        return stars.values().toArray(new DetailedStar[0]);
    }

    public static DetailedStar getStar(String id)
            throws SQLException
    {
        String statement = "SELECT * FROM stars " +
                "INNER JOIN stars_in_movies sim on stars.id = sim.starId " +
                "INNER JOIN movies m on sim.movieId = m.id " +
                "WHERE stars.id = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();

        DetailedStar star = null;
        ArrayList<Movie> movies = new ArrayList<>();
        while (rs.next())
        {
            if (star == null)
            {
                star = new DetailedStar(
                        id,
                        rs.getString("name"),
                        rs.getInt("birthYear"),
                        null
                );
            }
            movies.add(new Movie(rs.getString("m.id"), rs.getString("m.title")));
        }
        return star;
    }

    public static LinkedHashMap<String, ArrayList<Star>> getStarsForMovies(LinkedHashMap<String, DetailedMovie> movies)
            throws SQLException
    {
        LinkedHashMap<String, ArrayList<Star>> stars = new LinkedHashMap<>();

        if (!movies.isEmpty())
        {
            StringBuilder movieIdSet = new StringBuilder("(?");
            for (int i = 1; i < movies.size(); ++i)
            {
                movieIdSet.append(", ?");
            }
            movieIdSet.append(")");


            String starStatement = "SELECT * FROM stars_in_movies sm " +
                    "INNER JOIN stars s on sm.starId = s.id " +
                    "WHERE sm.movieId IN " + movieIdSet + ";";
            PreparedStatement ps = MovieService.getCon().prepareStatement(starStatement);

            int currentIndex = 1;
            for (String mid : movies.keySet())
            {
                ps.setString(currentIndex++, mid);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                String movieId = rs.getString("sm.movieId");
                if (!stars.containsKey(movieId))
                {
                    stars.put(movieId, new ArrayList<>());
                }
                stars.get(movieId).add(new Star(rs.getString("s.id"), rs.getString("s.name")));
            }
        }
        return stars;
    }

    private static int getNextStarId()
            throws SQLException
    {
        String statement = "SELECT COUNT(*) AS numStars FROM stars WHERE id LIKE 'ss%';";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt("numStars") + 1;
        return 1;
    }

    public static boolean starExistsWithName(String name, Integer birthYear)
            throws SQLException
    {
        String statement = "SELECT * FROM stars WHERE name = ? AND birthYear " +
        (birthYear == null ? "IS NULL" : "= ?") +
                ";";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setString(1, name);
        if (birthYear != null)
            ps.setInt(2, birthYear);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static String getStarIdByName(String name, Integer birthYear)
            throws SQLException
    {
        String statement = "SELECT id FROM stars WHERE name = ? AND birthYear " +
                (birthYear == null ? "IS NULL" : "= ?") +
                ";";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setString(1, name);
        if (birthYear != null)
            ps.setInt(2, birthYear);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getString("id");
        return null;
    }

    public static boolean starExistsInMovie(String starId, String movieId)
            throws SQLException
    {
        String statement = "SELECT * FROM stars_in_movies WHERE starId = ? AND movieId = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setString(1, starId);
        ps.setString(2, movieId);
        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");
        return rs.next();
    }

    public static String addStar(String name, Integer birthYear)
            throws SQLException
    {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String starId = "ss" + String.format("%07d", getNextStarId());

        String statement = "INSERT INTO stars VALUES (?, ?, " +
                (birthYear > currentYear ? "NULL" : "?") +
                ");";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setString(1, starId);
        ps.setString(2, name);
        if (birthYear <= currentYear)
            ps.setInt(3, birthYear);
        ServiceLogger.LOGGER.info("Trying insertion: " + ps.toString());
        ps.executeUpdate();
        ServiceLogger.LOGGER.info("Insertion succeeded.");
        return starId;
    }

    public static DetailedMovie[] getMoviesForStar(String starid)
            throws SQLException
    {
        String statement = "SELECT * FROM stars_in_movies sim INNER JOIN movies m on sim.movieId = m.id WHERE starId = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setString(1, starid);
        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");
        LinkedList<DetailedMovie> movies = new LinkedList<>();
        while (rs.next())
        {
            movies.add(
                    new DetailedMovie(
                            rs.getString("sim.movieId"),
                            rs.getString("title"),
                            rs.getString("director"),
                            rs.getInt("year"),
                            rs.getString("backdrop_path"),
                            rs.getInt("budget"),
                            rs.getInt("revenue"),
                            rs.getString("overview"),
                            rs.getString("poster_path"),
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            );
        }
        return movies.toArray(new DetailedMovie[0]);
    }

    public static void addStarInMovie(String starId, String movieId)
            throws SQLException
    {
        String statement = "INSERT INTO stars_in_movies VALUES (?, ?);";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setString(1, starId);
        ps.setString(2, movieId);
        ServiceLogger.LOGGER.info("Trying insertion: " + ps.toString());
        ps.executeUpdate();
        ServiceLogger.LOGGER.info("Insertion succeeded.");
    }
}
