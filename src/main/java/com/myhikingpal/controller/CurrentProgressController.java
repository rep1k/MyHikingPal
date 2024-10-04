package com.myhikingpal.controller;

import com.google.gson.JsonObject;
import com.myhikingpal.model.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Controller class for current progress scene
 */
public class CurrentProgressController {

    @FXML
    Text timeText;
    @FXML
    Text tempNow;
    @FXML
    ImageView weatherIcon;
    @FXML
    Text progressTest;
    @FXML
    ImageView map;
    @FXML
    ProgressBar progressBar;
    @FXML
    Text lenghtText;
    @FXML
    Text progressPercentage;
    @FXML
    Button mapButton;
    @FXML
    Button mainSceneButton;
    @FXML
    Button breakButton;
    @FXML
    Button resumeButton;
    @FXML
    Text breakText;
    @FXML
    TextArea reviewArea;
    @FXML
    Button submitButton;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private final WeatherAPI wApi = new WeatherAPI();
    private Connection connection;
    private HikingDayRoute hikeInstance;
    private HikingMultiDayRoute hikeMultiDayInstance;
    private User user;
    private boolean takingBreak = false;
    private final DoubleProperty progressProperty = new SimpleDoubleProperty(0);
    private final Semaphore semaphore = new Semaphore(1);

    /**
     * Method to initialize the scene
     */
    public void initialize(){
        try{
            connection = DbConnectionUser.getInstance().getConnection();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.9), evented -> {
                LocalTime time = LocalTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
                String formattedtime = time.format(myFormatObj);
                timeText.setText(formattedtime);
            }));
            progressBar.progressProperty().bind(progressProperty);

            user = User.getInstance();
            timeline.setCycleCount(Animation.INDEFINITE);
            hikeInstance = HikingDayRoute.getInstance();
            hikeMultiDayInstance = (HikingMultiDayRoute) HikingMultiDayRoute.getInstance();
            timeline.play();
            if(hikeInstance != null){
                mapWeather(hikeInstance.getStartLocation());
                progressTest.setText(hikeInstance.getStartLocation());
                String mapPath = getClass().getResource(hikeInstance.getMap()).toExternalForm();
                Image mapImage = new Image(mapPath);
                map.setImage(mapImage);
                lenghtText.setText(String.format("%.2f", hikeInstance.getRouteDistance()));
                try {
                    progressThread();

                } catch (Exception e) {
                    System.out.println(e);
                }
            } else if (hikeMultiDayInstance != null) {
                mapWeather(hikeMultiDayInstance.getStartLocation());
                progressTest.setText(hikeMultiDayInstance.getStartLocation());
                String mapPath = getClass().getResource(hikeMultiDayInstance.getMap()).toExternalForm();
                Image mapImage = new Image(mapPath);
                map.setImage(mapImage);
                lenghtText.setText(String.format("%.2f", hikeMultiDayInstance.getRouteDistance()));
                try {
                    progressThread();


                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     *  Method to simulate the progress of the hike with multithreading
     */
    public void progressThread() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Runnable task = new Runnable() {
            @Override
            public void run() {
//                resumeButton.setDisable(true);
                if(!mapButton.isDisabled() & !mainSceneButton.isDisabled() & !submitButton.isDisabled() & !reviewArea.isDisabled()){
                    mapButton.setDisable(true);
                    mainSceneButton.setDisable(true);
                    submitButton.setDisable(true);
                    reviewArea.setDisable(true);
                }
                System.out.println("Running on a New Thread");
                for (int i = 0; i <= 100; i++) {
                    try{
                        progressProperty.setValue(i / 100.0);
                        semaphore.acquire();
                        semaphore.release();
                        try {
                            Thread.sleep(500);  // Simulate work
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

//                        int finalI = i;
                        Platform.runLater(() -> progressPercentage.setText(String.format("%.2f", progressProperty.getValue() * 100)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                if(mapButton.isDisabled() & mainSceneButton.isDisabled() & submitButton.isDisabled() & reviewArea.isDisabled()){
                    mapButton.setDisable(false);
                    mainSceneButton.setDisable(false);
                    submitButton.setDisable(false);
                    reviewArea.setDisable(false);
                }
                if(!executorService.isShutdown()){
                    System.out.println("Shutting down");
                    executorService.shutdown();
                }

            }
        };
        executorService.execute(task);
    }

    /**
     * Method to submit a review after completing the hike
     * @param event     ActionEvent
     */
    public void submitReview (ActionEvent event){
        if(hikeInstance != null) {
            UserReview review = new UserReview(user, hikeInstance, reviewArea.getText());
            review.updateReviews();
        } else if(hikeMultiDayInstance != null) {
            UserReview review = new UserReview(user, hikeMultiDayInstance, reviewArea.getText());
            review.updateReviews();
        }
    }


    /**
     *  Method to take a break during the hike
     * @param event     ActionEvent
     */
    public void takeBreak(ActionEvent event) {
        semaphore.tryAcquire();
        breakText.setText("Taking break");

    }

    /**
     * Method to resume the hike after taking a break
     * @param event    ActionEvent
     */
    public void resumeHike(ActionEvent event){
        semaphore.release();
        breakText.setText("Continuing the hike");
    }

    /**
     * Method to switch to the main scene
     * @param event    ActionEvent
     */
    public void switchToMainScene(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainScene.fxml"));
            root = loader.load();

            scene = new Scene(root);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            // String css = this.getClass().getResource("../view/gui/Gui_Interface.css").toExternalForm();

            // scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();


        } catch(Exception e) {

        }
    }

    /**
     * Method to switch to the login scene
     * @param event     ActionEvent
     */
    public void switchToLoginScene(ActionEvent event){
        try{
            root = FXMLLoader.load(getClass().getResource("/gui/LoginScene.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        } catch(Exception e) {

        }
    }

    /**
     * Method to switch to the map scene
     * @param event     ActionEvent
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
     * Method to map the weather of the hike
     * @param location      String
     */
    public void mapWeather(String location) {
//         String replacment = "\\d*\\w-\\d*\\w-\\d*\\s";
        //todo: Transfer location from chosen hike route
        System.out.println(String.format("%s", location));
        String[] loc = location.split(" ");
        System.out.println(String.format("%s", loc[0]));
        String formattedLocation = loc[0].concat("%20").concat(loc[1]);
        JsonObject weather = wApi.getJsonWeather(String.format("%s", formattedLocation));
        JsonObject weatherNow = (JsonObject) weather.get("current").getAsJsonObject();
        String[] regexRes =  (weatherNow.getAsJsonObject("condition").get("icon").getAsString()).split("/");
        tempNow.setText(weatherNow.get("temp_c").getAsString());

        Image img = new Image(getClass().getResourceAsStream(String.format("/icons/%s", regexRes[6])));

        weatherIcon.setImage(img);
    }



}
