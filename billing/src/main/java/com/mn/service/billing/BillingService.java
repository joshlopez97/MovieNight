package com.mn.service.billing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mn.service.billing.configs.Configs;
import com.mn.service.billing.logger.ServiceLogger;
import com.mn.service.billing.models.ConfigsModel;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.internal.util.ExceptionUtils;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BillingService {
    public static BillingService billingService;

    private static Connection con = null;
    private static Configs configs = new Configs();

    public static void main(String[] args) {
        billingService = new BillingService();
        billingService.initService(args);
    }

    private void initService(String[] args) {
        // Validate arguments
        validateArguments(args);
        // Exec the arguments
        execArguments(args);
        // Initialize logging
        initLogging();
        ServiceLogger.LOGGER.config("Starting service...");
        configs.currentConfigs();
        // Connect to database
        connectToDatabase();
        createRequiredTables();
        // Initialize HTTP server
        initHTTPServer();

        if (con != null) {
            ServiceLogger.LOGGER.config("Service initialized.");
        } else {
            ServiceLogger.LOGGER.config("Service initialized with error(s)");
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
            configs = new Configs(loadConfigs(configFile));
            System.err.println("Configuration file successfully loaded.");
        } catch (NullPointerException e) {
            System.err.println("Config file not found. Using default values.");
            configs = new Configs();
        }
    }

    private ConfigsModel loadConfigs(String file) {
        System.err.println("Loading configuration file...");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ConfigsModel configs = null;

        try {
            configs = mapper.readValue(new File(file), ConfigsModel.class);
        } catch (IOException e) {
            exitAppFailure("Unable to load configuration file.");
        }
        return configs;
    }

    private void initLogging() {
        try {
            ServiceLogger.initLogger(configs.getOutputDir(), configs.getOutputFile());
        } catch (IOException e) {
            exitAppFailure("Unable to initialize logging.");
        }
    }

    private void initHTTPServer() {
        ServiceLogger.LOGGER.config("Initializing HTTP server...");
        String scheme = configs.getScheme();
        String hostName = configs.getHostName();
        int port = configs.getPort();
        String path = configs.getPath();

        try {
            ServiceLogger.LOGGER.config("Building URI from configs...");
            URI uri = UriBuilder.fromUri(scheme + hostName + path).port(port).build();
            ServiceLogger.LOGGER.config("Final URI: " + uri.toString());
            ResourceConfig rc = new ResourceConfig().packages("com.mn.service.billing.resources");
            ServiceLogger.LOGGER.config("Set Jersey resources.");
            rc.register(JacksonFeature.class);
            ServiceLogger.LOGGER.config("Set Jackson as serializer.");
            ServiceLogger.LOGGER.config("Starting HTTP server...");
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);
            server.start();
            ServiceLogger.LOGGER.config("HTTP server started.");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void connectToDatabase() {
        ServiceLogger.LOGGER.config("Connecting to database...");

        if (!configs.isDbConfigValid()) {
            ServiceLogger.LOGGER.config("Database configurations not valid. Cannot connect to database.");
            return;
        }

        try {
            Class.forName(configs.getDbDriver());
            ServiceLogger.LOGGER.config("Database URL: " + configs.getDbUrl());
            con = DriverManager.getConnection(configs.getDbUrl(), configs.getDbUsername(), configs.getDbPassword());
            ServiceLogger.LOGGER.config("Connected to database: " + configs.getDbUrl());
        } catch (ClassNotFoundException | SQLException | NullPointerException e) {
            ServiceLogger.LOGGER.severe("Unable to connect to database.\n" + ExceptionUtils.exceptionStackTraceAsString(e));
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
                "CREATE TABLE IF NOT EXISTS `carts` (" +
                        "id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                        "email VARCHAR(50) NOT NULL, " +
                        "movieId VARCHAR(10) NOT NULL, " +
                        "quantity INT NOT NULL, " +
                        "CONSTRAINT unqCart UNIQUE(email, movieId));",
                "CREATE TABLE IF NOT EXISTS `creditcards` (" +
                        "id VARCHAR(20) PRIMARY KEY NOT NULL, " +
                        "firstName VARCHAR(50) NOT NULL, " +
                        "lastName VARCHAR(50) NOT NULL, " +
                        "expiration DATE NOT NULL);",
                "CREATE TABLE IF NOT EXISTS `customers` (" +
                        "email VARCHAR(50) PRIMARY KEY NOT NULL, " +
                        "firstName VARCHAR(50) NOT NULL, " +
                        "lastName VARCHAR(50) NOT NULL, " +
                        "ccId VARCHAR(50) NOT NULL, " +
                        "address VARCHAR(200) NOT NULL, " +
                        "CONSTRAINT fkCustomerCreditCardId FOREIGN KEY (ccId) REFERENCES creditcards(id) ON DELETE CASCADE ON UPDATE CASCADE);",
                "CREATE TABLE IF NOT EXISTS `sales` (" +
                        "id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                        "email VARCHAR(50) NOT NULL, " +
                        "movieId VARCHAR(10) NOT NULL, " +
                        "quantity INT NOT NULL, " +
                        "saleDate DATE NOT NULL, " +
                        "CONSTRAINT fkSaleCustomerEmail FOREIGN KEY (email) REFERENCES customers(email) ON DELETE CASCADE ON UPDATE CASCADE);",
                "CREATE TABLE IF NOT EXISTS `movie_prices` (" +
                        "movieId VARCHAR(10) PRIMARY KEY NOT NULL, " +
                        "unit_price FLOAT NOT NULL, " +
                        "discount FLOAT NOT NULL);",
                "CREATE TABLE IF NOT EXISTS `transactions` (" +
                        "sId INT PRIMARY KEY NOT NULL, " +
                        "token VARCHAR(50) NOT NULL, " +
                        "transactionId VARCHAR(50)," +
                        "CONSTRAINT fkTransactionSaleId FOREIGN KEY (sId) REFERENCES sales(id) ON DELETE CASCADE ON UPDATE CASCADE);"
        };
        for (String statement : statements)
        {
            try
            {

                PreparedStatement ps = BillingService.getCon().prepareStatement(statement);

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

    public static Configs getConfigs() {
        return configs;
    }
}