package com.example.myapplication.Models;

import java.util.HashMap;

public class TripsPlanes {
    private String name="";
    private HashMap<String, Plan> allTrips = new HashMap<>();

    public TripsPlanes() {
    }

    public String getName() {
        return name;
    }

    public TripsPlanes setName(String name) {
        this.name = name;
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
                "name='" + name + '\'' +
                ", allTrips=" + allTrips +
                '}';
    }
}
