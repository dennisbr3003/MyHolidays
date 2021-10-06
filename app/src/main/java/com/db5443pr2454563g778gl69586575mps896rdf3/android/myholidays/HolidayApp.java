package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

import android.app.Application;
import android.util.Log;

import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.data_object.ContentRoot;

public class HolidayApp extends Application {

    // could this be a way to hold data during screen rotation
    private static ContentRoot contentRoot;
    public static HolidayApp instance = null;

    public ContentRoot getContentRoot() {
        return this.contentRoot;
    }

    public void setContentRoot(ContentRoot contentRoot) {
        this.contentRoot = contentRoot;
    }

    public boolean isContentRootAlive(){
        if(contentRoot != null) {
            return true;
        }
        return false;
    }

    public static HolidayApp getInstance() {
        if (null == instance) {
            instance = new HolidayApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DENNIS_B", "onCreate() of APP class called");
    }


}
