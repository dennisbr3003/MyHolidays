package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class SharedRef implements ISharedRef{

    public String getSharedRefRegion(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SHAREDREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(SHAREDREF_REGION, "Zuid");
    }

    public void setSharedRefRegion(Context context, String region){
        SharedPreferences prefs = context.getSharedPreferences(SHAREDREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putString(SHAREDREF_REGION, region);
        prefsEdit.apply(); // apply is background, commit is not
    }

    public void setSharedRefSchoolYear(Context context, String schoolyear){
        SharedPreferences prefs = context.getSharedPreferences(SHAREDREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putString(SHAREDREF_YEAR, schoolyear);
        prefsEdit.apply(); // apply is background, commit is not
    }

    public String getSharedRefSchoolYear(Context context){
        int year_start = Calendar.getInstance().get(Calendar.YEAR);
        int year_end = Calendar.getInstance().get(Calendar.YEAR) + 1;
        SharedPreferences prefs = context.getSharedPreferences(SHAREDREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(SHAREDREF_YEAR, year_start + "-" + year_end);
    }

}
