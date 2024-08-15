package com.example.myapplication.Models;

import java.util.HashMap;

public class Trips {
    private String TripDestination ="";
    private HashMap<String, DailySchedule> allDays = new HashMap<>();

    public Trips() {
    }

    public String getTripDestination() {
        return TripDestination;
    }

    public Trips setTripDestination(String tripDestination) {
        this.TripDestination = tripDestination;
        return this;
    }

    public HashMap<String, DailySchedule> getAllDays() {
        return allDays;
    }

    public Trips setAllDays(HashMap<String, DailySchedule> allDays) {
        this.allDays = allDays;
        return this;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "TripDestination='" + TripDestination + '\'' +
                ", allDays=" + allDays +
                '}';
    }
}
