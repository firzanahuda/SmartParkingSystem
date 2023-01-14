package com.example.smartparkingsystem;

public class RetrieveClass {

    private String startDate, endDate, vehicleType, startTime, endTime, parkingSlot, penalty, carPlate;

    public RetrieveClass(String startDate, String endDate, String vehicleType, String startTime, String endTime, String carPlate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.vehicleType = vehicleType;
        this.startTime = startTime;
        this.endTime = endTime;
       // this.parkingSlot = parkingSlot;
        this.carPlate = carPlate;
        //this.penalty = penalty;
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

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(String parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }
}
