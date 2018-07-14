package com.calendar.gitbusters.calendarapp;

import org.json.JSONException;
import org.json.JSONObject;

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
    private boolean sync = false; //by default

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

    public void setSync(boolean sync) { this.sync = sync; }

    public boolean isSync() { return sync; }
    /**
     * @return a JSON object representing this appointment
     */
    public JSONObject parseJSONObject()
    {
        JSONObject jso = new JSONObject();
        try {
            jso.put("id",23193);
            jso.put("name",this.title);
            String[] dp = this.date.split("-");
            String dateTime = dp[2] + "-" + dp[1] + "-" + dp[0] + "T";
            dateTime += this.time + ":00.000Z";
            jso.put("start_time",dateTime);
            jso.put("created_at","XD");
            jso.put("updated_at","XDD");
            jso.put("url","https://killmepls.com");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jso;
    }

}
