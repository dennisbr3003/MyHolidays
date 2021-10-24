package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

public class Result {

    boolean result;
    String message;
    String caption;

    public Result(boolean result, String message, String caption) {
        this.result = result;
        this.message = message;
        this.caption = caption;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
