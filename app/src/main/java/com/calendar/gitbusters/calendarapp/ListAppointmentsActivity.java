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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
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
            tv.setText(R.string.nothingtxt);
            ll.addView(tv);
        }
    }

    private void setListElements()
    {
        ll.removeAllViewsInLayout();

        TreeMap<String, String> ListingSS;

        TreeMap<String, Date> ListingSD;
        Set set;
        if(sorting.equals(sort.decDate)||sorting.equals(sort.incDate)) {
            if (sorting.equals(sort.incDate))
                ListingSS = new TreeMap<>(Comparator.<String>naturalOrder());
            else ListingSS = new TreeMap<>(Comparator.<String>reverseOrder());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.UK);
            for (Appointment i : aptList) {

                String txt = i.getTitle() + "\n" + i.getNotes() + "\n" + i.getDate() + ": " + i.getTime();
                if (!txt.toLowerCase().contains(search.toLowerCase())) continue;
                try {
                    Date dateAndTime = dateFormat.parse(i.getDate() + " " + i.getTime());
                    String datePlusTitle = dateAndTime.getTime() + i.getTitle();
                    String toPrint = i.getTitle() + "\n" + i.getNotes() + "\n" +dateAndTime.toString();
                    ListingSS.put(datePlusTitle, toPrint);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            set = ListingSS.entrySet();
        }else {
            if (sorting.equals(sort.incName))
                ListingSD = new TreeMap<>(Comparator.<String>naturalOrder());
            else ListingSD = new TreeMap<>(Comparator.<String>reverseOrder());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm",Locale.UK);
            for (Appointment i : aptList) {

                String txt = i.getTitle().substring(0,1).toUpperCase()+ i.getTitle().substring(1) + "\n" + i.getNotes() + "\n" + i.getDate() + ": " + i.getTime();
                if (!txt.toLowerCase().contains(search.toLowerCase())) continue;
                try {
                    Date dateAndTime = dateFormat.parse(i.getDate() + " " + i.getTime());
                    String toPrint = i.getTitle() + "\n" + i.getNotes() + "\n" +dateAndTime.toString();
                    ListingSD.put(toPrint, dateAndTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            set = ListingSD.entrySet();
        }

        for (Object aSet : set) {
            Map.Entry entry = (Map.Entry) aSet;
            // Create a textview that displays event info and a button
            // that TODO will let user edit event info.
            TextView tv = new TextView(this);
            Button b = new Button(this);

            if (sorting.equals(sort.decDate) || sorting.equals(sort.incDate)) {
                tv.setText(entry.getValue().toString());
            } else
                tv.setText(entry.getKey().toString());
            b.setText(R.string.editButtonTxt);

            // Do the below for a relatively more beautiful list layout.
            tv.setLayoutParams(new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 0.2f));
            b.setLayoutParams(new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 0.8f));

            LinearLayout horizontalLL = new LinearLayout(this);
            horizontalLL.setOrientation(LinearLayout.HORIZONTAL);

            horizontalLL.addView(tv);
            horizontalLL.addView(b);

            ll.addView(horizontalLL);
        }
        if(ll.getChildCount() == 0)
        {
            TextView tv = new TextView(this);
            tv.setText(R.string.nothingtxt);
            ll.addView(tv);
        }
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
