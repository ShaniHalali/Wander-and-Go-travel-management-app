package com.example.myapplication.Models;

import java.util.HashMap;

public class TripsPlanes {
    private String destination ="";
    private HashMap<String, Plan> allTrips = new HashMap<>();

    public TripsPlanes() {
    }

    public String getDestination() {
        return destination;
    }

    public TripsPlanes setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public HashMap<String, Plan> getAllTrips() {
        return allTrips;
    }

    public TripsPlanes setAllTrips(HashMap<String, Plan> allTrips) {
        this.allTrips = allTrips;
        return this;
    }

    @Override
    public String toString() {
        return "TripsPlanes{" +
                "destination='" + destination + '\'' +
                ", allTrips=" + allTrips +
                '}';
    }
}
