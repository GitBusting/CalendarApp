package com.calendar.gitbusters.calendarapp;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.internal.SnackbarContentLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CalendarView cw = (CalendarView) findViewById(R.id.calendarView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CreateAppointmentActivity.class);
                startActivityForResult(i, 1);
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
    public void handleNewAppointment(Appointment newApt)
    {
        String disp = "Created new appointment\nOn " +
                newApt.getDate() + " " + newApt.getTime() +
                "\nTitled: " + newApt.getTitle();
        Snackbar.make(findViewById(R.id.content_main), disp, 10000).show();
    }
}
