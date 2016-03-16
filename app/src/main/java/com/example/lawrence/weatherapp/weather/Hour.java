package com.example.lawrence.weatherapp.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

// Data model for an "hour of weather" (i.e. weather at 9AM, 10AM, 11AM, etc.)
public class Hour implements Parcelable{
    private long mTime;
    private double mTemperature;
    private String mSummary;
    private String mIcon;
    private String mTimezone;

    // creates an Hour object from a Parcel object. (unwraps parcel).
    protected Hour(Parcel in) {
        // be careful, order of write operations inside this method must be the same order
        // of the read() in the constructor.
        mTime = in.readLong();
        mTemperature = in.readDouble();
        mSummary = in.readString();
        mIcon = in.readString();
        mTimezone = in.readString();
    }
    // need a default constructor when we have multiple constructors, so placed an empty one.
    public Hour(){ }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel in) {
            return new Hour(in);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };

    @Override
    public int describeContents() {     // not used
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // be careful, order of write operations inside this method must be the same order
        // of the read() in the constructor.
        parcel.writeLong(mTime);
        parcel.writeDouble(mTemperature);
        parcel.writeString(mSummary);
        parcel.writeString(mIcon);
        parcel.writeString(mTimezone);
    }

    // helper method to get a formated hour
    public String getHour(){
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        Date date = new Date(mTime * 1000);
        return formatter.format(date);
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

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
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
