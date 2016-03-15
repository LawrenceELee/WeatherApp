package com.example.lawrence.weatherapp.weather;

// Bundles together all weather data models:
// current weather (Current.java),
// weather in the next few hours (Hour.java),
// weather for the next few days (Day.java).
public class Forecast {
    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDailyForecast;
}
