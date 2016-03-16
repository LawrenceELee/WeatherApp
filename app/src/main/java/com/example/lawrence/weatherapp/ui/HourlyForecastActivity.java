package com.example.lawrence.weatherapp.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.lawrence.weatherapp.R;
import com.example.lawrence.weatherapp.adapters.HourAdapter;
import com.example.lawrence.weatherapp.weather.Hour;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * Steps for construction Views (List and Recycler)
 * 1) Get a set of data to display. It can be an array, arraylist, hashmap or some other type of collection.
 * 2) Add a View (it can be ListView, RecyclerView, GridView, etc.)
 * 3) Design a custom layout for each item. It is the same principles a s full activity layout but for a smaller rectangle.
 * 4) Create an adapter to map data to the layout. This is like the MVC pattern where we have a data model, a layout for the view, and adapter for controller.
 *
 */
public class HourlyForecastActivity extends ActionBarActivity {
    private Hour[] mHours;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);

        // get the current intent
        Intent intent = getIntent();

        // get the parcelable from the intent sent over from MainActivity using the "key" we
        // labeled and shipped it with.
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_ACTIVITY);

        // convert Parcelable array into Hour array.
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourAdapter adapter = new HourAdapter(mHours);
        mRecyclerView.setAdapter(adapter);

        // layout manger component determines when list items are no longer visible and therefore can be reused.
        // This approach avoids the creation of unnecessary views or expensive findViewById() look-ups.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // this help improve performance of this list.
        mRecyclerView.setHasFixedSize(true);
    }
}
