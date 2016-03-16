package com.example.lawrence.weatherapp.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

// Data model for an "day of weather" (i.e. weather on Sat, Sun, Mon, etc.)
public class Day implements Parcelable{
    private long mTime;
    private String mSummary;
    private double mTemperatureMax;
    private String mIcon;
    private String mTimezone;

    // this constructor is used to "unwrap" (unparcel) data.
    protected Day(Parcel in) {
        mTime = in.readLong();
        mSummary = in.readString();
        mTemperatureMax = in.readDouble();
        mIcon = in.readString();
        mTimezone = in.readString();
    }
    // we need a empty default constructor because of the constructor that takes a Parcel object.
    public Day(){ }

    public static final Creator<Day> CREATOR = new Creator<Day>() {

        // this is where we call the constructor
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    // this is the method that "wraps" it up (writes the Day object to the Parcelable object).
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mTime);
        parcel.writeString(mSummary);
        parcel.writeDouble(mTemperatureMax);
        parcel.writeString(mIcon);
        parcel.writeString(mTimezone);
    }

    // we don't need this, we don't use it.
    @Override
    public int describeContents() {
        return 0;
    }

    // helper method to get the icon for weather condition.
    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    // helper method to get day of the week.
    public String getDayOfTheWeek(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date dateTime = new Date(mTime * 1000);
        return formatter.format(dateTime);
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperatureMax() {
        return (int) Math.round(mTemperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax = temperatureMax;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }
}
