package com.example.animalcare.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ticket implements Serializable {
    private String ticketID;
    private String username;
    private String title;
    private String text;
    private String status;
    private String date;
    public static final String OPEN = "OPEN";
    public static final String CLOSED = "CLOSED";

    public Ticket() {
    }

    public Ticket(String ticketID, String username, String title, String text) {
        this.ticketID = ticketID;
        this.username = username;
        this.title = title;
        this.text = text;
        this.status = OPEN;
        SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:sss'Z'");
        date = ISO_8601_FORMAT.format(new Date());
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
