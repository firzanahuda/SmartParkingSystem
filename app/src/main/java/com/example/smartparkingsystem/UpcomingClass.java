package com.example.smartparkingsystem;

public class UpcomingClass {

    private String startDate, endDate, duration, startTime, endTime, station, carPlate, qrCode, saveQRCode;

    private static final UpcomingClass instance = new UpcomingClass();

    public static UpcomingClass getInstance(){
        return instance;

    }

    public UpcomingClass() {
        super();
    }

    public UpcomingClass(String startDate, String endDate, String duration, String startTime, String endTime, String station, String carPlate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.station = station;
        this.carPlate = carPlate;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getSaveQRCode() {
        return saveQRCode;
    }

    public void setSaveQRCode(String saveQRCode) {
        this.saveQRCode = saveQRCode;
    }
}
