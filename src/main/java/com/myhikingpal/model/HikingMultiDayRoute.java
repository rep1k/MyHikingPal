package com.myhikingpal.model;

/**
 * Class for the Hiking Multi Day Route
 */
public class HikingMultiDayRoute extends HikingDayRoute{
    private static HikingMultiDayRoute instance = null;

    /**
     *  Constructor for the Hiking Multi Day Route
     * @param startLocation starting location of the hike
     * @param endLocation end location of the hike
     * @param Time time of the hike
     * @param distance distance of the hike
     * @param difficulty difficulty of the hike
     * @param map map of the hike
     * @param Multiday if the hike is multi-day of the hike
     */
    private HikingMultiDayRoute(String startLocation, String endLocation, String Time,
                           float distance, String difficulty, String map, int Multiday){
        super(startLocation, endLocation, Time, distance, difficulty, map, Multiday);
    }

    /**
     * Method for getting the singleton instance of the Multi-day Hike route
     * @param startLocation starting location of the hike
     * @param endLocation end location of the hike
     * @param Time time of the hike
     * @param distance distance of the hike
     * @param difficulty difficulty of the hike
     * @param map map of the hike
     * @param Multiday if the hike is multi-day of the hike
     * @return returns the info of Hike Route
     */
    public static HikingMultiDayRoute getInstance(String startLocation, String endLocation, String Time,
                                      float distance, String difficulty, String map, int Multiday){
        if(instance == null){
            instance = new HikingMultiDayRoute(startLocation, endLocation, Time, distance, difficulty, map, Multiday);
        }
        return instance;
    }

    /**
     *  Method used to retrieve hike info from database
     * @return returns the info of Hike Route
     */
    public static HikingDayRoute getInstance(){

        return instance;
    }
}
