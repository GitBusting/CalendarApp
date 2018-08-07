package com.calendar.gitbusters.calendarapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // References to some important objects
    private static ArrayList<Appointment> apts = null;
    private static ArrayList<Appointment> aptCopies = null;
    private static CalendarPickerView calendar = null;
    private static String highlightedDate = "";
    Synchronizer syn = new Synchronizer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pull user's appointments from the database.
        synchronizeAppointments();

        System.out.println("This is a token: " + FirebaseInstanceId.getInstance().getToken());

        // Initialize a calendarpickerview with a year earlier
        // as the minDate and a year later as the maxDate
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        calendar = (CalendarPickerView) findViewById(R.id.calendarView);
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -1);
        calendar.init(prevYear.getTime(), nextYear.getTime())
                .withSelectedDate(new Date()).inMode(CalendarPickerView.SelectionMode.SINGLE);

        // Initialize the proposed structure
        // for holding the user's appointments
        apts = new ArrayList<>();
        aptCopies = new ArrayList<>(); // Corresponding apt copies for the
                                                    // "highlightDates" dates

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
                String selDate = String.format("%02d-%02d-%d",mDay,mMonth,mYear);
                for(Appointment searchApt : aptCopies)
                {
                    if(searchApt.getDate().equals(selDate))
                        apt = searchApt;
                }
                highlightedDate = selDate;
                // Show apt's title if any appointment matches.
                if(apt == null) {
                    return;
                }
                else {
                    Snackbar.make(findViewById(R.id.content_main), apt.getTitle(), 3000).show();
                }
            }
            @Override
            public void onDateUnselected(Date date) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreate);
        fab.setOnClickListener(view -> {
            // switch to appointment creator activity when clicked
            Intent i = new Intent(view.getContext(),
                    CreateAppointmentActivity.class);
            if (!highlightedDate.isEmpty())
                i.putExtra("Highlighted-Date", highlightedDate);
            startActivityForResult(i, 1);
        });

        FloatingActionButton fabList = (FloatingActionButton) findViewById(R.id.fabList);
        fabList.setOnClickListener(view -> {
            // switch to appointment lister activity when clicked
            Intent i = new Intent(view.getContext(),
                    ListAppointmentsActivity.class);
            // TODO there may be a better way to share appointments
            // with the appointment lister activity, use intent
            // extras for now
            i.putExtra("apt-list",apts);
            startActivity(i);
        });

        // Wait for the thread that pulls appointments
        // from web-servers to finish
        try {
            syn.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Appointment apt : syn.getApts())
            this.handleNewAppointment(apt);
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
            syn = new Synchronizer();
            syn.addApt(apt);
            syn.start();
            this.handleNewAppointment(apt);
        }
    }

    /**
     * Given an appointment displays some of it's info
     * on the screen. This function is expected to be called
     * when CreateAppointmentActivity finishes with a specific
     * result code.
     * @param newApt the newly created appointment
     */
    private void handleNewAppointment(Appointment newApt)
    {
        if(!newApt.isSync()) {
            String disp = "Created a new appointment\nOn " +
                    newApt.getDate() + " " + newApt.getTime() +
                    "\nTitled: " + newApt.getTitle();
            Snackbar.make(findViewById(R.id.content_main), disp, 10000).show();
        }
        addNewApt(newApt);
    }

    /**
     * Given an appointment highlights it's date on the calendar
     * and adds it to the appointment list.
     * @param apt appointment to add to list.
     */
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
        aptCopies.add(apt);
        // If this is a periodic apt we display multiple
        // apts on the calendar
        if(apt.getPeriod() > 0)
        {
            System.out.println("Found a periodic apt: " + apt.getTitle());
            int prd = apt.getPeriod();
            // For any periodic apts, show each apt
            // in the next month
            for(int i = 0 ; i < 30 ; i += prd) {
                aptDate = new Date(aptDate.getTime() +
                    prd * 24 * 60 * 60 * 1000); // add period amount of milliseconds
                Appointment aptCopy = new Appointment(apt);
                aptCopy.setDate(sdf.format(aptDate));
                aptList.add(aptDate);
                aptCopies.add(aptCopy);
            }
        }
        calendar.highlightDates(aptList);
    }

    /**
     * TODO a template appointment list initializer function
     * This function is called to synchronize user's events
     * with whatever that is in the database.
     */
    private void synchronizeAppointments()
    {
        syn.start();
    }

}
