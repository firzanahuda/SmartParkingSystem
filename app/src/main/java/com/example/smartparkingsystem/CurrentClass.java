package com.example.smartparkingsystem;

public class CurrentClass {

    private String starting_Date, end_Date, start_Time, end_Time, station;

    public CurrentClass(String starting_date, String end_date, String start_time, String end_time, String station) {
        this.starting_Date = starting_date;
        this.end_Date = end_date;
        this.start_Time = start_time;
        this.end_Time = end_time;
        this.station = station;
    }



    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStarting_Date() {
        return starting_Date;
    }

    public void setStarting_Date(String starting_Date) {
        this.starting_Date = starting_Date;
    }

    public String getEnd_Date() {
        return end_Date;
    }

    public void setEnd_Date(String end_Date) {
        this.end_Date = end_Date;
    }

    public String getStart_Time() {
        return start_Time;
    }

    public void setStart_Time(String start_Time) {
        this.start_Time = start_Time;
    }

    public String getEnd_Time() {
        return end_Time;
    }

    public void setEnd_Time(String end_Time) {
        this.end_Time = end_Time;
    }
}
