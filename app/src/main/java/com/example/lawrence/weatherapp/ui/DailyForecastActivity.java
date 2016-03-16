package com.example.lawrence.weatherapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lawrence.weatherapp.R;
import com.example.lawrence.weatherapp.adapters.DayAdapter;
import com.example.lawrence.weatherapp.weather.Day;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

// normally we would extend ListActivity because that gives us a reference to the ListView, setListAdapter(), onListItemClick() built-in
// we'll extend have DailyForecastActivity extend Activity so we can learn how to implement the
// same functionality of the missing methods from scratch.
public class DailyForecastActivity extends Activity {

    private Day[] mDays;

    // need a reference to the ListView since we don't have it by default in extending Activity
    // we'll do this using ButterKnife.
    @Bind(android.R.id.list) ListView mListView;
    // similarly we'll need a reference to the empty text view
    @Bind(android.R.id.empty) TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.bind(this);

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


        // if we extended ListActivity, we would have setListAdapter by default.
        //setListAdapter(adapter);
        // but since we don't, we implement the same functionality by:
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // create a simple toast when item is clicked
                String dayOfTheWeek = mDays[position].getDayOfTheWeek();
                String conditions = mDays[position].getSummary();
                String highTemp = mDays[position].getTemperatureMax() + "";
                String message = String.format(
                        "On %s the high will be %s and it will be %s",
                        dayOfTheWeek,
                        conditions,
                        highTemp
                );
                //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                // can't use "this" for context since we are inside the anonymous inner class.
                // instead use DailyForecastActivity.this to reference the outer class.
                Toast.makeText(DailyForecastActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    /*
    // this is replaced by the anonymous inner class above.
    // onListItemClicked() method is called when a list item is click/tapped.
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // where l is ListView, v is specific item that was clicked,
        // position is the index of item, id is optional (not set).

        super.onListItemClick(l, v, position, id);

        // create a simple toast when item is clicked
        String dayOfTheWeek = mDays[position].getDayOfTheWeek();
        String conditions = mDays[position].getSummary();
        String highTemp = mDays[position].getTemperatureMax() + "";
        String message = String.format(
                "On %s the high will be %s and it will be %s",
                dayOfTheWeek,
                conditions,
                highTemp
        );

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    */

}
