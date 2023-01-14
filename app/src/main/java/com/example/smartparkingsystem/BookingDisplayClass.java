package com.example.smartparkingsystem;

public class BookingDisplayClass {

    private String textInputCarPlate, textInputVehicle, textInputStart, textInputEnd, textInputStartTime, textInputEndTime, station;

    private long duration;

    private static final BookingDisplayClass instance = new BookingDisplayClass();

    public static BookingDisplayClass getInstance(){
        return instance;

    }

    public BookingDisplayClass() {
        super();
    }

    public BookingDisplayClass(String textInputCarPlate, String textInputStart, String textInputEnd, String textInputStartTime, String textInputEndTime, String textInputVehicle, String station) {
        this.textInputCarPlate = textInputCarPlate;
        this.textInputVehicle = textInputVehicle;
        this.textInputStart = textInputStart;
        this.textInputEnd = textInputEnd;
        this.textInputStartTime = textInputStartTime;
        this.textInputEndTime = textInputEndTime;
        this.station = station;
    }

    public String getTextInputCarPlate() {
        return textInputCarPlate;
    }

    public void setTextInputCarPlate(String textInputCarPlate) {
        this.textInputCarPlate = textInputCarPlate;
    }

    public String getTextInputVehicle() {
        return textInputVehicle;
    }

    public void setTextInputVehicle(String textInputVehicle) {
        this.textInputVehicle = textInputVehicle;
    }

    public String getTextInputStart() {
        return textInputStart;
    }

    public void setTextInputStart(String textInputStart) {
        this.textInputStart = textInputStart;
    }

    public String getTextInputEnd() {
        return textInputEnd;
    }

    public void setTextInputEnd(String textInputEnd) {
        this.textInputEnd = textInputEnd;
    }


    public String getTextInputStartTime() {
        return textInputStartTime;
    }

    public void setTextInputStartTime(String textInputStartTime) {
        this.textInputStartTime = textInputStartTime;
    }

    public String getTextInputEndTime() {
        return textInputEndTime;
    }

    public void setTextInputEndTime(String textInputEndTime) {
        this.textInputEndTime = textInputEndTime;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
