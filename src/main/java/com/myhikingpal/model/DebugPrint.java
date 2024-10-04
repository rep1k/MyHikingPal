package com.myhikingpal.model;

/**
 * Interface for printing the debug messages
 */
public interface DebugPrint {
    /**
     * Method for printing the debug messages
     * @param typeTo   type of the route to
     * @param timeTo  time of the route to
     * @param priceTo price of the route to
     * @param typeFrom type of the route from
     * @param timeFrom time of the route from
     * @param priceFrom price of the route from
     */
    void print(String typeTo, String timeTo, String priceTo, String typeFrom, String timeFrom, String priceFrom);

    /**
     * default Method for printing the debug messages
     * @param typeTo  type of the route to
     * @param timeTo time of the route to
     * @param priceTo price of the route to
     * @param typeFrom type of the route from
     * @param timeFrom  time of the route from
     * @param priceFrom price of the route from
     */
    default void printPrint(String typeTo, String timeTo, String priceTo, String typeFrom, String timeFrom, String priceFrom){
        print(typeTo, timeTo, priceTo, typeFrom, timeFrom, priceFrom);
    }
}
