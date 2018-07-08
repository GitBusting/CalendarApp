package com.calendar.gitbusters.calendarapp;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            URL webServerUrl = new URL("https://safe-sea-33768.herokuapp.com/appointments.json");
            HttpsURLConnection conn =
                    (HttpsURLConnection) webServerUrl.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.connect();

            if (conn.getResponseCode() == 200) {
                InputStream responseBody = conn.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                jsonReader.beginArray(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all objects
                    jsonReader.beginObject(); // Loop through all keys
                    while (jsonReader.hasNext()) { // Loop through all keys
                        String key = jsonReader.nextName(); // Fetch the next key
                        System.out.println(key);
                    }
                }

            } else {
                System.out.println("a very bad thing happened.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
