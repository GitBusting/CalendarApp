package com.calendar.gitbusters.calendarapp;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class CreateAppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);
        ((Button) findViewById(R.id.button)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        displayStuff(v);
                    }
                }
        );
    }

    protected final void displayStuff(View v)
    {
        DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
        System.out.println(dp.getDayOfMonth());
        System.out.println(dp.getMonth());
        System.out.println(dp.getYear());
    }
}
