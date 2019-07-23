package com.mn.service.movies.core;

import com.mn.service.movies.MovieService;
import com.mn.service.movies.logger.ServiceLogger;
import com.mn.service.movies.models.DetailedMovie;
import com.mn.service.movies.models.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GenreManager
{
    public static Integer[] addGenres(Genre[] genres, String movieId)
            throws SQLException
    {
        HashMap<String, Integer> existingGenres = getExistingGenres(genres);
        Genre[] existingGenresInMovie = getGenresForMovie(movieId);
        Integer[] genreIds = new Integer[genres.length];
        String statement = "INSERT INTO genres_in_movies VALUES (?, ?);";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        for (int i = 0; i < genres.length; i++)
        {
            String genreName = genres[i].getName().toLowerCase();
            Integer genreId = genres[i].getId();
            if (!existingGenres.containsKey(genreName))
            {
                genreId = addGenre(genreName);
            }
            else if (!existingGenres.get(genreName).equals(genreId))
            {
                ServiceLogger.LOGGER.info("Provided genre ID " + genreId + " for genre '" + genreName + "' is invalid. " +
                        "Changing to " + existingGenres.get(genreName)
                        );
                genreId = existingGenres.get(genreName);
            }
            boolean addGenreToMovie = true;
            for (Genre g : existingGenresInMovie)
            {
                if (g.getId() == genreId)
                {
                    addGenreToMovie = false;
                    break;
                }
            }
            if (addGenreToMovie)
            {
                ps.setInt(1, genreId);
                ps.setString(2, movieId);
                ServiceLogger.LOGGER.info("Adding update to batch: " + ps.toString());
                ps.addBatch();
                ServiceLogger.LOGGER.info("Update succeeded.");
            }


            genreIds[i] = genreId;
        }
        ps.executeBatch();
        return genreIds;
    }

    public static Integer addGenre(String genreName)
            throws SQLException
    {
        String statement = "INSERT INTO genres VALUES (?, ?);";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        int newGenreId = getNextGenreId();
        ps.setInt(1, newGenreId);
        ps.setString(2, genreName);
        ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
        ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded.");
        return newGenreId;
    }

    private static int getNextGenreId()
            throws SQLException
    {
        String statement = "SELECT MAX(id) AS maxId FROM genres;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt("maxId") + 1;
        return 1;
    }

    private static HashMap<String, Integer> getExistingGenres(Genre[] genres)
            throws SQLException
    {
        if (genres.length == 0)
            return new HashMap<>();
        String statement = "SELECT * FROM genres WHERE name IN (";
        String[] params = new String[genres.length];
        Arrays.fill(params, "?");
        statement += String.join(",", params) + ");";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        for (int i = 0; i < genres.length; i++)
        {
            ps.setString(i + 1, genres[i].getName());
        }
        ResultSet rs = ps.executeQuery();

        HashMap<String, Integer> existingGenres = new HashMap<>();
        while (rs.next())
        {
            existingGenres.put(
                    rs.getString("name").toLowerCase(),
                    rs.getInt("id")
            );
        }
        return existingGenres;
    }

    public static LinkedHashMap<String, ArrayList<Genre>> getGenresForMovies(LinkedHashMap<String, DetailedMovie> movies)
            throws SQLException
    {
        LinkedHashMap<String, ArrayList<Genre>> genres = new LinkedHashMap<>();

        if (!movies.isEmpty())
        {
            StringBuilder movieIdSet = new StringBuilder("(?");
            for (int i = 1; i < movies.size(); ++i)
            {
                movieIdSet.append(", ?");
            }
            movieIdSet.append(")");


            String statement = "SELECT * FROM genres_in_movies gm " +
                    "INNER JOIN genres g on gm.genreId = g.id " +
                    "WHERE gm.movieId IN " + movieIdSet + ";";
            PreparedStatement ps = MovieService.getCon().prepareStatement(statement);

            int currentIndex = 1;
            for (String mid : movies.keySet())
            {
                ps.setString(currentIndex++, mid);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                String movieId = rs.getString("gm.movieId");
                if (!genres.containsKey(movieId))
                {
                    genres.put(movieId, new ArrayList<>());
                }
                genres.get(movieId).add(new Genre(rs.getInt("g.id"), rs.getString("g.name")));
            }
        }
        return genres;
    }

    public static Genre[] getGenresForMovie(String movieId)
            throws SQLException
    {
        List<Genre> genres = new LinkedList<>();

        String statement = "SELECT * FROM genres_in_movies gm " +
                "INNER JOIN genres g on gm.genreId = g.id " +
                "WHERE gm.movieId = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setString(1, movieId);

        ServiceLogger.LOGGER.info("Trying query: " + statement);
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");

        while (rs.next())
        {
            genres.add(
                    new Genre(
                            rs.getInt("id"),
                            rs.getString("name")
                    )
            );
        }
        return genres.toArray(new Genre[0]);
    }

    public static Genre[] getAllGenres()
            throws SQLException
    {
        List<Genre> genres = new LinkedList<>();

        String statement = "SELECT * FROM genres;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);

        ServiceLogger.LOGGER.info("Trying query: " + statement);
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");

        while (rs.next())
        {
            genres.add(
                    new Genre(
                            rs.getInt("id"),
                            rs.getString("name")
                    )
            );
        }
        return genres.toArray(new Genre[0]);
    }

    public static boolean genreExists(String name)
            throws SQLException
    {
        String query = "SELECT * FROM genres WHERE name = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
