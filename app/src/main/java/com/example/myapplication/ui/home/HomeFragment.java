package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.TripAdapter;
import com.example.myapplication.DaysTripActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.ui.TripScheduleActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private List<String> tripList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView and adapter
        recyclerView = root.findViewById(R.id.list_LST_trips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tripList = new ArrayList<>();

        tripAdapter = new TripAdapter(tripList,
                position -> {
                    // Handle the long click event
                    if (position >= 0 && position < tripList.size()) {
                        tripList.remove(position);
                        tripAdapter.notifyItemRemoved(position);
                        // Notify the adapter about changes
                        tripAdapter.notifyItemRangeChanged(position, tripList.size());
                        Toast.makeText(getContext(), "Trip removed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Invalid position", Toast.LENGTH_SHORT).show();
                    }
                },
                position -> {
                    // Handle the schedule button click
                    Intent intent = new Intent(getContext(), DaysTripActivity.class);
                    Toast.makeText(getContext(), "Schedule Activity", Toast.LENGTH_SHORT).show();
                    intent.putExtra("trip_name", tripList.get(position)); // Pass the trip name to the new activity
                    startActivity(intent);
                }
        );

        recyclerView.setAdapter(tripAdapter);

        // Button to add new trip
        com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton addButton = root.findViewById(R.id.list_BTN_planner);
        addButton.setOnClickListener(v -> {
            addNewTrip("New Trip");
            Toast.makeText(getContext(), "New trip added", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    private void addNewTrip(String tripName) {
        tripList.add(tripName);
        tripAdapter.notifyItemInserted(tripList.size() - 1);
    }
}
