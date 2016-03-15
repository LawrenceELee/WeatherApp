package com.example.lawrence.weatherapp.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lawrence.weatherapp.R;
import com.example.lawrence.weatherapp.weather.Current;
import com.example.lawrence.weatherapp.weather.Forecast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 * Things learned while building this app:
 *
 * 1) API:
 * API's (Application Software Interface) are different than SDK's in that API is one small part of an SDK. SDK is all the software required to run something, while API is just the part that is used to access it.
 * API can be via the web (like facebook api or twitter api) or locally (like android.graphics).
 * API's are important because it "creates a blackbox" (interfaces) that separates clients code with "our" code so that we can change the internals of the blackbox if necessary, without chaning how clients code works.
 * API's should be RESTful (Representaional State Transfer) meaning we don't need to know how the internals of the API works, just how to access it usually an URI/URL. REST is not just restricted to the web, but most web API's are RESTful.
 * API's usually relay data via XML (eXtensible Markup Language) or JSON (JavaScript Object Notation) format, however there are other less used formats too. As long as the client and server agree on the format used.
 *
 * 2) Networking:
 * Transfer only as much data as needed to complete the task to convserve battery life and data usage (if limited or costs per MB).
 * Avoid network timeouts. You don't want something to stop loading because it is taking "too long" (might be a slow connection 2G, 3G).
 * Allow users to pause/cancel network operations if they change their mind about a large download (podcast, audio, video file).
 * Handle failures gracefully (i.e. display pop-up instead of crashing entire program).
 * Keep users security in mind since you don't know how secure a network connection is. (i.e. use HTTPS if possible)
 *
 * 3) CRUD: Create (PUT), Retrieve (GET), Update (POST), Delete (DELETE)
 * CRUD is the general term. PUT, GET, POST, DELETE.
 *
 * 4) Gradle dependencies/3rd party Libraries:
 * OkHTTP (open-sourced by Square) for web connectivity instead of native HTTP library.
 * ButterKnife to "inject" binding (of controller code with UI code) instead of using typical boilerplate code.
 *
 * 5) Concurrency (threads) and Error handling:
 * main thread is reserved (top priority) for UI processing (tap, swipe, other user interaction) so that the app remains responsive.
 * other process should use background threads.
 * execute() for synchronous
 * enqueue() for asynchoronous
 * runOnUiThread() is part of the callback for the request/response model for * threads.
 * enqueue() method creates a new thread and it does it work in the background.
 * but to tell the main UI thread that the background work is done and to merge  * it back into the main UI thread we use runOnUIThread()
 * background threads are allowed to write to the log, but not able to touch the * main UI views (you will get a "calledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views"
 *
 * 6) Working with JSON:
 * using Java's native JSON library to parse JSON data from API.
 *
 * 7) Building an User Interface (UI):
 * use ButterKnife to bind controller (MainActivity) with view (layout) (saves time by not having to write boilerplate code).
 *
 */
public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Forecast mForecast;

    // using ButterKnife library to bind MainActivity (controller) with layout (view).
    // instead of old boilerplate style of declaring a member variable for corresponding view item.
    // then assigning variable a value using findViewById().
    // i.e. private TextView mTemperatureLabel; then mTemperatureLabel = findViewById(...);
    @Bind(R.id.timeLabel)
    TextView mTimeLabel;
    @Bind(R.id.temperatureLabel)
    TextView mTemperatureLabel;
    @Bind(R.id.humidityValue)
    TextView mHumidityValue;
    @Bind(R.id.precipValue)
    TextView mPrecipValue;
    @Bind(R.id.summaryLabel)
    TextView mSummaryLabel;
    @Bind(R.id.iconImageView)
    ImageView mIconImageView;
    @Bind(R.id.refreshImageView)
    ImageView mRefreshImageView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // required to do this right after activity is created to use ButterKnife lib.
        ButterKnife.bind(this);

        // hide refresh spinner/progress bar at the start of app.
        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude = 37.8268; final double longitude = -122.422; //Alcatraz Island, CA
        //latitude = 40.7791; longitude = -73.9635;       // Central Park, New York City, NY
        //latitude = 41.8661; longitude = --87.6169;      // Millennium Park, Chicago, IL
        // there is a slight bug where it doesn't update the location if you change lat/long.
        // also there isn't a name for the location in the DarkSky JSON response so location name is currently hardcoded.

        // logic for refresh button to fetch updated JSON data.
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForecast(latitude, longitude);
            }
        });

        getForecast(latitude, longitude);
        Log.d(TAG, "Main UI code running.");
    } // end onCreate()


    private void getForecast(double latitude, double longitude) {
        String apiKey = getAPIKey();
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {

            // toggles visibility of refresh button spinner/progress bar.
            toggleRefresh();

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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String JSONdata = response.body().string();
                        Log.v(TAG, JSONdata); // output JSON data to log to spot-check that we are getting valid data from API.

                        if (response.isSuccessful()) {

                            mForecast = parseForecastDetails(JSONdata);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }

                    } catch (IOException ioe) {
                        Log.e(TAG, "IOException caught: ", ioe);
                    } catch (JSONException jsone) {
                        // handle JSON exception here, instead of in the method.
                        Log.e(TAG, "JSONException caught: ", jsone);
                    }
                } //end onResponse()

            }); //end anonymous inner class

        } else {
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }
    }

    // toggles visibility of refresh button's spinner/progress bar.
    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();
        mTemperatureLabel.setText(current.getTemperature() + "");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());

        // convert int into the corresponding picture in the layout
        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        return forecast;
    }

    // convert JSON data (string) into our model for the weather (temp, time, humidity, etc.)
    private Current getCurrentDetails(String jsondata) throws JSONException {
        JSONObject forecast = new JSONObject(jsondata);

        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");
        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // network is available.
        if (networkInfo != null && networkInfo.isConnected()) return true;

        return false;
    }

    // Shows pop-up dialog box if there was some sort of error.
    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    // Helper methods to hide API Key in assets folder so that it isn't visible on GitHub.
    private String getAPIKey() {
        Properties prop = loadProperties();
        String apiKey = prop.getProperty("apikey");
        return apiKey;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Properties loadProperties() {
        Properties prop = new Properties();
        String[] fileList = {"config.properties"};   // can get multiple property files if you want.

        for (int i = fileList.length - 1; i >= 0; --i) {
            String file = fileList[i];

            // using new try-with-resoruces syntax in Java 7 to open file.
            // don't need to use "finally" block.
            try (InputStream inputStream = getAssets().open(file)) {   //
                prop.load(inputStream);
                inputStream.close(); // do we need to explicitly close stream with try-with-resources?
            } catch (FileNotFoundException fnfe) {
                Log.d(TAG, "Ignoring missing property file " + file);
            } catch (IOException ioe) {
                Log.e(TAG, "More general input/output exception", ioe);
            }
        }
        return prop;
    }

} // end MainActivity class
