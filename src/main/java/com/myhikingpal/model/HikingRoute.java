package com.myhikingpal.model;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class for the database connection of the hiking routes
 */
public class HikingRoute {
    
    private String startLocation;
    private String endLocation;
    private String routeTime;
    private float distance;
    private String difficulty;
    private String map;
    private int Multiday;

    private Connection connection;

    /**
     * Constructor for the hiking routes
     * @param startLocation starting location of the hike
     * @param endLocation end location of the hike
     * @param Time time of the hike
     * @param distance  distance of the hike
     * @param difficulty difficulty of the hike
     * @param map map of the hike
     * @param Multiday  if the hike is multi-day of the hike
     */
    public HikingRoute(String startLocation, String endLocation, String Time, 
        float distance, String difficulty, String map, int Multiday){
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            this.routeTime = Time;
            this.distance = distance;
            this.difficulty = difficulty;
            this.map = map;
            this.Multiday = Multiday;
        }

        /**
         * Constructor for the hiking routes
         */
    public HikingRoute(){
        this.distance = 0;
    }


    /**
     * Method used to retrieve hike info from database
     * @param startLocation starting location of the hike
     * @param endLocation end location of the hike
     * @return returns the info of Hike Route
     */
    public HikingRoute getRouteInfo(String startLocation, String endLocation){
        String sql = "SELECT * FROM route where startLocation = ? AND endLocation = ?";
        
        try  
        {
            connection = DbConnectionRoute.getInstance().getConnection();
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            pstmt.setString(1, startLocation);
            pstmt.setString(2, endLocation);
            ResultSet rs  = pstmt.executeQuery();
            
            this.startLocation = rs.getString("startLocation");
            this.endLocation = rs.getString("endLocation");
            this.routeTime = rs.getString("Time");
            this.distance = rs.getFloat("lenght");
            this.difficulty = rs.getString("difficulty");
            this.map = rs.getString("map");
            this.Multiday = rs.getInt("Multiday");
                
                System.out.printf("[DEBUG] === GET %s ROUTE === \n", startLocation);

            return this;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        
    
        
    }

    /**
     * Method used for initializing the routes from database
     * @return returns the List of all route names
     */
    public List<String> getInitRouteNames(){
        String sql = "SELECT startLocation, endLocation FROM route";
        
        try  
        {
            Connection conn = DbConnectionRoute.getInstance().getConnection();
            PreparedStatement pstmt  = conn.prepareStatement(sql);
            ResultSet rs  = pstmt.executeQuery();
            List<String> routeNames = new ArrayList<>();

            while(rs.next()){
                String name = rs.getString("startLocation");
                String nameEnd = rs.getString("endLocation");
                String fullRoute = name.concat("-").concat(nameEnd);
                // routeNames.add(name);
                // routeNames.add(nameEnd);
                routeNames.add(fullRoute);
                System.out.println(fullRoute);
            }
            return routeNames;
           

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        
    }

    /**
     * Method to retrieve start location
     * @return returns string
     */
    public String getStart(){
        System.out.println(startLocation);
        return startLocation;
    }

    /**
     * Method to retrieve start location
     * @return returns string
     */
    public String getStartLocation(){
        return this.startLocation;
    }

    /**
     * Method to retrieve end location
     * @return returns string
     */
    public String getEndLocation(){
        return this.endLocation;
    }
    /**
     * Method to retrieve hike route time
     * @return returns string
     */

    public String getRouteTime() {
        return this.routeTime;
    }

    /**
     * Method to retrieve hike route distance
     * @return returns string
     */
    public float getRouteDistance() {
        return this.distance;
    }

    /**
     * Method to retrieve hike route difficulty
     * @return returns string
     */
    public String getRouteDiff() {
        return this.difficulty;
    }

    /**
     * Method to retrieve hike route map
     * @return returns string
     */
    public String getMap() {
        return this.map;
    }

    /**
     * Method to retrieve hike route info if the route is multi-day
     * @return returns int
     */

    public int getMultidayInfo() {
        return this.Multiday;
    }




}
