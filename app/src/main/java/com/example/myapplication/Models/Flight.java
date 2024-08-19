package com.example.myapplication.Models;
public class Flight {
    private String flightTitle;
    private String flightNumber;
    private String departureAirport;
    private String departureTime;
    private String departureDate;
    private String arrivalDate;
    private String arrivalAirport;
    private String arrivalTime;
    private String flightDescription; // New field

    public Flight() {
        // Default constructor required for Firebase
    }

    public Flight(String flightTitle, String flightNumber, String departureAirport,
                  String departureTime, String departureDate, String arrivalDate,
                  String arrivalAirport, String arrivalTime, String flightDescription) {
        this.flightTitle = flightTitle;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.departureTime = departureTime;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = arrivalTime;
        this.flightDescription = flightDescription; // Initialize new field
    }

    // Getters and setters for all fields

    public String getFlightTitle() {
        return flightTitle;
    }

    public void setFlightTitle(String flightTitle) {
        this.flightTitle = flightTitle;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getFlightDescription() {
        return flightDescription;
    }

    public void setFlightDescription(String flightDescription) {
        this.flightDescription = flightDescription;
    }
}

