package com.example.smartparkingsystem;

public class User {

    private String firstName, lastName, phoneNum, icNum, carNumber, carPlate, carPlate2, carPlate3, carPlate4, carPlate5, username;

    public User(String firstName, String lastName, String phoneNum, String icNum, String carNumber,
                String carPlate, String carPlate2, String carPlate3, String carPlate4, String carPlate5,
                String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.icNum = icNum;
        this.carNumber = carNumber;
        this.carPlate2 = carPlate2;
        this.carPlate3 = carPlate3;
        this.carPlate4 = carPlate4;
        this.carPlate5 = carPlate5;
        this.carPlate = carPlate;
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIcNum() {
        return icNum;
    }

    public void setIcNum(String icNum) {
        this.icNum = icNum;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarPlate2() {
        return carPlate2;
    }

    public void setCarPlate2(String carPlate2) {
        this.carPlate2 = carPlate2;
    }

    public String getCarPlate3() {
        return carPlate3;
    }

    public void setCarPlate3(String carPlate3) {
        this.carPlate3 = carPlate3;
    }

    public String getCarPlate4() {
        return carPlate4;
    }

    public void setCarPlate4(String carPlate4) {
        this.carPlate4 = carPlate4;
    }

    public String getCarPlate5() {
        return carPlate5;
    }

    public void setCarPlate5(String carPlate5) {
        this.carPlate5 = carPlate5;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
