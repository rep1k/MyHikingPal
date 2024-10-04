package com.myhikingpal.model;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Class for the database connection of the routes
 */
public class DbConnectionRoute {
    
    private final static String DB_URL = "jdbc:sqlite:src/main/java/com/myhikingpal/Db/routes.sqlite";
    // Singleton instance
    private static DbConnectionRoute instance;

    // Database connection object
    private final Connection connection;


    /**
     * Constructor for the database connection of the routes
     * @throws SQLException throws SQLException if connection fails
     */
    private DbConnectionRoute() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
    }

    /**
     * Method for getting the singleton instance of the database connection
     * @return returns the instance of the database connection
     * @throws SQLException throws SQLException if connection fails
     */
    public static DbConnectionRoute getInstance() throws SQLException {
        if (instance == null) {
            instance = new DbConnectionRoute();
        }
        return instance;
    }

    /**
     * Method for getting the database connection
     * @return returns the database connection
     */
    public Connection getConnection() {
        return connection;
    }
}
