package com.example.myapplication.Models;

import java.util.HashMap;

public class Trip {
    private String TripDestination ="";
    private HashMap<String, DailySchedule> allDays = new HashMap<>();

    public Trip() {
    }

    public String getTripDestination() {
        return TripDestination;
    }

    public Trip setTripDestination(String tripDestination) {
        this.TripDestination = tripDestination;
        return this;
    }

    public HashMap<String, DailySchedule> getAllDays() {
        return allDays;
    }

    public Trip setAllDays(HashMap<String, DailySchedule> allDays) {
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
