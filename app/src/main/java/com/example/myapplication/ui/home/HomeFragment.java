package com.example.myapplication.ui.home;

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
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private List<String> tripList; // השתמש במודל Trip האמיתי שלך

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView and adapter
        recyclerView = root.findViewById(R.id.list_LST_trips); // ווידא ש-ID זה תואם ל-XML
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tripList = new ArrayList<>();
        tripAdapter = new TripAdapter(tripList, position -> {
            // Handle the long click event
            tripList.remove(position);
            tripAdapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "Trip removed", Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(tripAdapter);

        // Button to add new trip
        com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton addButton = root.findViewById(R.id.list_BTN_planner); // ווידא ש-ID זה תואם ל-XML
        addButton.setOnClickListener(v -> {
            // Add logic to add new trip to the list
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
