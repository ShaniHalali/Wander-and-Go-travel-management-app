package com.example.myapplication.Data;

import com.example.myapplication.Models.DailySchedule;
import com.example.myapplication.Models.Trips;

public class DataManager {

    public static Trips creatrTripsWithDailySchedules(){
        Trips trips = new Trips();

        trips.getAllDays()
                .put("France day 1",
                        new DailySchedule()
                                .setTitle("Day 1")
                                .setDailySchedule("9:00 AM - Breakfast at Cafe /n Enjoy a delicious breakfast at the local cafe.")
                                .setReservations("local cafe - cafe de flore")
                                ) ;
        trips.getAllDays()
                .put("France day 2",
                        new DailySchedule()
                                .setTitle("Day 2")
                                .setDailySchedule("11:00 AM - Visit Museum /n Explore the art and history of fashion at the Dior museum.")
                                .setReservations("Dior Museum - La Galerie Dior")
                ) ;
        trips.getAllDays()
                .put("France day 3",
                        new DailySchedule()
                                .setTitle("Day 3")
                                .setDailySchedule("11:00 AM - Visit Museum /n Explore the art and history of fashion at the Dior museum.")
                                .setReservations("Dior Museum - La Galerie Dior")
                ) ;


        return trips;
    }



}
