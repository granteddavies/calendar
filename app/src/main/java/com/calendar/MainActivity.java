package com.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendar;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private static final String FILE_NAME = "events.gd";
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<Event> daysEvents = new ArrayList<Event>();
    private ArrayList<String> daysEventsDescriptions = new ArrayList<String>();
    private int year, month, dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load();

        // Set up the list of the day's events
        list = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, daysEventsDescriptions);
        list.setAdapter(adapter);

        // Set up the floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setBackgroundColor(Color.parseColor("#555555"));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewEvent();
            }
        });

        calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setShowWeekNumber(false);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                updateDay(year, month, dayOfMonth);
                getDaysEvents();
            }
        });

        Date date = new Date(calendar.getDate());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        updateDay(year, month, dayOfMonth);
        getDaysEvents();
    }

    private void updateDay(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    private void getDaysEvents() {
        // Accumulate a list of all the events on the selected day
        daysEvents.clear();
        daysEventsDescriptions.clear();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            if (year == event.getYear() &&
                    month == event.getMonth() &&
                    dayOfMonth == event.getDayOfMonth()) {
                daysEvents.add(event);
                daysEventsDescriptions.add(event.getDescription());
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void createNewEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Event");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter a description");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Event event = new Event(year, month, dayOfMonth, input.getText().toString());
                events.add(event);
                save();
                getDaysEvents();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void load() {
        // Read in the list of events
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            events = (ArrayList<Event>) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Make sure we have a list
        if (events == null) {
            events = new ArrayList<Event>();
        }
    }

    private void save() {
        // Save the list of events
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(events);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
