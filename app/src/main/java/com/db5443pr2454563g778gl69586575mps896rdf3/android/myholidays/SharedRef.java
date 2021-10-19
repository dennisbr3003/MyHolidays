package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

public class SharedRef implements ISharedRef{

    public String getSharedRef(Context context, String field){

        String field_default_value = "";

        switch(field){
            case SHAREDREF_PERIOD:
                field_default_value = "2";
                break;
            case SHAREDREF_REGION:
                field_default_value = "Zuid";
                break;
            default:
                field_default_value = "unknown";
                break;
        }

        SharedPreferences prefs = context.getSharedPreferences(SHAREDREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(field, field_default_value);
    }

    public void setSharedRef(Context context, String field, String field_value){

        SharedPreferences prefs = context.getSharedPreferences(SHAREDREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putString(field, field_value);
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

    public void remSharedRefSchoolYear(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SHAREDREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.remove(SHAREDREF_YEAR);
        prefsEdit.apply(); // apply is background, commit is not
    }

}
