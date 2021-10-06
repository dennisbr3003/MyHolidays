package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.databinding.FragmentFirstBinding;

public class ListFragment extends Fragment {

    private FragmentFirstBinding binding;
    private HolidayListAdapter holidayListAdapter;
    private View v;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}