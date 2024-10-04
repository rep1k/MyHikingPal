package com.myhikingpal.controller;

import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.*;


import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.myhikingpal.model.DbConnectionUser;
import com.myhikingpal.model.User;

/**
 * Controller class for sign up scene
 */
public class SignUpSceneController {

    @FXML
    TextField signUpUsername;
    @FXML
    PasswordField signUpPassword;
    @FXML
    PasswordField signUpConfPassword;
    @FXML
    Label signUpLabel;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection connection;
    private User user;

    /**
     * Method to switch to login scene
     */
    public void initialize() {
        try{
            connection = DbConnectionUser.getInstance().getConnection();
        } catch (Exception e ){
            System.out.println("Error in loading database!");
        }
    }

    /**
     * Method to check signup credentials
     * @return returns true if sign up was successful
     */
    @FXML
    private boolean signupCheck(){
        
        // String messLabel = signUpLabel.getText();


        String username = signUpUsername.getText();
        String password = signUpPassword.getText();
        String passwordConfirm = signUpConfPassword.getText();


        String sql = "SELECT * FROM users WHERE name = ?";
        try 
        {

            PreparedStatement pstmt  = connection.prepareStatement(sql);

            pstmt.setString(1, username);
            ResultSet rs  = pstmt.executeQuery();
            if(rs.getString("name") == null && password.equals(passwordConfirm) && password.length() >= 6 && password.length() <= 12){
                registerToDB(username, password);
                return true;

            } else {
                signUpLabel.setText("Incorrect credetials => Password needs to be 6 - 12 characters long, and username needs to be unique");
                signUpLabel.setLayoutX(100);
                System.out.println("Username: " + username + "  Pass: " + password + "  PassConf: " + passwordConfirm);
            }
            

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        

        return false;


    }

    /**
     * Method to register user to database
     * @param username username of the user
     * @param pass  password of the user
     * @throws SQLException  throws exception if there is an error in SQL query
     */

    private void registerToDB(String username, String pass) throws SQLException {

//        final User customer = User.getInstance(username, pass);
//        customer.CreateUser();
        String sql = "INSERT INTO users (name, password, reviews, doneRoutes ,expenses) VALUES (?, ?, ? ,?, ?)";
        try (PreparedStatement pstmt  = connection.prepareStatement(sql)) {


            pstmt.setString(1, username);
            pstmt.setString(2, pass);
            pstmt.setInt(3, 0);
            pstmt.setString(4, "");
            pstmt.setString(5, "");

            int insertRows  = pstmt.executeUpdate();

            System.out.println(insertRows + " inserted rows");
            List<String > done_routes = new ArrayList<>();
            List<String> expenses = new ArrayList<>();
            user = User.getInstance(username, pass, done_routes, expenses);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        
    }

    /**
     * Method to switch to main scene
     * @param event event that triggers the method
     */
    @FXML
    public void signup (ActionEvent event){

        if(signupCheck()){
            try{

                String username = signUpUsername.getText();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainScene.fxml"));
                root = loader.load();
                user = User.getInstance();
                MainSceneController sceneToLogin = loader.getController();
                sceneToLogin.displayName(username);
                // sceneToLogin.displayTime();
                
                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                String css = this.getClass().getResource("/gui/Gui_Interface.css").toExternalForm();
                
                scene.getStylesheets().add(css);
                stage.setScene(scene);
                stage.show();
    
            } catch(Exception e) {
                System.out.print(e);
            }
        }


        
        
    }

    /**
     *  Method to switch to login scene
     * @param event event that triggers the method
     */
    @FXML
    public void goback(ActionEvent event){

        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginScene.fxml"));
            root = loader.load();
            

            
            scene = new Scene(root);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            String css = this.getClass().getResource("/gui/Gui_Interface.css").toExternalForm();
            
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            System.out.print(e);
        }
    }

}
