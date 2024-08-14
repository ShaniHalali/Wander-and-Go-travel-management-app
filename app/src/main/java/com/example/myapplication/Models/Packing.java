package com.example.myapplication.Models;


import java.util.HashMap;

public class Packing {
    private String Packing = "";
    private HashMap<String, PackingItems> allItems = new HashMap<>();

    public Packing() {
    }

    public HashMap<String, PackingItems> getAllItems() {
        return allItems;
    }

    public Packing setAllItems(HashMap<String, DailySchedule> allDays) {
        this.allItems = allItems;
        return this;
    }

    @Override
    public String toString() {
        return "Packing{" +
                "Packing='" + Packing + '\'' +
                ", allItems=" + allItems +
                '}';
    }
}