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

import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.databinding.FragmentSecondBinding;

public class SettingsFragment extends Fragment implements ISharedRef {

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
            setRadioGroup(view.findViewById(R.id.rgRegion), SHAREDREF_REGION, "text");
            initializePeriodFields(true);
        }catch(Exception e){
            Log.d(getString(R.string.tag), getString(R.string.preferences_error) + e.getMessage());
        }


        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    RadioButton radioButton;
                    RadioGroup radioGroup;

                    radioGroup = getView().findViewById(R.id.rgRegion);
                    radioButton = getView().findViewById(radioGroup.getCheckedRadioButtonId());
                    rf.setSharedRef(getContext(), SHAREDREF_REGION, radioButton.getText().toString());

                    TextView txtStart = (TextView) getView().findViewById(R.id.txtYearStart);
                    TextView txtEnd = (TextView) getView().findViewById(R.id.txtYearEnd);

                    Result result = checkSettings(txtStart.getText().toString() + "-" + txtEnd.getText().toString());
                    if (result != null && !result.isResult()){
                        // This method returns an AlertDialog so you can access methods directly after the method call
                        Message.ErrorDialog.getErrorMessageDialog(getContext(), result).show();
                        return;
                    }

                    Log.d(getString(R.string.tag), getString(R.string.txtstart_end) + txtStart.getText().toString() + "-" + txtEnd.getText().toString());
                    rf.setSharedRefSchoolYear(getContext(), txtStart.getText().toString() + "-" + txtEnd.getText().toString());

                    /* reload data on save */
                    if (activityEventListener != null) {
                        Log.d(getString(R.string.tag), getString(R.string.activity_event_listener_data));
                        activityEventListener.downLoadUserData();
                    }

                    getActivity().onBackPressed();

                } catch (Exception e) {
                    Log.d(getString(R.string.tag), getString(R.string.preferences_save_error) + e.getMessage());
                }

            }
        });
    }

    private void setRadioGroup(RadioGroup rg, String field, String property){

        int count = rg.getChildCount();

        for (int i = 0; i < count; i++) {
            View o = rg.getChildAt(i);
            if (o instanceof RadioButton) {
                RadioButton selectedAnswer = (RadioButton) o;
                switch(property){
                    case "tag":
                        if (selectedAnswer.getTag().equals(rf.getSharedRef(getContext(), field))) {
                            Log.d(getString(R.string.tag), getString(R.string.default_tag) + rf.getSharedRef(getContext(), field));
                            selectedAnswer.setChecked(true);
                        }
                        break;
                    case "text":
                        if (selectedAnswer.getText().equals(rf.getSharedRef(getContext(), field))) {
                            Log.d(getString(R.string.tag), getString(R.string.default_text) + rf.getSharedRef(getContext(), field));
                            selectedAnswer.setChecked(true);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void initializePeriodFields(boolean enabled){
        if(enabled){

            TextView txtStart = (TextView) getView().findViewById(R.id.txtYearStart);
            TextView txtEnd = (TextView) getView().findViewById(R.id.txtYearEnd);

            txtStart.setEnabled(true);
            txtEnd.setEnabled(true);

            setSchoolYearScreenValue(txtStart, txtEnd);

        } else {

            TextView txtStart = (TextView) getView().findViewById(R.id.txtYearStart);
            txtStart.setText("");

            TextView txtEnd = (TextView) getView().findViewById(R.id.txtYearEnd);
            txtEnd.setText("");

            txtStart.setEnabled(false);
            txtEnd.setEnabled(false);

        }
    }

    private void setSchoolYearScreenValue(TextView txtStart, TextView txtEnd){

        try {
            String[] parts = rf.getSharedRefSchoolYear(getContext()).split("-");
            txtStart.setText(parts[0]);
            txtEnd.setText(parts[1]);
        } catch (Exception e){
            Log.d(getString(R.string.tag), getString(R.string.parts_exception) + e.getMessage());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public Result checkSettings(String period){

        String[] parts = period.split("-");

        try {
            if (Math.abs(Integer.parseInt(parts[0]) - Integer.parseInt(parts[1])) != 1){
                return new Result(false, getString(R.string.invalid_period_interval), getString(R.string.Error_in_settings));
            }

            if (Integer.parseInt(parts[0]) > Integer.parseInt(parts[1])) {
                return new Result(false, getString(R.string.invalid_period_order), getString(R.string.Error_in_settings));
            }
        } catch (Exception e){
            return new Result(false, getString(R.string.unexpected_error) + e.getMessage(), getString(R.string.Error_in_settings));
        }
        return null;
    }

}