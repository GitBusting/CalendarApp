package com.calendar.gitbusters.calendarapp;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAppointmentsActivity extends AppCompatActivity {

    private ArrayList<Appointment> aptList = null;
    private LinearLayout ll = null;
    private TextInputEditText searchbar;
    private String search="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_appointments);
        // Fill the list using MainActivity's data
        aptList = (ArrayList<Appointment>) getIntent().getSerializableExtra("apt-list");
        ll = (LinearLayout) findViewById(R.id.listAppointmentsLL);


        searchbar = (TextInputEditText)findViewById(R.id.SearchBar);
        searchbar.addTextChangedListener(watchSearch);
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
        ll.removeAllViewsInLayout();
        for(Appointment i : aptList)
        {

            String txt = i.getTitle() + "\n" + i.getNotes() + "\n" + i.getDate()+ ": " + i.getTime();
            if(!txt.contains(search)) continue;
            // Create a textview that displays event info and a button
            // that TODO will let user edit event info.
            TextView tv = new TextView(this);
            Button b = new Button(this);

            tv.setText(txt);
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
        if(aptList.size() == 0)
        {
            TextView tv = new TextView(this);
            tv.setText("There seems to be nothing here...");
            ll.addView(tv);
        }
    }
    private TextWatcher watchSearch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            search = searchbar.getText().toString();
            setListElements();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            search = searchbar.getText().toString();
            setListElements();
        }
    };
}
