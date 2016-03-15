package com.example.lawrence.weatherapp.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.lawrence.weatherapp.R;
import com.example.lawrence.weatherapp.adapters.DayAdapter;
import com.example.lawrence.weatherapp.weather.Day;

import java.lang.reflect.Array;
import java.util.Arrays;

public class DailyForecastActivity extends ListActivity {

    private Day[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        /*
        // using standard built-in android listview and listadapters.
        String[] daysOfTheWeek =
                {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};


        // android.R.layout.simple_list_item_1 is a default list layout to the list adapter below.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                daysOfTheWeek
        );
        setListAdapter(adapter);
        // all the "hard work" is done by use by the android framewhere when we build lists using
        // the default settings.
        // The problem with this is that we are limited to Strings (daysOfTheWeek)
        // or whatever data type can fit in the generics field. We can solve this by building a custom adapter.
        */

        // we deserialize (unwrap) the extra data sent over with the intent.
        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        // Parcelable is an interface used to make data easy to transfer from one activity to another.
        // we can't just pass the object itself (have to use parcelable) because of how activities
        // are create and destroyed (android lifecycle).
        // The standard solution is to serialize our data into a common format that can be
        // deserialize at the other end (i.e. wrap it up, ship it, and unwrap it.)
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        // Custom list adapter that maps each piece of data from the Day object to the daily_list_item.
        DayAdapter adapter = new DayAdapter(this, mDays);
        // since Day[] mDays is in Forecast object is only available in MainActivity,
        // we need a Parcelable to pass the data from MainActivity to DailyForecastActivity.


        setListAdapter(adapter);
    }
}
