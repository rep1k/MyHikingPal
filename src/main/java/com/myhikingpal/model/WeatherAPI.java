package com.myhikingpal.model;

import java.net.*;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Class for the weather API
 */
public class WeatherAPI {

    /**
     * Method for getting the weather data from the API
     * @param city city for which the weather data is requested
     * @return returns the weather data in JSON format
     */
    public JsonObject getJsonWeather(String city) {
        try {
            
            URL url = new URL(String.format("http://api.weatherapi.com/v1/forecast.json?key=8409bf0c373c45b3b6b174106233103&q=%s&days=1&aqi=no&alerts=no", city.toLowerCase())); // Replace with your API endpoint
            System.out.printf("(-) URL DEBUG >> %s \n\n", url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET"); // Replace with the appropriate HTTP method
            con.connect();
            int status = con.getResponseCode(); // Check the response code to handle errors
            if(status != 200){
                throw new RuntimeException("HttpResponseCode: " + status);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()){
                    inline += scanner.nextLine();
                    // System.out.println(inline);
                }

                scanner.close();

                // Parse the JSON response using Gson

                JsonObject responseJson = JsonParser.parseString(inline).getAsJsonObject();
            
                // Access the response fields as needed
                // JsonObject obj = (JsonObject) responseJson.get("location");
                //   int field2 = responseJson.get("field2").getAsInt();
                return responseJson;
                // Process the response data as needed
                // System.out.println("field1: " + obj.get("name"));
                //   System.out.println("field2: " + field2);
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
      }

}
