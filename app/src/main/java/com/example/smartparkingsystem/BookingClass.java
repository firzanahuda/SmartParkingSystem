package com.example.smartparkingsystem;

public class BookingClass {

    private String textInputCarPlate, textInputVehicle, textInputStart, textInputEnd, duration, station, textInputStartTime, textInputEndTime;

    private static final BookingClass instance = new BookingClass();

    public BookingClass() {
        super();
    }


    public BookingClass(String textInputStart, String textInputEnd, String textInputCarPlate, String textInputVehicle, String textInputStartTime,
                        String textInputEndTime, String duration){
        this.textInputCarPlate = textInputCarPlate;
        this.textInputStart = textInputStart;
        this.textInputEnd = textInputEnd;
        this.duration = duration;
        this.textInputStartTime = textInputStartTime;
        this.textInputEndTime = textInputEndTime;
        this.textInputVehicle = textInputVehicle;
    }

    public BookingClass(String textInputStart, String textInputEnd, String textInputCarPlate, String duration){
        this.textInputCarPlate = textInputCarPlate;
        this.textInputStart = textInputStart;
        this.textInputEnd = textInputEnd;
        this.duration = duration;
    }

    public BookingClass(String textInputCarPlate, String textInputVehicle, String textInputStart, String textInputEnd, String duration){

        this.textInputCarPlate = textInputCarPlate;
        this.textInputVehicle = textInputVehicle;
        this.textInputStart = textInputStart;
        this.textInputEnd = textInputEnd;
        this.duration = duration;
    }

    public static BookingClass getInstance() {
        return instance;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
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
}
