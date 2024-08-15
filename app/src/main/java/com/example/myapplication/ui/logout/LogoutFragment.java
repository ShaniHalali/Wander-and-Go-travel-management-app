package com.example.myapplication.ui.logout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentMyTripsBinding;
import com.example.myapplication.ui.myTrips.MyTripsViewModel;

public class LogoutFragment  extends Fragment {

    private FragmentMyTripsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyTripsViewModel homeViewModel =
                new ViewModelProvider(this).get(MyTripsViewModel.class);

        binding = FragmentMyTripsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
