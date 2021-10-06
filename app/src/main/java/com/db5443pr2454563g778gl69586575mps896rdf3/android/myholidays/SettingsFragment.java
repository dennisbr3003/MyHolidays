package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.databinding.FragmentSecondBinding;

public class SettingsFragment extends Fragment {

    private FragmentSecondBinding binding;
    private IActivityEventListener activityEventListener;
    SharedRef rf = new SharedRef();

    public IActivityEventListener getActivityEventListener() {
        return activityEventListener;
    }

    public void setActivityEventListener(IActivityEventListener activityEventListener) {
        this.activityEventListener = activityEventListener;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Add listeners to avoid the underlying fragment to be clickable or touchable -->
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return v;

    }

    @Override
    public void onPause() {
        super.onPause();
        if(getActivity() != null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // to gain access to options menu --> and clear it subsequently


    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        try {
            RadioGroup radioGroup = view.findViewById(R.id.rgRegion);

            int count = radioGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View o = radioGroup.getChildAt(i);
                if (o instanceof RadioButton) {
                    RadioButton selectedAnswer = (RadioButton) o;
                    if (selectedAnswer.getText().equals(rf.getSharedRefRegion(getContext()))) {
                        selectedAnswer.setChecked(true);
                    }
                }
            }

            String[] parts = rf.getSharedRefSchoolYear(getContext()).split("-");
            TextView txtStart = (TextView) view.findViewById(R.id.editTextNumber3);
            txtStart.setText(parts[0]);
            TextView txtEnd = (TextView) view.findViewById(R.id.editTextNumber5);
            txtEnd.setText(parts[1]);

        }catch(Exception e){
            Log.d("DENNIS_B", "error getting preferences " + e.getMessage());
        }


        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    RadioGroup rg = getView().findViewById(R.id.rgRegion);
                    RadioButton radioButton = (RadioButton) getView().findViewById(rg.getCheckedRadioButtonId());
                    Log.d("DENNIS_B", "radioButton.getText().toString() " + radioButton.getText().toString());
                    rf.setSharedRefRegion(getContext(), radioButton.getText().toString());

                    TextView txtStart = (TextView) getView().findViewById(R.id.editTextNumber3);
                    TextView txtEnd = (TextView) getView().findViewById(R.id.editTextNumber5);
                    Log.d("DENNIS_B", "txtStart-txtEnd " + txtStart.getText().toString() + "-" + txtEnd.getText().toString());
                    rf.setSharedRefSchoolYear(getContext(),txtStart.getText().toString() + "-" + txtEnd.getText().toString());
                    /* reload data on save */
                    if (activityEventListener != null) {
                        Log.d("DENNIS_B", "sending data to activityEventListener");
                        activityEventListener.downLoadUserData();
                    }

                    getActivity().onBackPressed();
                } catch (Exception e) {
                    Log.d("DENNIS_B", "error saving preferences " + e.getMessage());
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}