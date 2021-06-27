package com.example.animalcare.models;

import java.io.Serializable;
import java.util.List;

public class Volunteer extends BasicUser implements Serializable {
    private int startingHour;
    private int endingHour;
    private List<String> workingDays;
    private String startingDate;

    public Volunteer() {
    }

    public Volunteer(String username, String firstName, String lastName, String email, String hashPassword, int startingHour, int endingHour, List<String> workingDays, String startingDate) {
        super(username, firstName, lastName, email, hashPassword);
        this.startingHour = startingHour;
        this.endingHour = endingHour;
        this.workingDays = workingDays;
        this.startingDate = startingDate;
    }

    public int getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(int startingHour) {
        this.startingHour = startingHour;
    }

    public int getEndingHour() {
        return endingHour;
    }

    public void setEndingHour(int endingHour) {
        this.endingHour = endingHour;
    }

    public List<String> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(List<String> workingDays) {
        this.workingDays = workingDays;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public static final String MONDAY = "Monday";
    public static final String TUESDAY = "Tuesday";
    public static final String WEDNESDAY = "Wednesday";
    public static final String THURSDAY = "Thursday";
    public static final String FRIDAY = "Friday";

}
