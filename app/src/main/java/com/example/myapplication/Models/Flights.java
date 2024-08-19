package com.example.myapplication.Models;

import java.util.HashMap;

public class Flights {
    private HashMap<String, Flight> allFlights = new HashMap<>();

    public Flights() {
    }

    public HashMap<String, Flight> getAllFlights() {
        return allFlights;
    }

    public Flights setAllFlights(HashMap<String, Flight> allFlights) {
        this.allFlights = allFlights;
        return this;
    }

    @Override
    public String toString() {
        return "Flights{" +
                "allFlights=" + allFlights +
                '}';
    }
}
