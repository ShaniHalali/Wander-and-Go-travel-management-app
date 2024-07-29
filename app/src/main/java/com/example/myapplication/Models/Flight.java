package com.example.myapplication.Models;

public class Flight {
    private String flightNumber;
    public enum Airline{
        ELAL,
        ARKIA,
        ISRAIR,
        BRITISHAIRWAYS,
        AMERICANAIRLINES,

    }
    public enum BaggageAllowance{
        CarryOnBag,
        StandardCheckedBag,
        PersonalItem,

    }
    private String departureLocation="";
    private String arrivalLocation="";
    private String departureTime="";
    private String ArrivalTime="";
    private Airline airline=Airline.ELAL;
    private BaggageAllowance baggage=BaggageAllowance.CarryOnBag;
    private Boolean isDelayed =Boolean.FALSE;

    public Flight() {
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Flight setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
        return this;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public Flight setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
        return this;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public Flight setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
        return this;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public Flight setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
        return this;
    }

    public String getArrivalTime() {
        return ArrivalTime;
    }

    public Flight setArrivalTime(String arrivalTime) {
        ArrivalTime = arrivalTime;
        return this;
    }

    public Airline getAirline() {
        return airline;
    }

    public Flight setAirline(Airline airline) {
        this.airline = airline;
        return this;
    }

    public BaggageAllowance getBaggage() {
        return baggage;
    }

    public Flight setBaggage(BaggageAllowance baggage) {
        this.baggage = baggage;
        return this;
    }

    public Boolean getDelayed() {
        return isDelayed;
    }

    public Flight setDelayed(Boolean delayed) {
        isDelayed = delayed;
        return this;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", departureLocation='" + departureLocation + '\'' +
                ", arrivalLocation='" + arrivalLocation + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", ArrivalTime='" + ArrivalTime + '\'' +
                ", airline=" + airline +
                ", baggage=" + baggage +
                ", isDelayed=" + isDelayed +
                '}';
    }
}
