package com.example.lawrence.weatherapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lawrence.weatherapp.R;
import com.example.lawrence.weatherapp.weather.Hour;

/*
 * This adapter will use the newer Recycler View available for Android 5+.
 * It is a newer ListView (Recycler View is more powerful and flexible)
 * that is better for large sets of data and certain operations like animating items in a list.
 * Recycler View is backwards compatible for older version of Android.
 *
 * ViewHolders are required for RecyclerViews (they were just a good practice for custom ListViews).
 * The is a slight modification to the ViewHolders for RecyclerViews in that they are more than
 * simple containers for views in the item layout (like they were before for custom ListViews).
 * The new ViewHolders are responsible for mapping/binding the list data to the view.
 * The means that the ViewHolder now contains the code that was previously in the getView() method.
 * The makes things more organized and efficient.
 *
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    private Hour[] mHours;
    private Context mContext;

    public HourAdapter(Context context, Hour[] hours){
        mContext = context;
        mHours = hours;
    }

    // called when a new ViewHolder is needed. Views are still recycled but they are created here as needed.
    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // view variable holds the root view for out item layout.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);

        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    // bridge between adapter and the bindHour() method we created in our ViewHolder class.
    // all we need to do is call the correct hour from the array using position.
    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
       holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {     // same as ListViews, returns count of items.
        return mHours.length;
    }

    // getView() has moved from the Adapter into the ViewHolder.
    // This is required for RecyclerViews.
    // clicks/taps in RecyclerViews are handled by whatever implements interface for OnClickListener.
    // one way to do this is to make ViewHolder class implement the listener.
    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public TextView mTemperatureLabel;
        public ImageView mIconImageView;

        public HourViewHolder(View itemView) {
            super(itemView);

            // initialize member variables (member of the named inner class, we aren't following the 'm' before name convention)
            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);

            itemView.setOnClickListener(this);  // needed for clicks/taps.
        } // end constructor for HourViewHolder

        // this method bind/map all the data to the View.
        public void bindHour(Hour hour){
            mTimeLabel.setText(hour.getHour() + "");
            mSummaryLabel.setText(hour.getSummary() + "");
            mTemperatureLabel.setText(hour.getTemperature() + "");
            mIconImageView.setImageResource(hour.getIconId());
        }

        @Override
        public void onClick(View view) {
            // how do we assign values to these local variables?
            // the problem is that the binding happens in the bindHour() method above,
            // but the Hour hour is an argument to the method and is destroy once we leave bindHour's scope.
            // one solution is to create a member variable for hour, but that's extra work
            // since we can just extra the information from the member labels and convert char sequence to a string.
            String time = mTimeLabel.getText().toString();
            String temperature = mTemperatureLabel.getText().toString();
            String summary = mSummaryLabel.getText().toString();
            String message = String.format(
                    "At %s it will be %s and %s",
                    time,
                    temperature,
                    summary
            );
            // since we are in a custom adapter, we need to pass in the context where this is being used.
            // (i.e. can't use "this" for the context as parameter for makeToast())
            // solution is to pass in the context in our HourAdapter constructor and make context a member variable.
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

        }
    } // end nested inner class ViewHolder

} // end HourAdapter class
