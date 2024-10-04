package com.myhikingpal.model;

import java.sql.SQLException;

/**
 * Interface for printing the debug messages
 *  Transport class for the transport methods
 */

public class Transport implements DebugPrint{

    private String typeTo;
    private String typeFrom;
    private int timeTo;
    private int timeFrom;
    private float priceTo;
    private float priceFrom;

    private static Transport instance;

    /**
     * Constructor for the transport class
     */
    public static Transport getInstance() {
        if (instance == null) {
            instance = new Transport();
        }
        return instance;
    }

    /**
     * Setting the transport method to destination
     * @param type type of transport
     * @param time time that it will take to get to the destination
     * @param price price that is calculated for the destination
     */
    public void setTransportTo(String type, int time, float price) {
        this.typeTo = type;
        this.timeTo = time;
        this.priceTo = price;
    }

    /**
     * Setting the transport method back from destination
     * @param type type of transport
     * @param time time that it will take to get to the destination
     * @param price price that is calculated for the destination
     */
    public void setTransportFrom(String type, int time, float price) {
        this.typeFrom = type;
        this.timeFrom = time;
        this.priceFrom = price;
    }

    /**
     * Method for printing the debug messages
     */
    DebugPrint printDebug = (typeTo, timeTo, priceTo, typeFrom, timeFrom, priceFrom) -> {
        System.out.println("Transport type to: " + typeTo);
        System.out.println("Transport time to: " + timeTo);
        System.out.println("Transport price to: " + priceTo);
        System.out.println("Transport type to: " + typeFrom);
        System.out.println("Transport time to: " + timeFrom);
        System.out.println("Transport price to: " + priceFrom);
    };

    /**
     *  Method for controlling if the transport is null
     * @return returns true if the transport is null
     */

    public boolean isNull() {
        return typeFrom == null || priceFrom == 0.0 || timeFrom == 0 || typeTo == null || priceTo == 0.0 || timeTo == 0;
    }

    /**
     * Method for getting the total cost of the transport
     * @return returns the total cost of the transport
     */
    public float getTotalCost(){
        return priceFrom + priceTo;
    }

    /**
     * Method for printing the debug messages
     * @param typeTo   type of the route to
     * @param timeTo  time of the route to
     * @param priceTo price of the route to
     * @param typeFrom type of the route from
     * @param timeFrom time of the route from
     * @param priceFrom price of the route from
     */
    @Override
    public void print(String typeTo, String timeTo, String priceTo, String typeFrom, String timeFrom, String priceFrom) {
        System.out.println("Transport type to: " + typeTo);
        System.out.println("Transport time to: " + timeTo);
        System.out.println("Transport price to: " + priceTo);
        System.out.println("Transport type to: " + typeFrom);
        System.out.println("Transport time to: " + timeFrom);
        System.out.println("Transport price to: " + priceFrom);
    }

    /**
     * Method for printing the debug messages
     */

    public void printPrint(){
        System.out.println("Transport type to: " + typeTo);
        System.out.println("Transport time to: " + timeTo);
        System.out.println("Transport price to: " + priceTo);
        System.out.println("Transport type to: " + typeFrom);
        System.out.println("Transport time to: " + timeFrom);
        System.out.println("Transport price to: " + priceFrom);
    }
}
