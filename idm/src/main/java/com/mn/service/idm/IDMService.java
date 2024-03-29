/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.mn.service.idm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mn.service.idm.configs.Configs;
import com.mn.service.idm.logger.ServiceLogger;
import com.mn.service.idm.models.ConfigsModel;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
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

public class IDMService
{
    public static IDMService idmService;

    private static Configs configs = new Configs();
    private static Connection con;

    public static void main(String[] args)
    {
        idmService = new IDMService();
        idmService.initService(args);
    }

    private void initService(String[] args)
    {
        // Validate arguments
        idmService.validateArguments(args);

        // Exec the arguments
        idmService.execArguments(args);

        // Initialize logging
        initLogging();
        ServiceLogger.LOGGER.config("Starting service...");
        configs.currentConfigs();

        // Connect to database
        connectToDatabase();

        // Initialize HTTP server
        initHTTPServer();
        ServiceLogger.LOGGER.config("Service initialized.");
    }

    private void validateArguments(String[] args)
    {
        boolean isConfigOptionSet = false;
        for (int i = 0; i < args.length; ++i)
        {
            switch (args[i])
            {
                case "--config":
                case "-c":
                    if (!isConfigOptionSet)
                    {
                        isConfigOptionSet = true;
                        ++i;
                    }
                    else
                    {
                        exitAppFailureArgs("Conflicting configuration file arguments.");
                    }
                    break;

                default:
                    exitAppFailureArgs("Unrecognized argument: " + args[i]);
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

    private void getConfigFile(String configFile)
    {
        try
        {
            System.err.println("Config file name: " + configFile);
            configs = new Configs(loadConfigs(configFile));
            System.err.println("Configuration file successfully loaded.");
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            System.err.println("Config file not found. Using default values.");
            configs = new Configs();
        }
    }

    private ConfigsModel loadConfigs(String file)
    {
        System.err.println("Loading configuration file...");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ConfigsModel configs = null;

        try
        {
            configs = mapper.readValue(new File(file), ConfigsModel.class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            exitAppFailure("Unable to load configuration file.");
        }
        return configs;
    }

    private void initLogging()
    {
        try
        {
            ServiceLogger.initLogger(configs.getOutputDir(), configs.getOutputFile());
        }
        catch (IOException e)
        {
            exitAppFailure("Unable to initialize logging.");
        }
    }

    private void initHTTPServer()
    {
        ServiceLogger.LOGGER.config("Initializing HTTP server...");
        String scheme = configs.getScheme();
        String hostName = configs.getHostName();
        int port = configs.getPort();
        String path = configs.getPath();

        try
        {
            URI uri = UriBuilder.fromUri(scheme + hostName + path).port(port).build();
            ResourceConfig rc = new ResourceConfig().packages("com.mn.service.idm.resources");
            rc.register(JacksonFeature.class);
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);
            server.start();
            ServiceLogger.LOGGER.config("HTTP server started.");
        }
        catch (IOException e)
        {
            throw new RuntimeException();
        }
    }

    private void exitAppFailure(String message)
    {
        System.err.println("ERROR: " + message);
    }

    private void exitAppFailureArgs(String message)
    {
        System.err.println("ERROR: " + message);
        System.err.println("Usage options: ");
        System.err.println("\tSpecify configuration file:");
        System.err.println("\t\t--config [file]");
        System.err.println("\t\t-c");
        System.exit(-1);
    }
    private void connectToDatabase() {
        ServiceLogger.LOGGER.config("Connecting to database...");
        String hostName = configs.getDbHostname();
        int port = configs.getDbPort();
        String username = configs.getDbUsername();
        String password = configs.getDbPassword();
        String dbName = configs.getDbName();
        String settings = configs.getDbSettings();
        String driver = configs.getDbDriver();

        try {
            Class.forName(driver);
            String url = "jdbc:mysql://" + hostName + ":" + port + "/" + dbName + settings;
            ServiceLogger.LOGGER.config("Database URL: " + url);
            con = DriverManager.getConnection(url, username, password);
            ServiceLogger.LOGGER.config("Connected to database: " + con.toString());
            createRequiredTables();
        }
        catch (Exception e) {
            e.printStackTrace();
            if (e instanceof ClassCastException) {
                ServiceLogger.LOGGER.warning("Unable to load class for driver \"" + driver + "\".");
            }
            if (e instanceof SQLException) {
                ServiceLogger.LOGGER.warning("Access problem while loading driver \"" + driver + "\" into memory.");
            }
            if (e instanceof NullPointerException) {
                ServiceLogger.LOGGER.warning("Unable to instantiate driver: " + driver);
            }
            ServiceLogger.LOGGER.warning("Connection to database " + dbName + " failed.");
        }
    }

    private static void createRequiredTables()
    {
        String[] statements = {
                "CREATE TABLE IF NOT EXISTS `privilege_levels` (" +
                        "plevel INT PRIMARY KEY NOT NULL, " +
                        "pname VARCHAR(20) NOT NULL);",
                "CREATE TABLE IF NOT EXISTS `session_status` (" +
                        "statusid INT PRIMARY KEY NOT NULL, " +
                        "status VARCHAR(20) NOT NULL);",
                "CREATE TABLE IF NOT EXISTS `user_status` (" +
                        "statusid INT PRIMARY KEY NOT NULL, " +
                        "status VARCHAR(20) NOT NULL);",
                "CREATE TABLE IF NOT EXISTS `sessions` (" +
                        "email VARCHAR(50) NOT NULL, " +
                        "sessionID varchar(128)  PRIMARY KEY NOT NULL, " +
                        "status INT NOT NULL, " +
                        "timeCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                        "lastUsed TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                        "exprTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                        "CONSTRAINT fkSessionStatusID FOREIGN KEY (status) REFERENCES session_status(statusid) ON DELETE CASCADE ON UPDATE CASCADE, " +
                        "CONSTRAINT fkSessionEmail FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE ON UPDATE CASCADE);",
                "CREATE TABLE IF NOT EXISTS `users` (" +
                        "id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                        "email VARCHAR(50) NOT NULL UNIQUE, " +
                        "status INT NOT NULL, " +
                        "plevel INT NOT NULL, " +
                        "salt VARCHAR(8) NOT NULL, " +
                        "pword VARCHAR(128) NOT NULL, " +
                        "CONSTRAINT fkPlevel FOREIGN KEY (plevel) REFERENCES privilege_levels(plevel) ON DELETE CASCADE ON UPDATE CASCADE, " +
                        "CONSTRAINT fkStatusID FOREIGN KEY (status) REFERENCES user_status(statusid) ON DELETE CASCADE ON UPDATE CASCADE);"
        };
        for (String statement : statements)
        {
            try
            {

                PreparedStatement ps = IDMService.getCon().prepareStatement(statement);

                ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
                ps.executeUpdate();
                ServiceLogger.LOGGER.info("Query succeeded.");
            }
            catch (SQLException e)
            {
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
