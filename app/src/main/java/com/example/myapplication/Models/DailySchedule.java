package com.example.myapplication.Models;

public class DailySchedule {
    private String dayTitle ="";
    private int dayNumber=0;
    private String morningSchedule ="";
    private String noonSchedule ="";
    private String eveningSchedule ="";
    private String reservations="";
    private String notes ="";


    public DailySchedule() {
    }

    public String getDayTitle() {
        return dayTitle;
    }

    public DailySchedule setDayTitle(String dayTitle) {
        this.dayTitle = dayTitle;
        return this;
    }

    public String getDailySchedule() {
        return morningSchedule;
    }

    public DailySchedule setDailySchedule(String dailySchedule) {
        this.morningSchedule = dailySchedule;
        return this;
    }

    public String getReservations() {
        return reservations;
    }

    public DailySchedule setReservations(String reservations) {
        this.reservations = reservations;
        return this;
    }


    public String getMorningSchedule() {
        return morningSchedule;
    }

    public DailySchedule setMorningSchedule(String morningSchedule) {
        this.morningSchedule = morningSchedule;
        return this;
    }

    public String getNoonSchedule() {
        return noonSchedule;
    }

    public DailySchedule setNoonSchedule(String noonSchedule) {
        this.noonSchedule = noonSchedule;
        return this;
    }

    public String getEveningSchedule() {
        return eveningSchedule;
    }

    public DailySchedule setEveningSchedule(String eveningSchedule) {
        this.eveningSchedule = eveningSchedule;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public DailySchedule setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public DailySchedule setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
        return this;
    }

    @Override
    public String toString() {
        return "DailySchedule{" +
                "dayTitle='" + dayTitle + '\'' +
                ", dayNumber=" + dayNumber +
                ", morningSchedule='" + morningSchedule + '\'' +
                ", noonSchedule='" + noonSchedule + '\'' +
                ", eveningSchedule='" + eveningSchedule + '\'' +
                ", reservations='" + reservations + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
