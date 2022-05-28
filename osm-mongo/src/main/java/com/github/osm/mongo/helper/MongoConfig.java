package com.github.osm.mongo.helper;

import org.apache.commons.lang3.StringUtils;


/**
 * MongoDB connection configuration.
 */
public final class MongoConfig {

    private final String host;
    private final int port;
    private final String database;


    // Constructor
    // ------------------------------------------------------------------------

    private MongoConfig(String host, int port, String database) {
        super();

        // Sanity checks
        if (StringUtils.isEmpty(host)) {
            throw new IllegalArgumentException("MongoConfig :: Host should not be blank");
        }

        if (StringUtils.isEmpty(database)) {
            throw new IllegalArgumentException("MongoConfig :: DataBase should not be blank");
        }

        this.host = host;
        this.port = port;
        this.database = database;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "MongoConfig [host=" + host + ", port=" + port + ", database=" + database + "]";
    }


    // Factory
    // ------------------------------------------------------------------------

    public static MongoConfig with(String host, int port, String database) {
        return new MongoConfig(host, port, database);
    }


}
