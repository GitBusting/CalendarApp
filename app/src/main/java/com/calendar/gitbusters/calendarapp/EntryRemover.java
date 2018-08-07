package com.calendar.gitbusters.calendarapp;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

public class EntryRemover extends Thread {

    private Appointment delApt = null;

    public EntryRemover(Appointment apt) {
        delApt = apt;
    }

    public void run() {
        HttpsURLConnection conn = null;
        try {
            URL webServerUrl = new URL("https://safe-sea-33768.herokuapp.com/appointments/" +
                    delApt.getId() + ".json");
            Logger.getGlobal().log(Level.INFO, webServerUrl.toString());
            conn = (HttpsURLConnection) webServerUrl.openConnection();

            // Activity wants us to push some appointments to web-server
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setDoOutput(true);

            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-type", "application/json");
            conn.connect();
            if (conn.getResponseCode() == 200)
                System.out.println("Successfully posted a new apt.");
            else
                System.out.println(conn.getResponseMessage());
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }
}
