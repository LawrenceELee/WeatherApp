package com.example.lawrence.weatherapp.ui;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.lawrence.weatherapp.R;

import java.lang.reflect.Array;

public class DailyForecastActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        String[] daysOfTheWeek =
                {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};


        // android.R.layout.simple_list_item_1 is a default list layout to the list adapter below.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                daysOfTheWeek
        );
        setListAdapter(adapter);
        // all the "hardwork" is done by use by the android framewhere when we build lists using
        // the default settings.



    }
}
