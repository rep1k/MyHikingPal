package com.myhikingpal.model;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing the user reviews
 */

public class UserReview {

    private Connection connection;
    private User user;
    private HikingRoute route;
    private String review;

    /**
     * Constructor for the user reviews
     */
    public UserReview(){}

    /**
     * Constructor for the user reviews
     * @param user user
     * @param hikeRoute hiking route
     * @param review reviewS
     */
    public UserReview(User user, HikingRoute hikeRoute, String review){     //! Aggregation
        this.user = user;
        this.route = hikeRoute;
        this.review = review;
    }

    /**
     * Method to update user's reviews in database
     */
    public void updateReviews(){
        try  {
            String sql = "SELECT * FROM review WHERE user = ? AND routeLocation = ?";
            connection = DbConnectionReviews.getInstance().getConnection();
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            pstmt.setString(1, this.user.getUsername());
            pstmt.setString(2, this.route.getStartLocation());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                // If user and location exist, update the review_content
                sql = "UPDATE review SET review_content =? WHERE user =? AND routeLocation =? AND endLocation = ?";
                PreparedStatement pstmtUpdate = connection.prepareStatement(sql);
                pstmtUpdate.setString(1, review);
                pstmtUpdate.setString(2, user.getUsername());
                pstmtUpdate.setString(3, route.getStartLocation());
                pstmtUpdate.setString(4, route.getEndLocation());
                pstmtUpdate.executeUpdate();
            } else {
                // If user and location do not exist, insert a new row
                sql = "INSERT INTO review (user, routeLocation, endLocation ,review_content) VALUES(?,?,?,?)";
                PreparedStatement pstmtInsert = connection.prepareStatement(sql);
                pstmtInsert.setString(1, user.getUsername());
                pstmtInsert.setString(2, route.getStartLocation());
                pstmtInsert.setString(3, route.getEndLocation());
                pstmtInsert.setString(4, review);
                pstmtInsert.executeUpdate();
            }

        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    /**
     * Method to retrieve all user's reviews from database
     * @return and returns the List of all the reviews
     */
    public List<List<String>> getReviews() {
        try {
            List<List<String>> review_data = new ArrayList<>();
            String sql = "SELECT * FROM review";
            connection = DbConnectionReviews.getInstance().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                List<String> one_user = new ArrayList<>();
                one_user.add(rs.getString("user"));
                one_user.add(rs.getString("routeLocation"));
                one_user.add(rs.getString("endLocation"));
                one_user.add(rs.getString("review_content"));
                review_data.add(one_user);
            }

            for(List<String> s : review_data){
                System.out.println(s);
            }

            return review_data;
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
