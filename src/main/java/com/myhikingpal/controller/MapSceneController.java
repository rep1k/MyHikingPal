package com.myhikingpal.controller;

import com.myhikingpal.model.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javafx.scene.web.WebView;
import javafx.util.Duration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


import com.google.gson.JsonObject;





/**
 * Class for the map scene controller
 */
public class MapSceneController {

    /**
     * Exception class for when nothing is selected
     */
    static class NothingSelectedException extends Exception {
        public NothingSelectedException(String message){
            super(message);
        }
    }
   
    @FXML 
    WebView mapView;
    @FXML
    ImageView map;
    @FXML
    Text diff;
    @FXML
    Text timeText;
    @FXML
    ChoiceBox<String> routeSelect;
    @FXML
    Text routeTime;
    @FXML
    Text routeDist;
    @FXML
    Text routeDays;
    @FXML
    Text routeWeather;
    @FXML
    ImageView weatherIcon;
    @FXML
    RadioButton carToToggle;
    @FXML
    RadioButton carFromToggle;
    @FXML
    RadioButton busToToggle;
    @FXML
    RadioButton busFromToggle;
    @FXML
    RadioButton trainToToggle;
    @FXML
    RadioButton trainFromToggle;
    @FXML
    RadioButton taxiToToggle;
    @FXML
    RadioButton taxiFromToggle;
    @FXML
    Text totalMoney;
    @FXML
    Text hikeError;


    private Stage stage;
    private Scene scene;
    private Parent root;
    private final WeatherAPI wApi = new WeatherAPI();
    private String routeN;
    private Connection connection;
    private User user;
    HikingRoute route = new HikingRoute();
    HikingDayRoute dayRoute;
    HikingMultiDayRoute multiDayRoute;

    // private Timeline timeline;

    /**
     * Method that initializes the Map Scene
     */
    @FXML
    public void initialize(){
        try{
            connection = DbConnectionUser.getInstance().getConnection();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.9), evented -> {
                LocalTime time = LocalTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
                String formattedtime = time.format(myFormatObj);
                timeText.setText(formattedtime);
            }));
            user = User.getInstance();
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
            for (String locationString : route.getInitRouteNames()) {
                routeSelect.getItems().add(locationString);
            }
            // LAMBDA FUNCTION FOR RADIO BUTTONS
            ChangeListener<Boolean> radioButtonListener = (observable, oldValue, newValue) -> {
                if (newValue) {
                    transportOptions();
                }
            };

            carToToggle.selectedProperty().addListener(radioButtonListener);
            carFromToggle.selectedProperty().addListener(radioButtonListener);
            busToToggle.selectedProperty().addListener(radioButtonListener);
            busFromToggle.selectedProperty().addListener(radioButtonListener);
            trainToToggle.selectedProperty().addListener(radioButtonListener);
            trainFromToggle.selectedProperty().addListener(radioButtonListener);
            taxiToToggle.selectedProperty().addListener(radioButtonListener);
            taxiFromToggle.selectedProperty().addListener(radioButtonListener);
            routeSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                  
                    System.out.println("New Value: " + newValue);
                    routeN = newValue;
                    String[] splittedRouteName = newValue.split("-");
                    System.out.printf("from %s to %s\n",splittedRouteName[0], splittedRouteName[1]);
                    route.getRouteInfo(splittedRouteName[0], splittedRouteName[1]);
                    String mapPath = getClass().getResource(route.getMap()).toExternalForm();
                    Image mapImage = new Image(mapPath);
                    map.setImage(mapImage);
                    diff.setText(route.getRouteDiff());
                    routeDist.setText(String.format("%.2f km", route.getRouteDistance()));
                    routeDays.setText(String.format("%d Day/s", route.getMultidayInfo()));
                    routeTime.setText(String.format("%s", route.getRouteTime()));
                    mapWeather(splittedRouteName[0].trim());
                    

                }
            });
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * method that calculates the price of selected transport
     */
    public void transportOptions() {
        Random rand = new Random();
        if (carToToggle.isSelected()) {
            Transport.getInstance().setTransportTo("car", 1 + rand.nextInt(3), 20 + rand.nextFloat() * (7));
        }
        if (carFromToggle.isSelected()) {
            Transport.getInstance().setTransportFrom("car", rand.nextInt(3), 20 + rand.nextFloat() * (7));
        }
        if (busToToggle.isSelected()) {
            Transport.getInstance().setTransportTo("bus", 1 + rand.nextInt(5), 9 + rand.nextFloat() * (5));
        }
        if (busFromToggle.isSelected()) {
            Transport.getInstance().setTransportFrom("bus", 1 + rand.nextInt(6), 9 + rand.nextFloat() * (5));
        }
        if (trainToToggle.isSelected()) {
            Transport.getInstance().setTransportTo("train", 1 + rand.nextInt(4), 14 + rand.nextFloat() * (6));
        }
        if (trainFromToggle.isSelected()) {
            Transport.getInstance().setTransportFrom("train", 1 + rand.nextInt(4), 14 + rand.nextFloat() * (6));
        }
        if (taxiToToggle.isSelected()) {
            Transport.getInstance().setTransportTo("taxi", 1 + rand.nextInt(3), 25 + rand.nextFloat() * (5));
        }
        if (taxiFromToggle.isSelected()) {
            Transport.getInstance().setTransportFrom("taxi", 1 + rand.nextInt(3), 25 + rand.nextFloat() * (5));
        }

        if(Transport.getInstance().isNull()){
            totalMoney.setText("0.00");
        } else {
            totalMoney.setText(String.format("$%.2f", Transport.getInstance().getTotalCost()));
        }
    }

    /**
     * Method that starts the hike
     * @param event     ActionEvent
     * @throws NothingSelectedException  (My own Exception)  Exception for when not everything is selected
     */
    public void startHikeBttn(ActionEvent event) throws NothingSelectedException {
        if (Transport.getInstance().isNull() && routeSelect.getSelectionModel().isEmpty()) {
            hikeError.setText("Please select a transport option and a route");
            throw new NothingSelectedException("Not everything is selected");
//            System.out.println("No Transport and Route Selected");
        } else if (Transport.getInstance().isNull()) {
//            System.out.println("No Transport Selected");
            hikeError.setText("Please select a transport option");
            throw new NothingSelectedException("Not everything is selected");

//
        } else if (routeSelect.getSelectionModel().isEmpty()) {
//            System.out.println("No Route Selected");
            hikeError.setText("Please select a route");
            throw new NothingSelectedException("Not everything is selected");
        } else {
            hikeError.setText("");
            if(route.getMultidayInfo() == 1) {
               dayRoute = HikingDayRoute.getInstance(route.getStartLocation(),
                        route.getEndLocation(),
                        route.getRouteTime(),
                        route.getRouteDistance(),
                        route.getRouteDiff(),
                        route.getMap(),
                        route.getMultidayInfo());
            } else {
                multiDayRoute = HikingMultiDayRoute.getInstance(route.getStartLocation(),
                        route.getEndLocation(),
                        route.getRouteTime(),
                        route.getRouteDistance(),
                        route.getRouteDiff(),
                        route.getMap(),
                        route.getMultidayInfo());
            }
            Transport.getInstance().printPrint();
//            User user = User.getInstance();
            user.updateDone_routes(routeN);
            user.updateExpenses(routeN ,totalMoney.getText());
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/currentProgressScene.fxml"));
                root = loader.load();

                scene = new Scene(root);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                // String css = this.getClass().getResource("../view/gui/Gui_Interface.css").toExternalForm();

                // scene.getStylesheets().add(css);
                stage.setScene(scene);
                stage.show();


            } catch(Exception e) {
                System.out.println(e);
            }

        }
    }


    /**
     * Method that gets the weather for the route
     * @param location  String
     */
    public void mapWeather(String location) {

        // String replacment = "\\d*\\w-\\d*\\w-\\d*\\s";
        System.out.println(String.format("%s", location));
        String[] loc = location.split(" ");
        System.out.println(String.format("%s", loc[0]));
        String formattedLocation = loc[0].concat("%20").concat(loc[1]);
        JsonObject weather = wApi.getJsonWeather(String.format("%s", formattedLocation));
        JsonObject weatherNow = (JsonObject) weather.get("current").getAsJsonObject();
        String[] regexRes =  (weatherNow.getAsJsonObject("condition").get("icon").getAsString()).split("/");
        routeWeather.setText(weatherNow.get("temp_c").getAsString());

        Image img = new Image(getClass().getResourceAsStream(String.format("/icons/%s", regexRes[6])));

        weatherIcon.setImage(img);
    }

    /**
     *  Method to switch to the login scene
     * @param event    ActionEvent
     */

    public void switchToLoginScene(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginScene.fxml"));
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
     * Method that switches to the main scene
     * @param event     ActionEvent
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
     * Method that switches to the current progress scene
     * @param event     ActionEvent
     */
    public void switchToCurrentProgress(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/currentProgressScene.fxml"));
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
     * Method that switches to the review scene
     * @param event    ActionEvent
     */
    public void switchToReviewScene(ActionEvent event){
        try{
            root = FXMLLoader.load(getClass().getResource("/gui/ReviewScene.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
