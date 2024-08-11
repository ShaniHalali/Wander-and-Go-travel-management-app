package com.example.myapplication.Data;

import com.example.myapplication.Models.DailySchedule;
import com.example.myapplication.Models.Trip;

public class DataManager {
    private static int tripCount=0;

    public static Trip createTripsWithDailySchedules(){
        Trip trip = new Trip();

        trip.getAllDays()
                .put("France day "+ getNextTripCount(),
                        new DailySchedule()
                                .setDayTitle("Day "+ tripCount)
                                .setDayNumber( getCurrentTripCount())
                                .setMorningSchedule("")
                                .setNoonSchedule("")
                                .setEveningSchedule("")
                                .setReservations("")
                                .setNotes("")
                                .setDailySchedule("")

                                ) ;


        return trip;
    }

    public static int getCurrentTripCount() {
        return tripCount;
    }
    public static int getNextTripCount() {
        tripCount++;
        return tripCount;
    }


}
