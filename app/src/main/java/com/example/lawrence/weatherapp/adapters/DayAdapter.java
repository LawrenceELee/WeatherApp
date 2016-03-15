package com.example.lawrence.weatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lawrence.weatherapp.R;
import com.example.lawrence.weatherapp.weather.Day;

// Custom list adapter that maps each piece of data from the Day object to the daily_list_item.
// We're extending BaseAdapter instead of ListAdapter to learn about building from scratch.
// If we were to extend ListAdapter, the methods below would be implemented and we would only
// need to override the methods we want.
public class DayAdapter extends BaseAdapter{

    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context, Day[] days){
        mContext = context;
        mDays = days;
    }

    @Override
    // gets the count of items the array, kind of self-explanatory.
    public int getCount() {
        return mDays.length;
    }

    @Override
    // gets the element at position i, also kind of self-explanatory.
    public Object getItem(int i) {
        return mDays[i];
    }

    @Override
    // we are not using this. but this method is for tagging items for easy reference.
    public long getItemId(int i) {
        return 0;
    }

    @Override
    // this is where we set/map/bind our view with model.
    // getView() is called for each item in the list. it will be called to prepare all the visible
    // items then subsequent calls for items that scroll into view.
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        // where i is the position, convertView is the view object we want to reuse, viewGroup is
        // the first time getView is called convertView will be null so we need to create it and
        // set it up. If it isn't null, then we have a view ready to use and we just need to reset
        // the data (where the recycling happens).

        ViewHolder holder;

        if( convertView == null ){ // it is null so we need to create and set it up.

            // a layout inflater take xml layouts and turns them into view in code
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);

            holder = new ViewHolder();
            // we can now set the fields like we usually do, but we need to call findViewById() from
            // contextView since we aren't inside an activity.
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);

            convertView.setTag(holder); // sets the tag to be the name of the holder
            // we do this so we have a ref to reuse if it has been created.

        } else {
            holder = (ViewHolder) convertView.getTag();     // get ref to ViewHolder for reuse.
        }

        Day day = mDays[i];     // get the corresponding Day object from model.

        // set the corresponding view here.
        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax() + "");
        holder.dayLabel.setText(day.getDayOfTheWeek());

        if(i == 0){  // set text to "Today" for 1st day in list, instead of day of the week.
            holder.dayLabel.setText("Today");
        } else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }

        return convertView;
    }

    // use a helper object (ViewHolder) to re-use the same references to objects in the view.
    // it will save memory, power, and improve performance.
    private static class ViewHolder {
        ImageView iconImageView;    // these are public by default
        TextView temperatureLabel;
        TextView dayLabel;
    }

}
