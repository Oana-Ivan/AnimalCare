package com.example.animalcare.roomDatabase.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    private int userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String hashPassword;
//    private String role;
//    private boolean isVolunteer;

    public User(int userId, String firstName, String lastName, String username, String email, String hashPassword){ //, boolean isVolunteer) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.hashPassword = hashPassword;
//        if (isVolunteer) {
//            this.role = "VOLUNTEER";
//        }
//        else {
//            this.role = "ADOPTER";
//        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

//    public boolean isVolunteer() {
//        return isVolunteer;
//    }
//
//    public void setVolunteer(boolean volunteer) {
//        isVolunteer = volunteer;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }

    //    public enum Role {
//        ADMIN, VOLUNTEER, ADOPTER;
//    }
}
