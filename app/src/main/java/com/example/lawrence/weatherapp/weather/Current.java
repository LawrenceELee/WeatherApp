package com.example.lawrence.weatherapp.weather;

import com.example.lawrence.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Current {
    private String mIcon;
    // the icon is a string in the JSON data, but we convert to an int to get the right icon in resources. see getIconId() below.
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;

    // converts number of seconds into a nicely formatted time (e.g. 12:00 P).
    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));

        // convert from seconds to milliseconds since Date objects use milliseconds from epoch.
        Date dateTime = new Date(getTime() * 1000);

        String timeString = formatter.format(dateTime);
        return timeString;
    }

    // converts the string in JSON data into a int so that we can matchup with corresponding weather icon.
    // refactored and moved into Forecast so it is available to the Adapters.
    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }
    public int getPrecipChance() {
        return (int) Math.round(mPrecipChance * 100);
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getTimeZone() {
        return mTimeZone;
    }
}
