package com.example.animalcare.models;

import java.io.Serializable;
import java.util.List;

public class Visit implements Serializable {
    private String visitID;
    private String adopterUsername;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minutes;
    private List<String> savedAnimalsID;
    private String status;
    public static String STATUS_ON = "ON";
    public static String STATUS_OFF = "OFF";

    public Visit() {
    }

    public Visit(String visitID, String adopterUsername, int day, int month, int year, int hour, int minutes, List<String> savedAnimalsID) {
        this.visitID = visitID;
        this.adopterUsername = adopterUsername;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minutes = minutes;
        this.savedAnimalsID = savedAnimalsID;
        this.status = STATUS_ON;
    }

    public String getVisitID() {
        return visitID;
    }

    public void setVisitID(String visitID) {
        this.visitID = visitID;
    }

    public String getAdopterUsername() {
        return adopterUsername;
    }

    public void setAdopterUsername(String adopterUsername) {
        this.adopterUsername = adopterUsername;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public List<String> getSavedAnimalsID() {
        return savedAnimalsID;
    }

    public void setSavedAnimalsID(List<String> savedAnimalsID) {
        this.savedAnimalsID = savedAnimalsID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
