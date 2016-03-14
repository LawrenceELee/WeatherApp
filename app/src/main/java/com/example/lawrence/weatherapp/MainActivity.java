package com.example.lawrence.weatherapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

        String apiKey = getAPIKey();
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

    // Helper methods to hide API Key in assets folder so that it isn't visible on GitHub.
    private String getAPIKey(){
        Properties prop = loadProperties();
        String apiKey = prop.getProperty("apikey");
        return apiKey;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Properties loadProperties(){
        Properties prop = new Properties();
        String[] fileList = {"config.properties"};   // can get multiple property files if you want.

        for( int i=fileList.length-1; i >= 0; --i ){
            String file = fileList[i];

            // using new try-with-resoruces syntax in Java 7 to open file.
            // don't need to use "finally" block.
            try( InputStream inputStream = getAssets().open(file) ) {   //
                prop.load(inputStream);
                inputStream.close(); // do we need to explicitly close stream with try-with-resources?
            } catch( FileNotFoundException fnfe ) {
                Log.d(TAG, "Ignoring missing property file " + file);
            } catch( IOException ioe ) {
                Log.e(TAG, "More general input/output exception", ioe);
            }
        }

        return prop;
    }


} // end MainActivity class
