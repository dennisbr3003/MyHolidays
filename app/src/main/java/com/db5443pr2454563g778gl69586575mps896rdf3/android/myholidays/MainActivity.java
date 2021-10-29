package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.data_object.ContentRoot;
import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.databinding.ActivityMainBinding;
//import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.databinding.ActivityMainBinding;
//import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.databinding.ActivityMainBinding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements HolidayListAdapter.ItemClickListener, IWebEventListener, IActivityEventListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    WebServiceClient webServiceClient = new WebServiceClient();
    HolidayListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        webServiceClient.setWebEventListener(this);

        /* get the data */
        Log.d(getString(R.string.tag), "HolidayApp.getInstance().isContentRootAlive() " + HolidayApp.getInstance().isContentRootAlive());
        if(!HolidayApp.getInstance().isContentRootAlive()) {
            webServiceClient.downloadUserDataPayload(this);
        }
        else {
            this.loadDownLoadedUserData(HolidayApp.getInstance().getContentRoot(), null);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSettings();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSettings(){
        Log.d(getString(R.string.tag), getString(R.string.start_settings));
        try {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.SecondFragment);
            FragmentTransaction transaction = fm.beginTransaction();
            if(fragment == null) {
                fragment = new SettingsFragment();
                transaction.add(R.id.fragment_container_view, fragment);
                transaction.addToBackStack(getString(R.string.tag));
                transaction.commit();
            }
            ((SettingsFragment) fragment).setActivityEventListener(this);
        }catch(Exception e){
            Log.d(getString(R.string.tag), e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onItemClick(View view, int position) {
            //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadDownLoadedUserData(ContentRoot holidayMain, final AlertDialog dlg) {

        // data to populate the RecyclerView with
        Log.d("DENNIS_B", "HolidayApp.getInstance().isContentRootAlive() " + holidayMain.getType().trim());

        Context context_local = this;

        HolidayApp.getInstance().setContentRoot(holidayMain);
        try {
            this.runOnUiThread(() -> {

                // AsyncTask is deprecated. You can catch the completion event of this runnable by using a handler
                Handler handler = new Handler(Looper.getMainLooper());

                RecyclerView recyclerView = findViewById(R.id.holiday_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(context_local));
                adapter = new HolidayListAdapter(context_local, holidayMain);
                recyclerView.addItemDecoration(new DividerItemDecoration(context_local, DividerItemDecoration.VERTICAL));
                adapter.setClickListener((MainActivity) context_local);
                recyclerView.setAdapter(adapter);

                handler.post(() -> {
                    if (dlg != null) {
                        Log.d(getString(R.string.tag), getString(R.string.dismiss_dialog));
                        dlg.dismiss();
                    }
                });

            });
        }catch(Exception e) {
            Log.d("DENNIS_B", "Error loading recycler view " + e.getMessage());
        }
    }

    @Override
    public void downLoadUserData() {
        webServiceClient.downloadUserDataPayload(this);
    }
}