package com.example.myapplication.Models;

import java.util.HashMap;

public class Trips {
    private String name="";
    private HashMap<String, DailySchedule> allDays = new HashMap<>();

    public Trips() {
    }

    public String getName() {
        return name;
    }

    public Trips setName(String name) {
        this.name = name;
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
        return "Trips{" +
                "name='" + name + '\'' +
                ", allDays=" + allDays +
                '}';
    }
}
