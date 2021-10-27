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
        // deze moet met message, we hebben de dlg nog nodig dus geen direct show()
        final AlertDialog dlg = Message.ProgressDialog.getProgressDialog(context, new Result(context.getString(R.string.DownloadingUserData)));
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

                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                        dlg.dismiss();
                        showSyncErrorDialog(e.getMessage());
                    }

                    // call m.b.v.enqueue is zelf asynchrone, onPostExecute is hier dan niet nodig; alles gaat via de callback
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

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
                            JSONObject j_object = new JSONObject(JsonResponse);

                            Log.d("DENNIS_B",context.getString(R.string.signature) + j_object.has(SIGNATURE_KEY));

                            if (j_object.has(SIGNATURE_KEY)) {

                                // Now cast the answer to the expected format by using a pojo -->
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
                                    showSyncErrorDialog("data did not download successfully. Nothing was retrieved");
                                }

                            } else { // error object is returned
                                dlg.dismiss();
                                if (j_object.has(SIGNATURE_FIELD)) {
                                    showSyncErrorDialog(j_object.getString(SIGNATURE_FIELD));
                                }else {
                                    showSyncErrorDialog("No signature key found in answer");
                                }
                            }
                        } catch (JSONException e) {
                            dlg.dismiss();
                            Log.d("DENNIS_B","error casting answer to JSON: " + e.getMessage());
                            showSyncErrorDialog(e.getMessage());
                        }catch(Exception e){
                            Log.d("DENNIS_B","error casting answer to pojo " + e.getMessage());
                            dlg.dismiss();
                            showSyncErrorDialog(e.getMessage());
                        }
                    }
                });
                return null;
            }
        }.execute();

    }

    private void showSyncErrorDialog(final String message){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Message.ErrorDialog.getErrorMessageDialog(context, new Result(message)).show();
            }
        });
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

}