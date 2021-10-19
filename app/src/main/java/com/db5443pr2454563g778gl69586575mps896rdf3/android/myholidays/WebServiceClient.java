package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.data_object.ContentRoot;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebServiceClient implements IWebService,ISharedRef{

    private Context context;
    private static final MediaType JSON = MediaType.parse(JSON_UTF8);
    private IWebEventListener webEventListener;
    private Handler mHandler = new Handler();
    private SharedRef rf = new SharedRef();

    public IWebEventListener getWebEventListener() {
        return webEventListener;
    }

    public void setWebEventListener(IWebEventListener webEventListener) {
        this.webEventListener = webEventListener;
    }

    @SuppressLint("StaticFieldLeak")
    public void downloadUserDataPayload(final Context context){

        this.context = context;

        final AlertDialog dlg = createProgressDialog(SyncDirection.DOWN);
        dlg.show();

        new AsyncTask<Void, Void, Void>(){

            protected Void doInBackground(Void... voids) {

                OkHttpClient client = new OkHttpClient().newBuilder().build();

                Log.d("DENNIS_B", "okhttp3 url: " + createUrlFromSettings());

                // okHttp3 does not support a body for GET, using the device_id as a path variable -->
                Request request = new Request.Builder()
                        .url(createUrlFromSettings())
                        .method("GET", null)
                        .build();

                try {
                    Thread.sleep(1000); // fake slow download so the dialog will show
                } catch (InterruptedException e) {
                    showSyncErrorDialog(e.getMessage());
                }

                try {
                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                            dlg.dismiss();
                            showSyncErrorDialog(e.getMessage());
                        }

                        // call m.b.v.enqueue is zelf asynchrone, onPostExecute is hier dan niet nodig; alles gaat via de callback
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            JSONObject j_object = null;
                            try {
                                // first cast the answer to a json object for easy handling -->
                                String JsonResponse = response.body().string();
                                if(JsonResponse.startsWith("[")){
                                    //this cannot be supported for now
                                    Log.d("DENNIS_B","Response is an JSON array (not supported for now)");
                                    dlg.dismiss();
                                    showSyncErrorDialog("Unsupported response format. Expected JsonObject, received JsonArray");
                                    return;
                                }
                                j_object = new JSONObject(JsonResponse);

                                Log.d("DENNIS_B","Has signature key " + j_object.has(SIGNATURE_KEY));

                                if (j_object.has(SIGNATURE_KEY)) {

                                    // Now cast the answer to the expected format by using a pojo -->
                                    try {
                                        ObjectMapper mapper = new ObjectMapper();
                                        ContentRoot contentRoot = mapper.readValue(j_object.toString(), ContentRoot.class);
                                        Log.d("DENNIS_B","contentRoot not null? " + (contentRoot != null));

                                        if (contentRoot != null){

                                            Log.d("DENNIS_B","webEventListener not null? " + (webEventListener != null));

                                            if (webEventListener != null) {
                                                Log.d("DENNIS_B", "sending answer (contentRoot) to webEventListener");
                                                webEventListener.loadDownLoadedUserData(contentRoot, dlg);
                                            }
                                        } else {
                                            dlg.dismiss();
                                            showSyncErrorDialog("userdata did not download successfully. Nothing was retrieved");
                                        }
                                    }catch(Exception e){
                                        Log.d("DENNIS_B","error casting answer to pojo " + e.getMessage());
                                        dlg.dismiss();
                                        showSyncErrorDialog(e.getMessage());
                                    }

                                } else { // error object is returned
                                    dlg.dismiss();
                                    if (j_object.has(SIGNATURE_FIELD)) {
                                        showSyncErrorDialog(j_object.getString(SIGNATURE_FIELD));
                                    }else {
                                        showSyncErrorDialog("No signature key found in answer");
                                    }
                                }
                            } catch (final JSONException e) {
                                dlg.dismiss();
                                Log.d("DENNIS_B","error casting answer to JSON: " + e.getMessage());
                                showSyncErrorDialog(e.getMessage());
                            }
                        }
                    });
                } catch (final Exception e) {
                    dlg.dismiss();
                    Log.d("DENNIS_B","error, final exception: " + e.getMessage());
                    showSyncErrorDialog(e.getMessage());
                }
                return null;
            }
        }.execute();

    }

    private void showSyncErrorDialog(final String message){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                showErrorDialog(new Callresult(false, message), DialogType.SYNCHRONIZATION);
            }
        });
    }
    private void showErrorDialog(Callresult cr, final DialogType dialogType){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inf = LayoutInflater.from(context);
        View errorDialogExtraLayout;
        TextView tv;

        switch (dialogType){
            case SYNCHRONIZATION:
                errorDialogExtraLayout = inf.inflate(R.layout.cloud_sync_error, null);
                tv = errorDialogExtraLayout.findViewById(R.id.txtViewErrorMessage);
                break;
            default:
                errorDialogExtraLayout = inf.inflate(R.layout.impossible_error_dialog, null);
                tv = errorDialogExtraLayout.findViewById(R.id.txtViewImpossibleErrorMessage);
                break;
        }

        builder.setView(errorDialogExtraLayout);
        tv.setText(cr.getMessage());
        AlertDialog dlg = builder.create();
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(true);

        dlg.show();

    }

    private AlertDialog createProgressDialog(SyncDirection syncDirection){

        AlertDialog.Builder builder = new AlertDialog.Builder(context); // no title, icon or message
        builder.setCancelable(false); // block back-button

        LayoutInflater inf = LayoutInflater.from(context); // set op the dialog extra layout (cloud_sync_dialog_animated.xml)
        View cloudDialogExtraLayout = inf.inflate(R.layout.cloud_sync_dialog_animated, null);

        TextView tv = cloudDialogExtraLayout.findViewById(R.id.txtViewProgressHeader);
        if(syncDirection.equals(SyncDirection.UP)) {
            tv.setText(R.string.UploadingUserData);
        } else {
            tv.setText(R.string.DownloadingUserData);
        }

        builder.setView(cloudDialogExtraLayout); // load the view into the dialog

        AlertDialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);
        return dlg;

    }

    private String createUrlFromSettings(){

        String url;

        if(rf.getSharedRef(context, SHAREDREF_PERIOD).equals("1")){
            url = String.format("%s?%s", BASE_URL, PROC_OUTPUT);
        } else {
            url = String.format("%s/%s/%s?%s", BASE_URL, INTEGRATION, rf.getSharedRefSchoolYear(context), PROC_OUTPUT);
        }
        return url;
    }

    private class Callresult{

        private Boolean answer = false;
        private String message = "";

        public Callresult() {
        }

        public Callresult(Boolean answer, String message) {
            this.answer = answer;
            this.message = message;
        }

        public Boolean getAnswer() {
            return answer;
        }

        public void setAnswer(Boolean answer) {
            this.answer = answer;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


    }

}
