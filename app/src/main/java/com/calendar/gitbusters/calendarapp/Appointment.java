package com.calendar.gitbusters.calendarapp;

import java.io.Serializable;

/**
 * Appointment class currently holds
 * the most basic data needed to identify
 * an appointment
 *
 * Implementing "Serializable" to send an instance
 * of this class from an activity to another.
 */

/*
 * TODO decide on whether or not to use the "Parcelable"
 * interface for exchanging objects in between activities.
 */
public class Appointment implements Serializable{
    private String date;
    private String notes;
    private String time;
    private String title;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
