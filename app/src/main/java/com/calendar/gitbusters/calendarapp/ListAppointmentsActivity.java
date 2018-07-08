package com.calendar.gitbusters.calendarapp;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
public class ListAppointmentsActivity extends AppCompatActivity {

    private ArrayList<Appointment> aptList = null;
    private LinearLayout ll = null;
    private TextInputEditText searchBar;
    private String search="";
    private FloatingActionButton sortbyName, sortbyDate;
    private enum sort {incName, decName, incDate, decDate}

    sort sorting = sort.incDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_appointments);
        // Fill the list using MainActivity's data
        aptList = (ArrayList<Appointment>) getIntent().getSerializableExtra("apt-list");
        ll = (LinearLayout) findViewById(R.id.listAppointmentsLL);


        searchBar = (TextInputEditText)findViewById(R.id.SearchBar);
        searchBar.addTextChangedListener(watchSearch);
        // Fill the actual list (LinearLayout) using event info
        sortbyName = (FloatingActionButton) findViewById(R.id.NameSortButton);
        sortbyDate = (FloatingActionButton) findViewById(R.id.DateSortButton);



        sortbyName.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (sorting.equals(sort.incName)) {
                      sorting = sort.decName;
                      setListElements();
                  } else {
                      sorting = sort.incName;
                      setListElements();
                  }
              }
          });
        sortbyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sorting.equals(sort.incDate)) {
                    sorting = sort.decDate;
                    setListElements();
                } else {
                    sorting = sort.incDate;
                  setListElements();
                }
            }
        });
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

        TreeMap<Date, String> ListingDS;

        TreeMap<String, Date> ListingSD;
        Set set;
        if(sorting.equals(sort.decDate)||sorting.equals(sort.incDate)) {
            if (sorting.equals(sort.incDate))
                ListingDS = new TreeMap<>(Comparator.<Date>naturalOrder());
            else ListingDS = new TreeMap<>(Comparator.<Date>reverseOrder());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            for (Appointment i : aptList) {

                String txt = i.getTitle() + "\n" + i.getNotes() + "\n" + i.getDate() + ": " + i.getTime();
                if (!txt.toLowerCase().contains(search.toLowerCase())) continue;
                try {
                    Date datentime = dateFormat.parse(i.getDate() + " " + i.getTime());
                    ListingDS.put(datentime, txt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            set = ListingDS.entrySet();
        }else {
            if (sorting.equals(sort.incName))
                ListingSD = new TreeMap<>();
            else ListingSD = new TreeMap<>(Comparator.<String>reverseOrder());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            for (Appointment i : aptList) {

                String txt = i.getTitle() + "\n" + i.getNotes() + "\n" + i.getDate() + ": " + i.getTime();
                if (!txt.toLowerCase().contains(search.toLowerCase())) continue;
                try {
                    Date datentime = dateFormat.parse(i.getDate() + " " + i.getTime());
                    ListingSD.put(txt, datentime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            set = ListingSD.entrySet();
        }

        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            // Create a textview that displays event info and a button
            // that TODO will let user edit event info.
            TextView tv = new TextView(this);
            Button b = new Button(this);

            if(sorting.equals(sort.decDate)||sorting.equals(sort.incDate)) {
                tv.setText(entry.getValue().toString());
            }else
                tv.setText(entry.getKey().toString());
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
        /*if(ll.getChildCount() == 0)
        {
            TextView tv = new TextView(this);
            tv.setText("There seems to be nothing here...");
            ll.addView(tv);
        }*/
    }
    private TextWatcher watchSearch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            search = searchBar.getText().toString();
            setListElements();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            search = searchBar.getText().toString();
            setListElements();
        }
    };
}
