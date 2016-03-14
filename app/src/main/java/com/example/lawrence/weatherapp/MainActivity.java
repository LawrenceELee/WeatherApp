package com.example.lawrence.weatherapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "27974c4bc33201748eaf542a6769c3a8";
        double latitude = 37.8268;
        double longitude = -122.422;
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;

        // create a new OkHTTP client to connect to API.
        OkHttpClient client = new OkHttpClient();

        // build HTTP request object to send to API.
        Request request = new Request.Builder()
                .url(forecastUrl)
                .build();

        // create call object from combining OkHTTP client and HTTP request object.

        Call call = client.newCall(request);

        // synchronous execute() will use main thread (reserved for UI) and cause app to crash.
        //Response response = call.execute();

        // asynchronous enqueue() will use another thread to get the API data.
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        // output JSON data to log to spot-check that we are getting valid data from API.
                        Log.v(TAG, response.body().string());
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            } //end onResponse()

        });
    }

} // end MainActivity class
