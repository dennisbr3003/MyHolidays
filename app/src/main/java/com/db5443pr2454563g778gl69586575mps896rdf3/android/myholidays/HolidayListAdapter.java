package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.data_object.ContentRoot;
import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.data_object.Vacation;

public class HolidayListAdapter extends RecyclerView.Adapter<HolidayListAdapter.ViewHolder> implements ISharedRef, IVacation {
    private ContentRoot mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    private String schoolyear;
    private String region;

    SharedRef rf = new SharedRef();

    // data is passed into the constructor
    HolidayListAdapter(Context context, ContentRoot data) {

        this.mInflater = LayoutInflater.from(context);

        Log.d("DENNIS_B", "data.getCanonical() " + data.getCanonical());

        this.mData = data;

        schoolyear = rf.getSharedRefSchoolYear(context);
        region = rf.getSharedRef(context, SHAREDREF_REGION);

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.holiday_list_row, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            Vacation vacation = mData.get(position, schoolyear);

            Log.d("DENNIS_B", "vacation.toString() " + vacation.toString());

            holder.mHolidayTitle.setText(vacation.getType());
            holder.mStartDate.setText(vacation.getStartDate(region));
            holder.mEndDate.setText(vacation.getEndDate(region));

            if(vacation.getNumberOfTimeUnits(region).equals("0")){ // days = default
                if(vacation.getNumberOfTimeUnits(region, "H").equals("0")){ // hours if n.o. days = 0
                    holder.mNumberOfDays.setText(vacation.getNumberOfTimeUnits(region, "M")); // minutes if n.o. hours = 0
                    holder.mTimeUnit.setText(R.string.timeunit_m);
                } else {
                    holder.mNumberOfDays.setText(vacation.getNumberOfTimeUnits(region, "H")); // hours
                    holder.mTimeUnit.setText(R.string.timeunit_h);
                }
            }else {
                holder.mNumberOfDays.setText(vacation.getNumberOfTimeUnits(region)); //days (default)
                holder.mTimeUnit.setText(R.string.timeunit_d);
            }

            holder.mSmileySad.setVisibility(View.GONE);
            holder.mSmileyContent.setVisibility(View.GONE);
            holder.mSmileyHappy.setVisibility(View.GONE);
            holder.mSmileyVacation.setVisibility(View.GONE);
            holder.mStartsIn.setVisibility(View.VISIBLE);
            holder.mNumberOfDays.setVisibility(View.VISIBLE);
            holder.mTimeUnit.setVisibility(View.VISIBLE);
            holder.mVacationOver.setVisibility(View.INVISIBLE);

            String state = vacation.getState(region);

            switch (state) {
                case SMILEY_HAPPY:
                    holder.mSmileyHappy.setVisibility(View.VISIBLE);
                    break;
                case SMILEY_SAD:
                    holder.mSmileySad.setVisibility((View.VISIBLE));
                    /* adjust text */
                    holder.mStartsIn.setVisibility(View.INVISIBLE);
                    holder.mNumberOfDays.setVisibility(View.INVISIBLE);
                    holder.mTimeUnit.setVisibility(View.INVISIBLE);
                    holder.mVacationOver.setVisibility(View.VISIBLE);
                    holder.mVacationOver.setText(R.string.HolidayOver);
                    holder.mVacationOver.setTextColor(Color.RED);
                    break;
                case SMILEY_CONTENT:
                    holder.mSmileyContent.setVisibility(View.VISIBLE);
                    break;
                case SMILEY_VACATION:
                    holder.mSmileyVacation.setVisibility(View.VISIBLE);
                    holder.mStartsIn.setVisibility(View.INVISIBLE);
                    holder.mNumberOfDays.setVisibility(View.INVISIBLE);
                    holder.mTimeUnit.setVisibility(View.INVISIBLE);
                    holder.mVacationOver.setVisibility(View.VISIBLE);
                    holder.mVacationOver.setText(R.string.HolidayOngoing);
                    holder.mVacationOver.setTextColor(Color.rgb(50,205,50)); //Lime Green
                    break;
                default: //unknown
                    break;
            }
        } catch(Exception e){
            Log.d("DENNIS_B", "error trying to fill recycler view row " + e.getMessage());
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        Log.d("DENNIS_B", "Number of recyclerview rows " + mData.getSize(schoolyear));
        return mData.getSize(schoolyear);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mHolidayTitle;
        TextView mStartDate;
        TextView mEndDate;
        TextView mStartsIn;
        TextView mNumberOfDays;
        TextView mTimeUnit;
        ImageView mSmileySad;
        ImageView mSmileyHappy;
        ImageView mSmileyContent;
        ImageView mSmileyVacation;
        TextView mVacationOver;

        ViewHolder(View itemView) {
            super(itemView);
            mHolidayTitle = itemView.findViewById(R.id.txtHolidayTitle);
            mStartDate = itemView.findViewById(R.id.txtStartDate);
            mEndDate = itemView.findViewById(R.id.txtEndDate);
            mNumberOfDays = itemView.findViewById(R.id.txtNrOfDays);
            mSmileySad = itemView.findViewById(R.id.ivHolidaySad);
            mSmileyHappy = itemView.findViewById(R.id.ivHolidayHappy);
            mSmileyContent = itemView.findViewById(R.id.ivHolidayContent);
            mSmileyVacation = itemView.findViewById(R.id.ivHolidayVacation);
            mStartsIn = itemView.findViewById(R.id.txtStartsIn);
            mTimeUnit = itemView.findViewById(R.id.txtTimeUnit);
            mVacationOver = itemView.findViewById(R.id.txtVacationOver);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Vacation getItem(int id) {
        return mData.get(id, schoolyear);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
