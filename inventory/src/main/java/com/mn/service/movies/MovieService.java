package com.mn.service.movies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mn.service.movies.logger.ServiceLogger;
import com.mn.service.movies.configs.ConfigsModel;
import com.mn.service.movies.configs.MovieConfigs;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.internal.util.ExceptionUtils;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieService {
    public static MovieService movieService;
    private static Connection con = null;
    private static MovieConfigs movieConfigs = new MovieConfigs();

    public static void main(String[] args)
    {
        movieService = new MovieService();
        movieService.initService(args);
    }

    private void initService(String[] args)
    {
        // Validate arguments
        validateArguments(args);
        // Exec the arguments
        execArguments(args);
        // Initialize logging
        initLogging();
        ServiceLogger.LOGGER.config("Starting service...");
        movieConfigs.currentConfigs();
        // Connect to database
        connectToDatabase();
        createRequiredTables();
        // Initialize HTTP server
        initHTTPServer();

        if (con != null) {
            ServiceLogger.LOGGER.config("Service initialized.");
        } else {
            ServiceLogger.LOGGER.config("Service initialized with error(s).");
        }
    }

    private void validateArguments(String[] args) {
        boolean isConfigOptionSet = false;
        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "--default":
                case "-d":
                    if (i + 1 < args.length) {
                        exitAppFailure("Invalid arg after " + args[i] + " option: " + args[i + 1]);
                    }
                case "--config":
                case "-c":
                    if (!isConfigOptionSet) {
                        isConfigOptionSet = true;
                        ++i;
                    } else {
                        exitAppFailure("Conflicting configuration file arguments.");
                    }
                    break;

                default:
                    exitAppFailure("Unrecognized argument: " + args[i]);
            }
        }
    }

    private void execArguments(String[] args)
    {
        if (args.length > 0)
        {
            for (int i = 0; i < args.length; ++i)
            {
                switch (args[i])
                {
                    case "--config":
                    case "-c":
                        // Config file specified. Load it.
                        getConfigFile(args[i + 1]);
                        ++i;
                        break;
                    default:
                        exitAppFailure("Unrecognized argument: " + args[i]);
                }
            }
        }
        else
        {
            String DEFAULT_CONFIG_FILE_PATH = "config.yaml";
            getConfigFile(DEFAULT_CONFIG_FILE_PATH);
        }
    }

    private void getConfigFile(String configFile) {
        try {
            System.err.println("Config file name: " + configFile);
            movieConfigs = new MovieConfigs(loadConfigs(configFile));
            System.err.println("Configuration file successfully loaded.");
        } catch (NullPointerException e) {
            System.err.println("Config file not found. Using default values.");
            movieConfigs = new MovieConfigs();
        }
    }

    private ConfigsModel loadConfigs(String file) {
        System.err.println("Loading configuration file...");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ConfigsModel configs = null;

        try {
            configs = mapper.readValue(new File(file), ConfigsModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            exitAppFailure("Unable to load configuration file.");
        }
        return configs;
    }

    private void initLogging() {
        try {
            ServiceLogger.initLogger(movieConfigs.getOutputDir(), movieConfigs.getOutputFile());
        } catch (IOException e) {
            exitAppFailure("Unable to initialize logging.");
        }
    }

    private void connectToDatabase() {
        ServiceLogger.LOGGER.config("Connecting to database...");
        String driver = "";

        if (!movieConfigs.isDbConfigValid()) {
            ServiceLogger.LOGGER.config("Database configurations not valid. Cannot connect to database.");
            return;
        }

        try {
            Class.forName(movieConfigs.getDbDriver());
            ServiceLogger.LOGGER.config("Database URL: " + movieConfigs.getDbUrl());
            con = DriverManager.getConnection(movieConfigs.getDbUrl(), movieConfigs.getDbUsername(), movieConfigs.getDbPassword());
            ServiceLogger.LOGGER.config("Connected to database: " + movieConfigs.getDbUrl());
        } catch (ClassNotFoundException | SQLException | NullPointerException e) {
            ServiceLogger.LOGGER.severe("Unable to connect to database.\n" + ExceptionUtils.exceptionStackTraceAsString(e));
        }
    }

    private void initHTTPServer() {
        ServiceLogger.LOGGER.config("Initializing HTTP server...");
        String scheme = movieConfigs.getScheme();
        String hostName = movieConfigs.getHostName();
        int port = movieConfigs.getPort();
        String path = movieConfigs.getPath();

        try {
            ServiceLogger.LOGGER.config("Building URI from movieConfigs...");
            URI uri = UriBuilder.fromUri(scheme + hostName + path).port(port).build();
            ServiceLogger.LOGGER.config("Final URI: " + uri.toString());
            ResourceConfig rc = new ResourceConfig().packages("com.mn.service.movies.resources");
            ServiceLogger.LOGGER.config("Set Jersey resources.");
            rc.register(JacksonFeature.class);
            ServiceLogger.LOGGER.config("Set Jackson as serializer.");
            ServiceLogger.LOGGER.config("Starting HTTP server...");
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);
            server.start();
            ServiceLogger.LOGGER.config("HTTP server started.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void exitAppFailure(String message) {
        System.err.println("ERROR: " + message);
        System.err.println("Usage options: ");
        System.err.println("\tSpecify configuration file:");
        System.err.println("\t\t--config [file]");
        System.err.println("\t\t-c");
        System.err.println("\tUse default configuration:");
        System.err.println("\t\t--default");
        System.err.println("\t\t-d");
        System.exit(-1);
    }

    private static void createRequiredTables()
    {
        String[] statements = {
                "CREATE TABLE IF NOT EXISTS `genres` (" +
                        "id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                        "name VARCHAR(32) NOT NULL);",
                "CREATE TABLE IF NOT EXISTS `movies` (" +
                        "id VARCHAR(10) PRIMARY KEY NOT NULL, " +
                        "title VARCHAR(100) NOT NULL, " +
                        "year INT NOT NULL, " +
                        "director VARCHAR(100) NOT NULL, " +
                        "backdrop_path VARCHAR(256) NULL, " +
                        "budget INT DEFAULT 0 NULL, " +
                        "overview VARCHAR(8192) NULL, " +
                        "poster_path VARCHAR(256) NULL, " +
                        "revenue INT DEFAULT 0 NULL, " +
                        "hidden INT DEFAULT 0 NULL);",
                "CREATE TABLE IF NOT EXISTS `genres_in_movies` (" +
                        "genreId INT NOT NULL, " +
                        "movieId VARCHAR(10) NOT NULL, " +
                        "CONSTRAINT pkMovieGenreId PRIMARY KEY(genreId, movieId), " +
                        "CONSTRAINT fkGenreId FOREIGN KEY (genreId) REFERENCES genres(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                        "CONSTRAINT fkMovieId FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE);",
                "CREATE INDEX movieId ON genres_in_movies(movieId);",
                "CREATE TABLE IF NOT EXISTS `ratings` (" +
                        "rating FLOAT NOT NULL, " +
                        "numVotes INT NOT NULL, " +
                        "movieId VARCHAR(10) NOT NULL, " +
                        "CONSTRAINT fkRatingMovieId FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE);",
                "CREATE TABLE IF NOT EXISTS `stars` (" +
                        "id VARCHAR(10) NOT NULL PRIMARY KEY, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "birthYear INT NULL);",
                "CREATE TABLE IF NOT EXISTS `stars_in_movies` (" +
                        "starId VARCHAR(10) NOT NULL, " +
                        "movieId VARCHAR(10) NOT NULL, " +
                        "CONSTRAINT pkMovieStarId PRIMARY KEY(starId, movieId), " +
                        "CONSTRAINT fkStarsInMoviesStarId FOREIGN KEY (starId) REFERENCES stars(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                        "CONSTRAINT fkStarsInMoviesMovieId FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE);",
                "CREATE INDEX movieId ON stars_in_movies(movieId);"
        };
        for (String statement : statements)
        {
            try
            {

                PreparedStatement ps = MovieService.getCon().prepareStatement(statement);

                ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
                ps.executeUpdate();
                ServiceLogger.LOGGER.info("Query succeeded.");
            }
            catch (SQLException e)
            {
                // Error code 1061 is for duplicate key when creating existing indices
                if (e.getErrorCode() != 1061)
                    e.printStackTrace();
            }
        }
    }

    public static Connection getCon()
            throws SQLException
    {
        if (con == null)
            throw new SQLException("Connection to database could not be established");
        return con;
    }


    public static MovieConfigs getMovieConfigs() {
        return movieConfigs;
    }
}
