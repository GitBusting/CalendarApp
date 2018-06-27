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
        // fill the list using MainActivity's data
        aptList = (ArrayList<Appointment>) getIntent().getSerializableExtra("apt-list");
        ll = (LinearLayout) findViewById(R.id.listAppointmentsLL);

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
            TextView tv = new TextView(this);
            Button b = new Button(this);
            tv.setText(i.getTitle() + "\n" + i.getNotes() + "\n" + i.getDate()
                    + ": " + i.getTime());
            b.setText("EDIT");

            LinearLayout horizontalLL = new LinearLayout(this);
            horizontalLL.setOrientation(LinearLayout.HORIZONTAL);

            horizontalLL.addView(tv);
            horizontalLL.addView(b);

            ViewGroup.LayoutParams layoutParams = new
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            horizontalLL.setLayoutParams(layoutParams);

            ll.addView(horizontalLL);
        }
    }
}
