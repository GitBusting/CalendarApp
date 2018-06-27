package com.calendar.gitbusters.calendarapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.squareup.timessquare.CalendarPickerView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // References to some important objects
    private static ArrayList<Appointment> apts = null;
    private static CalendarPickerView calendar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize a calendarpickerview with today
        // as the minDate and a year later as the maxDate
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        calendar = (CalendarPickerView) findViewById(R.id.calendarView);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);

        // Initialize the proposed structure
        // for holding the user's appointments
        apts = new ArrayList<>();

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                // See if there's an appointment
                // on the selected date
                Calendar cldr = Calendar.getInstance();
                cldr.setTimeInMillis(date.getTime());
                int mYear = cldr.get(Calendar.YEAR);
                int mMonth = cldr.get(Calendar.MONTH) + 1;
                int mDay = cldr.get(Calendar.DAY_OF_MONTH);
                Appointment apt = null;
                for(Appointment searchApt : apts)
                {
                    String selDate = String.format("%02d-%02d-%d",mDay,mMonth,mYear);
                    if(searchApt.getDate().equals(selDate))
                        apt = searchApt;
                }
                if(apt == null)
                    return;
                else
                {
                    // TODO Probably switch to a new activity
                    // that displays a list of appointments
                    // Display some text for now...
                    Snackbar.make(findViewById(R.id.content_main),"There is an event on " +
                            "the selected date", 3000).show();
                }
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),
                        CreateAppointmentActivity.class);
                startActivityForResult(i, 1);
            }
        });

        FloatingActionButton fabList = (FloatingActionButton) findViewById(R.id.fabList);
        fabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),
                        ListAppointmentsActivity.class);
                i.putExtra("apt-list",apts);
                startActivity(i);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // No need to use the result nor the request codes
        // here since CreateAppointmentActivity
        // currently only responds with an Appointment instance
        // Actually I take it back
        // since the user can close the activity using
        // the on-screen buttons as well.
        if(resultCode == RESULT_OK) {
            Appointment apt = (Appointment) data.getSerializableExtra("new-apt");
            this.handleNewAppointment(apt);
        }
    }

    // Template method that handles
    // the creation of a new appointment
    private void handleNewAppointment(Appointment newApt)
    {
        String disp = "Created new appointment\nOn " +
                newApt.getDate() + " " + newApt.getTime() +
                "\nTitled: " + newApt.getTitle();
        Snackbar.make(findViewById(R.id.content_main), disp, 10000).show();
        addNewApt(newApt);
    }

    private void addNewApt(Appointment apt)
    {
        apts.add(apt);
        // Parse a date object from the appointment's date field.
        Date aptDate = new Date();
        String someDate = apt.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            aptDate = sdf.parse(someDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // CalendarPickerView requires us to
        // keep "to-be-highlighted" dates in
        // a collection.
        ArrayList<Date> aptList = new ArrayList<>();
        aptList.add(aptDate);
        calendar.highlightDates(aptList);
    }

}
