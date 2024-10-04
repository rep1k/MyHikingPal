package com.myhikingpal.controller;

import com.myhikingpal.model.DbConnectionUser;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.*;


// import com.myhikingpal.model.User;
import com.myhikingpal.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Class for the login scene controller
 */
public class LoginSceneController {

    @FXML
    TextField nameFieldLogin;
    @FXML
    PasswordField passFieldLogin;
    @FXML
    Label loginLabel;


    private Stage stage;
    private Scene scene;
    private Parent root;
//    SessionData session;
    private Connection connection;

    /**
     *  Method to initialize the database connection
     */
    public void initialize(){
        try{
            connection = DbConnectionUser.getInstance().getConnection();
        } catch (Exception e ){
            System.out.println("Error in loading database!");
        }
    }

    /**
     * Method to Check login credentials
     * @return returns true if login is successful
     * @throws SQLException throws SQLException if connection fails
     */
    @FXML
    private boolean checkLogin() throws SQLException {
        boolean isUser = false;
        String username = nameFieldLogin.getText();
        String password = passFieldLogin.getText();
        List<String> done_hikes = null;
        List<String> user_expenses = null;
        String sql = "SELECT * FROM users WHERE name = ?";
        try
        {

            PreparedStatement pstmt  = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs  = pstmt.executeQuery();
            if(rs.getString("name") != null && password.equals(rs.getString("password"))){

                isUser = true;
               done_hikes = Arrays.asList(rs.getString("doneRoutes").split(","));
               user_expenses = Arrays.asList(rs.getString("expenses").split(","));


            } else {
                System.out.println("Username: " + username + "  Pass: " + password);
                isUser = false;
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        final User customer = User.getInstance(username, password, done_hikes, user_expenses);

        return isUser;

        
    }
    /**
     * Method to login and then transfer to the main scene
     * @param event event to be handled
     * @throws SQLException throws SQLException if connection fails
     */
    @FXML
    public void login (ActionEvent event) throws SQLException {

        
        if(checkLogin()){
            try{
                String username = nameFieldLogin.getText();
               
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainScene.fxml"));
                root = loader.load();
                
                MainSceneController sceneToLogin = loader.getController();
                sceneToLogin.displayName(username);
                // sceneToLogin.displayTime();
    
                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                String css = this.getClass().getResource("/gui/LoginScene.css").toExternalForm();
                
                scene.getStylesheets().add(css);
                stage.setScene(scene);
                stage.show();
    
            } catch(Exception e) {
                System.out.print(e);
            }
        } else {
            loginLabel.setText("Incorrect Credentials!");
        }
    }

    /** Method to transfer to the signup scene
     * @param event event to be handled
     */
    @FXML
    public void signup (ActionEvent event){
        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SignupScene.fxml"));
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
