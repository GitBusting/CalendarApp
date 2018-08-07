package com.calendar.gitbusters.calendarapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class CreateAppointmentActivity extends AppCompatActivity {

    private EditText textTitle, textNotes, textPeriod;
    private TextView textViewTime, textViewDate;

    private String pickedDate = null;
    private String pickedTime = "12:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        Calendar cldr = Calendar.getInstance();
        cldr.setTimeInMillis(new Date().getTime());
        int mYear = cldr.get(Calendar.YEAR);
        int mMonth = cldr.get(Calendar.MONTH) + 1;
        int mDay = cldr.get(Calendar.DAY_OF_MONTH);

        String createDate;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                createDate = String.format("%02d-%02d-%d", mDay, mMonth, mYear);
            } else {
                createDate= extras.getString("Highlighted-Date");
            }
        } else {
            createDate= (String) savedInstanceState.getSerializable("Highlighted-Date");
        }
        pickedDate=createDate;
        // Set up references to GUI elements
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textTitle = (EditText) findViewById(R.id.textTitle);
        textNotes = (EditText) findViewById(R.id.textNotes);
        textPeriod = (EditText) findViewById(R.id.textPeriod);

        if(pickedDate!=null) textViewDate.setText(pickedDate);
        textViewTime.setText(pickedTime);
        findViewById(R.id.buttonPickDate).setOnClickListener(v -> {
            DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                    (view, year, month, dayOfMonth) -> {
                        pickedDate = String.format("%02d-%02d-%d",dayOfMonth,(month+1),year);
                        textViewDate.setText(pickedDate);
                    }, mYear, mMonth, mDay);
            dpd.show();
        });
        findViewById(R.id.buttonPickTime).setOnClickListener(v -> {
            TimePickerDialog tpd = new TimePickerDialog(v.getContext(),
                    (view, hourOfDay, minute) -> {
                        pickedTime = String.format("%02d:%02d",hourOfDay,minute);
                        textViewTime.setText(pickedTime);
                    }, 12, 0, false);
            tpd.show();
        });
        findViewById(R.id.buttonCreate).setOnClickListener(v -> {
            String title = textTitle.getText().toString();
            String notes = textNotes.getText().toString();
            int period = 0;
            if (!textPeriod.getText().toString().equals(""))
                period = Integer.parseInt(textPeriod.getText().toString());
            if(title.equals("") || notes.equals("") || pickedDate == null ||
                    pickedTime == null)
                Snackbar.make(v,"Please fill all the fields above before " +
                        "clicking \"CREATE\"",5000).show();
            else
            {
                System.out.println("Title: " + title + "Notes: " + notes);
                createAppointment(title, notes, period, pickedDate, pickedTime);
            }
        });
    }

    private final void createAppointment(String title, String notes, int period, String date, String time)
    {
        Appointment apt = new Appointment();
        apt.setTitle(title);
        apt.setNotes(notes);
        apt.setPeriod(period);
        apt.setDate(date);
        apt.setTime(time);
        // Finish this activity and send the new appointment
        // to the previous activity
        Intent i = new Intent().putExtra("new-apt",apt);
        setResult(RESULT_OK,i);
        finish();
    }
}
