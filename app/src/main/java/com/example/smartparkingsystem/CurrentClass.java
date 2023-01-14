package com.example.smartparkingsystem;

public class CurrentClass {

    private String station, duration;

    public CurrentClass(String station, String duration) {
        this.station = station;
        this.duration = duration;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
