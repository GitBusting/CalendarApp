package com.calendar.gitbusters.calendarapp;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Template class holds code that will
 * be used in different contexts in the app
 * to synchronize appointments between the
 * database and local storage (actually memory)
 */

public class Synchronizer implements Runnable {

    @Override
    public void run() {
        try {
            URL webServerUrl = new URL("https://safe-sea-33768.herokuapp.com");
            HttpsURLConnection conn =
                    (HttpsURLConnection) webServerUrl.openConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
