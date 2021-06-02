package com.example.animalcare.models;

public class Admin {
    private static final String adminUsername = "admin";
    private static final String adminPassword = "admin";
    private static Admin instance = null;

    private Admin() {}

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }
    public boolean verifyCredentials(String username, String password) {
        return adminUsername.equals(username) && adminPassword.equals(password);
    }
}
