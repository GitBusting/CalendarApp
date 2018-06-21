package com.calendar.gitbusters.calendarapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class CreateAppointmentActivity extends AppCompatActivity {

    private EditText textTitle, textNotes;
    private TextView textViewTime, textViewDate;

    private String pickedDate = null;
    private String pickedTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        // Set up references to GUI elements
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textTitle = (EditText) findViewById(R.id.textTitle);
        textNotes = (EditText) findViewById(R.id.textNotes);

        findViewById(R.id.buttonPickDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO change hardcoded default starting date.
                DatePickerDialog dpd = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pickedDate = String.format("%02d-%02d-%d",dayOfMonth,(month+1),year);
                        textViewDate.setText(pickedDate);
                    }
                },2018,5,21);
                dpd.show();
            }
        });
        findViewById(R.id.buttonPickTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        pickedTime = String.format("%02d:%02d",hourOfDay,minute);
                        textViewTime.setText(pickedTime);
                    }
                }, 0, 0, false);
                tpd.show();
            }
        });
        findViewById(R.id.buttonCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = textTitle.getText().toString();
                String notes = textTitle.getText().toString();
                if(title.equals("") || notes.equals("") || pickedDate == null || pickedTime == null)
                    Snackbar.make(v,"Please fill all the fields above before clicking \"CREATE\"",5000).show();
                else
                {
                    System.out.println("Title: " + title + "Notes: " + notes);
                    createAppointment(title,notes,pickedDate,pickedTime);
                }
            }
        });
    }

    private final void createAppointment(String title, String notes, String date, String time)
    {
        Appointment apt = new Appointment();
        apt.setTitle(title);
        apt.setNotes(notes);
        apt.setDate(date);
        apt.setTime(time);
        // Finish this activity and send the new appointment
        // to the previous activity
        Intent i = new Intent().putExtra("new-apt",apt);
        setResult(RESULT_OK,i);
        finish();
    }
}
