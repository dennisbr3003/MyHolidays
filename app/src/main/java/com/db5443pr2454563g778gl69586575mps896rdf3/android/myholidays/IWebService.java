package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

public interface IWebService {

    String BASE_URL = "https://opendata.rijksoverheid.nl/v1/sources/rijksoverheid/infotypes/schoolholidays/schoolyear";
    String JSON_UTF8 = "application/json; charset=utf-8";
    String PROC_OUTPUT = "output=json";
    String SIGNATURE_FIELD = "type";
    String SIGNATURE_KEY = "type";

    enum SyncDirection{
        UP, DOWN
    }

    enum DialogType{
         SYNCHRONIZATION
    }
}
