package com.myhikingpal;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

import com.myhikingpal.model.DbConnectionUser;

/**
 * JavaFX App
 */
public class App extends Application {


    /**
     * Method to start the application
     * @param mainWindow  Stage object
     * @throws IOException throws IOException
     */
    @Override
    public void start(Stage mainWindow) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/gui/LoginScene.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/gui/MapScene.fxml"));
        Scene scene = new Scene(root);


            mainWindow.setTitle("MyHikingPal");
            // mainWindow.initStyle(StageStyle.UNDECORATED);
            mainWindow.setResizable(false);
            mainWindow.setScene(scene);
            mainWindow.show();
    }


    /**
     * Main method
     * @param args arguments
     */
    public static void main(String[] args) {
        try{
            DbConnectionUser.getInstance().getConnection();
        } catch (Exception e ){
            System.out.println("Error in loading database!");
        }
        launch(args);
    }

}