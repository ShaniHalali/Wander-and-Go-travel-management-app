package com.example.myapplication.Data;

import com.example.myapplication.Models.DailySchedule;
import com.example.myapplication.Models.Trip;

public class DataManager {
    private static int tripCount=1;

    public static Trip createTripsWithDailySchedules(){
        Trip trip = new Trip();

        trip.getAllDays()
                .put("day "+ tripCount,
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

    public static DailySchedule NewDailyDay(String nextDay){
        DailySchedule dailySchedule = new DailySchedule();

        dailySchedule
                .setDayTitle(nextDay)
                .setDayNumber( getCurrentTripCount())
                .setMorningSchedule("")
                .setNoonSchedule("")
                .setEveningSchedule("")
                .setReservations("")
                .setNotes("")
                .setDailySchedule("");


        return dailySchedule;
    }


    public static int getCurrentTripCount() {
        return tripCount;
    }
    public static int getNextTripCount() {
        tripCount++;
        return tripCount;
    }


}