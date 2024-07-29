package com.example.myapplication.Models;

import java.util.ArrayList;

public class DailySchedule {
    private String title="";
    private String schedule="";
    private String reservations="";
    private ArrayList<String> packingList;

    public DailySchedule() {
    }

    public String getTitle() {
        return title;
    }

    public DailySchedule setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDailySchedule() {
        return schedule;
    }

    public DailySchedule setDailySchedule(String dailySchedule) {
        this.schedule = dailySchedule;
        return this;
    }

    public String getReservations() {
        return reservations;
    }

    public DailySchedule setReservations(String reservations) {
        this.reservations = reservations;
        return this;
    }

    public ArrayList<String> getPackingList() {
        return packingList;
    }

    public DailySchedule setPackingList(ArrayList<String> packingList) {
        this.packingList = packingList;
        return this;
    }

    @Override
    public String toString() {
        return "TripSchedule{" +
                "title='" + title + '\'' +
                ", dailySchedule='" + schedule + '\'' +
                ", reservations='" + reservations + '\'' +
                ", packingList=" + packingList +
                '}';
    }
}
