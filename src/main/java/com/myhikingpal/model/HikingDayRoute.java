package com.myhikingpal.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Class for the database connection of the hiking routes
 */

public class HikingDayRoute extends HikingRoute {       //! Inheritance
    private static HikingDayRoute instance = null;
    private String startLocation;
    private String endLocation;
    private String routeTime;
    private float distance;
    private String difficulty;
    private String map;
    private int Multiday;

    private Connection connection;

    /**
     * Constructor for the database connection of the hiking routes
     * @param startLocation starting location of the hike
     * @param endLocation end location of the hike
     * @param Time time of the hike
     * @param distance distance of the hike
     * @param difficulty difficulty of the hike
     * @param map map of the hike
     * @param Multiday multiday of the hike
     */
    HikingDayRoute(String startLocation, String endLocation, String Time,
                   float distance, String difficulty, String map, int Multiday){
        super(startLocation, endLocation, Time, distance, difficulty, map, Multiday);
    }

    /**
     * Method for getting the singleton instance of the Day Hike route
     * @param startLocation starting location of the hike
     * @param endLocation end location of the hike
     * @param Time time of the hike
     * @param distance  distance of the hike
     * @param difficulty difficulty of the hike
     * @param map map of the hike
     * @param Multiday if the hike is multi-day of the hike
     * @return returns the info of Hike Route
     */
    public static HikingDayRoute getInstance(String startLocation, String endLocation, String Time,
                                      float distance, String difficulty, String map, int Multiday){
        if(instance == null){
            instance = new HikingDayRoute(startLocation, endLocation, Time, distance, difficulty, map, Multiday);
        }
        return instance;
    }

    /**
     * Method used to retrieve hike info from database
     * @return returns the info of Hike Route
     */
    public static HikingDayRoute getInstance(){

        return instance;
    }

    /**
     *  Method used to retrieve hike info from database
     * @param startLocation starting location of the hike
     * @param endLocation end location of the hike
     * @return  returns the info of Hike Route
     */
    @Override
    public HikingRoute getRouteInfo(String startLocation,  String endLocation){      //! Polymorphism
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
//            this.Multiday = rs.getInt("Multiday");

            System.out.printf("[DEBUG] === GET %s ROUTE === \n", startLocation);

            return this;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
//        System.out.println("The chosen location is .....")
    }

}
