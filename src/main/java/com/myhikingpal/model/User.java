package com.myhikingpal.model;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for User object
 */
public class User {
    private static User instance;
    private final String username;
    private final String password;
    private int reviews;
    private String review_content;
    private List<String> done_routes;
    private List<String> expenses;
    private Connection connection;

    /**
     * Constructor for the User object
     * @param username username
     * @param password password
     * @param done_routes routes done by the user
     * @param expenses expenses of the user
     */

    public User(String username, String password, List<String> done_routes, List<String> expenses) {
        this.username = username;
        this.password = password;
        this.done_routes = done_routes;
        this.expenses = expenses;
    }

    /**
     * Method for getting the user instance
     * @param username username of the user
     * @param password password of the user
     * @param done_routes routes done by the user
     * @param expenses all expenses of done routes
     * @return returns the instance of a user
     */
    public static User getInstance(String username, String password, List<String> done_routes, List<String> expenses) {
        if (instance == null) {
            instance = new User(username, password, done_routes, expenses);
        }
        return instance;
    }

    /**
     * Method for getting the instance of a user
     * @return returns the instance of a user
     */

    public static User getInstance() {
        return instance;
    }

    /**
     * Method for updating user's done routes in database
     * @param route chosen route to be done
     */
    public void updateDone_routes(String route) {
        //TODO: Update database entry if new routes has been started
        String sql = "UPDATE users SET doneRoutes = ? WHERE name = ?";
        try  {
            connection = DbConnectionUser.getInstance().getConnection();
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            pstmt.setString(2, this.username);
            List<String> tempList = new ArrayList<>(done_routes);
            tempList.add(route);
            StringBuilder newRoutes = new StringBuilder();
            for(String s : tempList){
                newRoutes.append(s).append(",");
            }
            pstmt.setString(1, newRoutes.toString());
            pstmt.executeUpdate();
            done_routes = new ArrayList<>(tempList);
            System.out.println(" inserted rows");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Method to update expenses that took in the done route
     * @param route chosen route to be done
     * @param price calculated price for the route
     */
    public void updateExpenses(String route,String price) {

        // Update database entry if new routes has been started
        String sql = "UPDATE users SET expenses = ? WHERE name = ?";
        try {
            connection = DbConnectionUser.getInstance().getConnection();
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            pstmt.setString(2, instance.username);
            List<String> tempList = new ArrayList<>(expenses);
            StringBuilder routePrice = new StringBuilder();
            routePrice.append(route).append(" - ").append(price);

            tempList.add(routePrice.toString());
            StringBuilder newExpenses = new StringBuilder();
            for(String s : tempList){
                newExpenses.append(s).append(",");
            }

            pstmt.setString(1, newExpenses.toString());
            pstmt.executeUpdate();
            expenses = new ArrayList<>(tempList);

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to get the user's done routes
     * @return returns the user's done routes
     */
    public List<String> getDone_routes() {
        return done_routes;
    }

    /**
     * Method to get the user's expenses
     * @return returns the user's expenses
     */

    public List<String> getExpenses() {
        return expenses;
    }

    /**
     * Method to get the user's username
     * @return returns the user's username
     */

    public String getUsername() {
        return username;
    }
}
