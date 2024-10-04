package com.myhikingpal.model;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * Class for the database connection of the user
 */
public class DbConnectionUser {

        private final static String DB_URL = "jdbc:sqlite:src/main/java/com/myhikingpal/Db/user.sqlite";
        // Singleton instance
        private static DbConnectionUser instance;

        // Database connection object
        private Connection connection;
    
        // Private constructor to prevent direct instantiation

    /**
     * Constructor for the database connection of the user
     * @throws SQLException throws SQLException if connection fails
     */
    private DbConnectionUser() throws SQLException {
            this.connection = DriverManager.getConnection(DB_URL);
        }
    
        // Static method to get the singleton instance

    /**
     * Method for getting the singleton instance of the database connection
     * @return returns the instance of the database connection
     * @throws SQLException throws SQLException if connection fails
     */
    public static DbConnectionUser getInstance() throws SQLException {

            if (instance == null || instance.getConnection().isClosed()) {
                instance = new DbConnectionUser();
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

