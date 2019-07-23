package com.mn.service.idm.configs;

import com.mn.service.idm.models.ConfigsModel;
import com.mn.service.idm.logger.ServiceLogger;

import java.util.Map;

public class Configs {
    // Default service configs
    private final String DEFAULT_SCHEME = "http://";
    private final String DEFAULT_HOSTNAME = "0.0.0.0";
    private final int    DEFAULT_PORT = 6243;
    private final String DEFAULT_PATH = "/api/idmService";

    // Default logger configs
    private final String DEFAULT_OUTPUTDIR = "./logs/";
    private final String DEFAULT_OUTPUTFILE = "idmService.log";

    // Service configs
    private String scheme;
    private String hostName;
    private int    port;
    private String path;

    // Logger configs
    private String outputDir;
    private String outputFile;

    // Database configs
    private String dbUsername;
    private String dbPassword;
    private String dbHostname;
    private int    dbPort;
    private String dbDriver;
    private String dbName;
    private String dbSettings;

    private long sessionTimeout;
    private long sessionExpr;


    public Configs() {
        scheme = DEFAULT_SCHEME;
        hostName = DEFAULT_HOSTNAME;
        port = DEFAULT_PORT;
        path = DEFAULT_PATH;
        outputDir = DEFAULT_OUTPUTDIR;
        outputFile = DEFAULT_OUTPUTFILE;
    }

    public Configs(ConfigsModel cm) throws NullPointerException {
        if (cm == null) {
            throw new NullPointerException("Unable to create Configs from ConfigsModel.");
        } else {
            // Set service configs
            scheme = cm.getServiceConfig().get("scheme");
            if (scheme == null) {
                scheme = DEFAULT_SCHEME;
                System.err.println("Scheme not found in configuration file. Using default.");
            }

            hostName = cm.getServiceConfig().get("hostName");
            if (hostName == null) {
                hostName = DEFAULT_HOSTNAME;
                System.err.println("Hostname not found in configuration file. Using default.");
            }

            port = Integer.parseInt(cm.getServiceConfig().get("port"));
            if (port == 0) {
                port = DEFAULT_PORT;
                System.err.println("Port not found in configuration file. Using default.");
            } else if (port < 1024 || port > 65536) {
                port = DEFAULT_PORT;
                System.err.println("Port is not within valid range. Using default.");
            }

            path = cm.getServiceConfig().get("path");
            if (path == null) {
                path = DEFAULT_PATH;
                System.err.println("Path not found in configuration file. Using default.");
            }

            // Set logger configs
            outputDir = cm.getLoggerConfig().get("outputDir");
            if (outputDir == null) {
                outputDir = DEFAULT_OUTPUTDIR;
                System.err.println("Logging output directory not found in configuration file. Using default.");
            }

            outputFile = cm.getLoggerConfig().get("outputFile");
            if (outputFile == null) {
                outputFile = DEFAULT_OUTPUTFILE;
                System.err.println("Logging output file not found in configuration file. Using default.");
            }

            dbUsername = getRequired(cm.getDatabaseConfig(), "dbUsername");
            dbPassword = getRequired(cm.getDatabaseConfig(), "dbPassword");
            dbPort     = Integer.parseInt(getRequired(cm.getDatabaseConfig(), "dbPort"));
            dbName     = getRequired(cm.getDatabaseConfig(), "dbName");
            dbHostname = getRequired(cm.getDatabaseConfig(), "dbHostname");
            dbDriver   = getRequired(cm.getDatabaseConfig(), "dbDriver");
            dbSettings = getRequired(cm.getDatabaseConfig(), "dbSettings");

            sessionExpr = Long.parseLong(getRequired(cm.getSessionConfig(), "expiration"));
            sessionTimeout = Long.parseLong(getRequired(cm.getSessionConfig(), "timeout"));
        }
    }

    private String getRequired(Map<String, String> config, String key)
            throws NullPointerException
    {
        String configValue = config.get(key);
        if (configValue == null)
            throw new NullPointerException("Required value `" + key + "` missing in configuration file.");
        return configValue;
    }

    public void currentConfigs() {
        ServiceLogger.LOGGER.config("Scheme: " + scheme);
        ServiceLogger.LOGGER.config("Hostname: " + hostName);
        ServiceLogger.LOGGER.config("Port: " + port);
        ServiceLogger.LOGGER.config("Path: " + path);
        ServiceLogger.LOGGER.config("Logger output directory: " + outputDir);
        ServiceLogger.LOGGER.config("Logger output file: " + outputFile);
        ServiceLogger.LOGGER.config("DB username: " + dbUsername);
        ServiceLogger.LOGGER.config("DB password provided");
        ServiceLogger.LOGGER.config("DB hostname: " + dbHostname);
        ServiceLogger.LOGGER.config("DB port: " + dbPort);
        ServiceLogger.LOGGER.config("DB driver: " + dbDriver);
        ServiceLogger.LOGGER.config("DB settings: " + dbSettings);
        ServiceLogger.LOGGER.config("Session expire: " + sessionExpr);
        ServiceLogger.LOGGER.config("Session timeout: " + sessionTimeout);
    }

    public String getScheme() {
        return scheme;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getDbUsername()
    {
        return dbUsername;
    }

    public String getDbPassword()
    {
        return dbPassword;
    }

    public String getDbHostname()
    {
        return dbHostname;
    }

    public int getDbPort()
    {
        return dbPort;
    }

    public String getDbDriver()
    {
        return dbDriver;
    }

    public String getDbName()
    {
        return dbName;
    }

    public String getDbSettings()
    {
        return dbSettings;
    }

    public long getSessionTimeout()
    {
        return sessionTimeout;
    }

    public long getSessionExpr()
    {
        return sessionExpr;
    }
}
