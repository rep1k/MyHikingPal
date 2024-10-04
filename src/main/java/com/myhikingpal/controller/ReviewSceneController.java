package com.myhikingpal.controller;

import com.myhikingpal.model.UserReview;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.awt.*;
import java.util.List;

/**
 * Class for the review scene controller
 */

public class ReviewSceneController {


    @FXML
    Button mainSceneButton;
    @FXML
    Button mapButton;
    @FXML
    ListView<String> reviews;
    private Stage stage;
    private Scene scene;
    private Parent root;
    UserReview user_reviews = new UserReview();

    /**
     * Method to initialize the review scene
     */

    public void initialize() {

        List<List<String>> info = user_reviews.getReviews();
        for(List<String> row: info){
            reviews.getItems().add(String.format("%s ---- %s-%s \n\t(-) %s",row.get(0), row.get(1), row.get(2), row.get(3) ));
        }
    }

    /**
     * Method to switch to map scene
     * @param event ActionEvent object
     */

    public void switchToMapScene(ActionEvent event){
        try{
            root = FXMLLoader.load(getClass().getResource("/gui/MapScene.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Method to switch to main scene
     * @param event ActionEvent object
     */

    public void switchToMainScene(ActionEvent event){
        try{
            root = FXMLLoader.load(getClass().getResource("/gui/MainScene.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Method to switch to login scene
     * @param event ActionEvent object
     */
    public void switchToLoginScene(ActionEvent event) {
        try{
            root = FXMLLoader.load(getClass().getResource("/gui/LoginScene.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            

        } catch(Exception e) {
            System.out.println(e);
        }
    }

}
