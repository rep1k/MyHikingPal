package com.myhikingpal.model;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * Class for the database connection of the reviews
 */
public class DbConnectionReviews {
    private final static String DB_URL = "jdbc:sqlite:src/main/java/com/myhikingpal/Db/reviews.sqlite";
    // Singleton instance
    private static DbConnectionReviews instance;

    // Database connection object
    private Connection connection;


    /**
     * Constructor for the database connection of the reviews
     * @throws SQLException throws SQLException if connection fails
     */
    private DbConnectionReviews() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL);
    }


    /**
     * Method for getting the singleton instance of the database connection
     * @return returns the instance of the database connection
     * @throws SQLException throws SQLException if connection fails
     */
    public static DbConnectionReviews getInstance() throws SQLException {
//            System.out.println("");
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DbConnectionReviews();
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

    /**
     * Method for closing the database connection
     * @throws SQLException throws SQLException if connection fails
     */
    public void closeConnection() throws SQLException {
        connection.close();
    }
}
