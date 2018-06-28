package com.calendar.gitbusters.calendarapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListAppointmentsActivity extends AppCompatActivity {

    private ArrayList<Appointment> aptList = null;
    private LinearLayout ll = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_appointments);
        // Fill the list using MainActivity's data
        aptList = (ArrayList<Appointment>) getIntent().getSerializableExtra("apt-list");
        ll = (LinearLayout) findViewById(R.id.listAppointmentsLL);

        // Fill the actual list (LinearLayout) using event info
        if(aptList.size() > 0)
            setListElements();
        else
        {
            TextView tv = new TextView(this);
            tv.setText("There seems to be nothing here...");
            ll.addView(tv);
        }
    }

    private void setListElements()
    {
        for(Appointment i : aptList)
        {
            // Create a textview that displays event info and a button
            // that TODO will let user edit event info.
            TextView tv = new TextView(this);
            Button b = new Button(this);
            tv.setText(i.getTitle() + "\n" + i.getNotes() + "\n" + i.getDate()
                    + ": " + i.getTime());
            b.setText("EDIT");

            // Do the below for a relatively more beautiful list layout.
            tv.setLayoutParams(new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,0.2f));
            b.setLayoutParams(new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,0.8f));

            LinearLayout horizontalLL = new LinearLayout(this);
            horizontalLL.setOrientation(LinearLayout.HORIZONTAL);

            horizontalLL.addView(tv);
            horizontalLL.addView(b);

            ll.addView(horizontalLL);
        }
    }
}
