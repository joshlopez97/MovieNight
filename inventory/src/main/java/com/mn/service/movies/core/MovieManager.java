package com.mn.service.movies.core;

import com.mn.service.movies.MovieService;
import com.mn.service.movies.logger.ServiceLogger;
import com.mn.service.movies.models.Genre;
import com.mn.service.movies.models.DetailedMovie;
import com.mn.service.movies.models.Star;
import com.mn.service.movies.models.requests.AddMovieRequest;
import com.mn.service.movies.models.requests.MovieSearchRequest;
import com.mn.service.movies.models.responses.AddMovieResponse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class MovieManager
{
    public static DetailedMovie[] searchForMovies(MovieSearchRequest request, boolean includeHidden, boolean detailed)
            throws SQLException
    {
        String statement = "SELECT * FROM (`movies` LEFT JOIN `ratings` r ON movies.id = r.movieId)";

        ArrayList<String> constraints = new ArrayList<>();
        if (request.getTitle() != null)
            constraints.add("(title REGEXP ? OR title = ?)");
        if (request.getDirector() != null)
            constraints.add("(director LIKE ? OR director = ?)");
        if (request.getGenre() != null)
        {
            statement += " INNER JOIN genres_in_movies gim ON gim.movieId = movies.id INNER JOIN genres g ON gim.genreId = g.id";
            constraints.add("(g.name LIKE ? OR g.name = ?)");
        }
        if (request.getYear() != null)
            constraints.add("year = ?");
        if (request.getHidden() != null)
            constraints.add("hidden = ?");
        if (!constraints.isEmpty())
        {
            statement += " WHERE ";
            statement += String.join(" AND ", constraints);
        }
        statement += " ORDER BY " + request.getSortby() + " " + request.getOrderby() + ", movies.title ASC LIMIT ? OFFSET ?;";

        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);

        int currIndex = 1;
        if (request.getTitle() != null)
        {
            ps.setString(currIndex++, request.getTitle());
            ps.setString(currIndex++, request.getTitle());
        }
        if (request.getDirector() != null)
        {
            ps.setString(currIndex++, "%" + request.getDirector() + "%");
            ps.setString(currIndex++, request.getDirector());
        }
        if (request.getGenre() != null)
        {
            ps.setString(currIndex++, "%" + request.getGenre() + "%");
            ps.setString(currIndex++, request.getGenre());
        }
        if (request.getYear() != null)
            ps.setInt(currIndex++, request.getYear());
        if (request.getHidden() != null)
            ps.setInt(currIndex++, request.getHidden() ? 1 : 0);
        ps.setInt(currIndex++, request.getLimit());
        ps.setInt(currIndex, request.getOffset());

        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");

        LinkedList<DetailedMovie> movies = new LinkedList<>();
        LinkedList<DetailedMovie> detailedMovies = new LinkedList<>();
        int i = 0;
        while (rs.next())
        {
            i++;
            String id = rs.getString("id");
            int numVotes = rs.getInt("numVotes");
            System.out.println(rs.getString("title"));
            DetailedMovie ommittedMovie = new DetailedMovie(
                    id,
                    rs.getString("title"),
                    rs.getString("director"),
                    rs.getInt("year"),
                    null,
                    null,
                    null,
                    null,
                    null,
                    rs.getFloat("rating"),
                    rs.getInt("numVotes"),
                    null,
                    null,
                    includeHidden ? rs.getInt("hidden") == 1 : null
            );
            movies.add(ommittedMovie);
            DetailedMovie dm = new DetailedMovie(
                    id,
                    rs.getString("title"),
                    rs.getString("director"),
                    rs.getInt("year"),
                    rs.getString("backdrop_path"),
                    rs.getInt("budget"),
                    rs.getInt("revenue"),
                    rs.getString("overview"),
                    rs.getString("poster_path"),
                    rs.getFloat("rating"),
                    rs.getInt("numVotes"),
                    null,
                    null,
                    includeHidden ? rs.getInt("hidden") == 1 : null
            );
            detailedMovies.add(dm);
        }
        System.out.println(i + " SQL entries parsed.");

        return detailed? detailedMovies.toArray(new DetailedMovie[0]) : movies.toArray(new DetailedMovie[0]);
    }

    public static int countMovies(MovieSearchRequest request)
            throws SQLException
    {
        String statement = "SELECT COUNT(*) AS movieCount FROM (`movies` LEFT JOIN `ratings` r ON movies.id = r.movieId)";
        ArrayList<String> constraints = new ArrayList<>();
        if (request.getTitle() != null)
            constraints.add("(title REGEXP ? OR title = ?)");

        if (request.getDirector() != null)
            constraints.add("(director LIKE ? OR director = ?)");
        if (request.getGenre() != null)
        {
            statement += " INNER JOIN genres_in_movies gim ON gim.movieId = movies.id INNER JOIN genres g ON gim.genreId = g.id";
            constraints.add("(g.name LIKE ? OR g.name = ?)");
        }
        if (request.getYear() != null)
            constraints.add("year = ?");
        if (request.getHidden() != null)
            constraints.add("hidden = ?");
        if (!constraints.isEmpty())
        {
            statement += " WHERE ";
            statement += String.join(" AND ", constraints);
        }
        statement += " ORDER BY " + request.getSortby() + " " + request.getOrderby() + ", movies.title ASC;";

        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);

        int currIndex = 1;
        if (request.getTitle() != null)
        {
            ps.setString(currIndex++, request.getTitle());
            ps.setString(currIndex++, request.getTitle());
        }
        if (request.getDirector() != null)
        {
            ps.setString(currIndex++, "%" + request.getDirector() + "%");
            ps.setString(currIndex++, request.getDirector());
        }
        if (request.getGenre() != null)
        {
            ps.setString(currIndex++, "%" + request.getGenre() + "%");
            ps.setString(currIndex++, request.getGenre());
        }
        if (request.getYear() != null)
            ps.setInt(currIndex++, request.getYear());
        if (request.getHidden() != null)
            ps.setInt(currIndex++, request.getHidden() ? 1 : 0);

        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ResultSet rs = ps.executeQuery();
        ServiceLogger.LOGGER.info("Query succeeded.");

        if (rs.next())
        {
            return rs.getInt("movieCount");
        }

        return 0;
    }

    public static DetailedMovie getMovie(String id)
            throws SQLException
    {
        String movieStatement = "SELECT * FROM movies LEFT JOIN ratings r on movies.id = r.movieId WHERE id = ?;";
        String genreStatement = "SELECT * FROM genres_in_movies gim INNER JOIN genres g on gim.genreId = g.id WHERE gim.movieId = ?;";
        String starStatement = "SELECT * FROM stars s INNER JOIN stars_in_movies sim on s.id = sim.starId WHERE sim.movieId = ?;";

        PreparedStatement ps = MovieService.getCon().prepareStatement(movieStatement);
        ps.setString(1, id);
        ResultSet firstResultSet = ps.executeQuery();

        DetailedMovie movie = null;

        if (firstResultSet.next())
        {
            movie = new DetailedMovie(
                        id,
                        firstResultSet.getString("title"),
                        firstResultSet.getString("director"), firstResultSet.getInt("year"),
                        firstResultSet.getString("backdrop_path"), firstResultSet.getInt("budget"), firstResultSet.getInt("revenue"), firstResultSet.getString("overview"), firstResultSet.getString("poster_path"), firstResultSet.getFloat("rating"), firstResultSet.getInt("numVotes"), new Genre[]{},
                        new Star[]{},
                        firstResultSet.getInt("hidden") == 1
            );
        }
        if (movie == null)
            return null;

        ps = MovieService.getCon().prepareStatement(genreStatement);
        ps.setString(1, id);
        ResultSet genreResultSet = ps.executeQuery();
        LinkedList<Genre> genres = new LinkedList<>();
        while (genreResultSet.next())
        {
            genres.add(new Genre(genreResultSet.getInt("g.id"), genreResultSet.getString("g.name")));
        }



        ps = MovieService.getCon().prepareStatement(starStatement);
        ps.setString(1, id);
        ResultSet starResultSet = ps.executeQuery();
        LinkedList<Star> stars = new LinkedList<>();
        while (starResultSet.next())
        {
            stars.add(new Star(starResultSet.getString("s.id"), starResultSet.getString("name")));
        }
        movie.setGenres(genres.toArray(new Genre[0]));
        movie.setStars(stars.toArray(new Star[0]));
        return movie;
    }

    private static int getNextMovieId()
            throws SQLException
    {
        String statement = "SELECT COUNT(*) AS numMovies FROM movies WHERE id LIKE 'cs%'";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt("numMovies") + 1;
        return 1;
    }

    public static boolean updateMovie(AddMovieRequest request)
            throws SQLException
    {
        String statement = "UPDATE movies SET backdrop_path = ?, budget = ?, overview = ?, poster_path = ?, revenue = ? WHERE title = ? AND director = ? AND year = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        ps.setString(6, request.getTitle());
        ps.setInt(8, request.getYear());
        ps.setString(7, request.getDirector());
        ps.setString(1, request.getBackdrop_path());
        if (request.getBudget() != null)
            ps.setInt(2, request.getBudget());
        else
            ps.setObject(2, null);
        ps.setString(3, request.getOverview());
        ps.setString(4, request.getPoster_path());
        if (request.getRevenue() != null)
            ps.setInt(5, request.getRevenue());
        else
            ps.setObject(5, null);

        ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
        ;
        ServiceLogger.LOGGER.info("Update succeeded.");
        return ps.executeUpdate() > 0;
    }

    public static AddMovieResponse addMovie(AddMovieRequest request)
            throws SQLException
    {
        String statement = "INSERT INTO movies VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = MovieService.getCon().prepareStatement(statement);
        String movieid = "cs" + String.format("%07d", getNextMovieId());
        ps.setString(1, movieid);
        ps.setString(2, request.getTitle());
        ps.setInt(3, request.getYear());
        ps.setString(4, request.getDirector());
        ps.setString(5, request.getBackdrop_path());
        if (request.getBudget() != null)
            ps.setInt(6, request.getBudget());
        else
            ps.setObject(6, null);
        ps.setString(7, request.getOverview());
        ps.setString(8, request.getPoster_path());
        if (request.getRevenue() != null)
            ps.setInt(9, request.getRevenue());
        else
            ps.setObject(9, null);
        ps.setInt(10, 0);

        ServiceLogger.LOGGER.info("Trying update: " + ps.toString());
        ps.executeUpdate();
        ServiceLogger.LOGGER.info("Update succeeded.");
        Integer[] genresIds = GenreManager.addGenres(request.getGenres(), movieid);
        RatingManager.setRatings(0.0f, 0, movieid);
        return new AddMovieResponse(214, "Movie successfully added.", movieid, genresIds);
    }

    public static boolean deleteMovie(String movieid)
            throws SQLException
    {
        if (!movieExistsWithID(movieid))
            return false;

        String updateMovie = "UPDATE movies SET hidden = 1 WHERE id = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(updateMovie);
        ps.setString(1, movieid);
        ps.executeUpdate();
        return true;
    }

    public static boolean movieExistsWithTitle(String title)
            throws SQLException
    {
        String query = "SELECT * FROM movies WHERE title = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, title);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt("hidden") == 0;
        return false;
    }

    public static String getMovieIdByTitleAndYear(String title, int year)
            throws SQLException
    {
        String query = "SELECT * FROM movies WHERE title = ? AND year = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, title);
        ps.setInt(2, year);
        ResultSet rs = ps.executeQuery();
        if (rs.next() && rs.getInt("hidden") == 0)
        {
            return rs.getString("id");
        }
        return null;
    }

    public static boolean movieExistsWithID(String movieid)
            throws SQLException
    {
        String query = "SELECT * FROM movies WHERE id = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, movieid);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt("hidden") == 0;
        return false;
    }

    public static boolean movieExistsWithIDHiddenIncluded(String movieid)
            throws SQLException
    {
        String query = "SELECT * FROM movies WHERE id = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, movieid);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
