package com.example.smartparkingsystem;

public class HistoryClass {

    private String startTime, endTime, duration, startDate, endDate, station, carPlate;

    private static final HistoryClass instance = new HistoryClass();

    public static HistoryClass getInstance(){
        return instance;

    }

    public HistoryClass() {
        super();
    }

    public HistoryClass(String startTime, String endTime, String duration, String startDate, String endDate, String station, String carPlate) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.station = station;
        this.carPlate = carPlate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }
}
