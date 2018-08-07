package com.calendar.gitbusters.calendarapp;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Template class holds code that will
 * be used in different contexts in the app
 * to synchronize appointments between the
 * database and local storage (actually memory)
 */

public class Synchronizer extends Thread {

    private ArrayList<Appointment> apts = null;

    public Synchronizer()
    {
        apts = new ArrayList<>();
    }

    /**
     * This segment is highly inspired by the snippets provided in the link below
     * https://code.tutsplus.com/tutorials/android-from-scratch-using-rest-apis--cms-27117
     */
    @Override
    public void run() {
        HttpsURLConnection conn = null, connPut = null;
        try {
            URL webServerUrl = new URL("https://safe-sea-33768.herokuapp.com/appointments.json");

            if(apts.size() > 0)
            {
                connPut =
                    (HttpsURLConnection) webServerUrl.openConnection();

                // Activity wants us to push some appointments to web-server
                connPut.setReadTimeout(10000 /* milliseconds */);
                connPut.setConnectTimeout(15000 /* milliseconds */);
                connPut.setDoOutput(true);

                connPut.setRequestMethod("POST");
                connPut.setRequestProperty("Content-type", "application/json");
                connPut.connect();
                OutputStreamWriter out = new OutputStreamWriter(connPut.getOutputStream());
                Appointment apt = apts.get(0);

                // Post all newly created apts to database.
                while(apts.size()>0)
                {
                    out.write(apt.parseJSONObject().toString());
                    System.out.println(apt.parseJSONObject().toString());
                    out.flush();
                    apts.remove(0);
                }
                out.close();
                if(connPut.getResponseCode() == 200)
                    System.out.println("Successfully posted a new apt.");
                else
                    System.out.println(connPut.getResponseMessage());

                connPut.disconnect();
            }

            // Get all apts from database
            conn =
                    (HttpsURLConnection) webServerUrl.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);

            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == 200) {
                InputStream responseBody = conn.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                jsonReader.beginArray(); // Start processing the JSON array
                while (jsonReader.hasNext()) { // Loop through all objects
                    jsonReader.beginObject(); // Loop through all keys
                    Appointment apt = new Appointment();
                    apt.setNotes("");
                    while (jsonReader.hasNext()) { // Loop through all keys
                        String key = jsonReader.nextName(); // Fetch the next
                        // This matches the title of our appointment
                        if (key.equals("id")) {
                            int id = jsonReader.nextInt();
                            apt.setId(id);
                        } else if (key.equals("name"))
                        {
                            String val = jsonReader.nextString();
                            apt.setTitle(val);
                            System.out.println("title: " + val);
                        }
                        // Extract date and time from this field
                        else if (key.equals("start_time"))
                        {
                            // String black magic
                            String val = jsonReader.nextString();
                            String[] parts = val.split("T");
                            String date = parts[0];
                            String[] fixDate = date.split("-");
                            date = fixDate[2] + "-" + fixDate[1] + "-" + fixDate[0];
                            String time = parts[1].split("[.]")[0];
                            System.out.println("date: " + date + " time: " + time);
                            apt.setDate(date);
                            apt.setTime(time);
                            apt.setSync(true);
                        }
                        else if (key.equals("period"))
                        {
                            int prd = jsonReader.nextInt();
                            apt.setPeriod(prd);
                        } else if (key.equals("id")) {
                            apt.setId(jsonReader.nextInt());
                        } else // we do not need these
                            jsonReader.skipValue();
                    }
                    apts.add(apt);
                    jsonReader.endObject();
                }
                jsonReader.endArray();

            } else {
                // Unable to connect
                System.out.println("a very bad thing happened.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn!=null)
                conn.disconnect();
            if(connPut!=null)
                connPut.disconnect();
        }
    }

    public void addApt(Appointment extApt)
    {
        this.apts.add(extApt);
    }

    public ArrayList<Appointment> getApts()
    {
        return apts;
    }
}
