package com.example.smartparkingsystem;

public class BookingClass {

    private String textInputCarPlate, textInputVehicle, textInputStart, textInputEnd, duration;

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
}
