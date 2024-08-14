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
import com.example.myapplication.Data.DataManager;
import com.example.myapplication.TripPlansActivity;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private List<String> tripList;
    private Map<String, String> tripIdToDestinationMap; // New map to store trip IDs and destinations
    private DatabaseReference myRef;
    private int nextTripNumber = 1; // Counter to track the next unique trip number
    private String tripName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView and adapter
        recyclerView = root.findViewById(R.id.list_LST_trips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tripList = new ArrayList<>();
        tripIdToDestinationMap = new HashMap<>(); // Initialize the map
        tripAdapter = new TripAdapter(tripList,
                position -> {
                    // Handle the long click event
                    if (position >= 0 && position < tripList.size()) {
                        String tripId = getKeyByValue(tripIdToDestinationMap, tripList.get(position));
                        tripList.remove(position);
                        tripAdapter.notifyItemRemoved(position);
                        tripAdapter.notifyItemRangeChanged(position, tripList.size());
                        Toast.makeText(getContext(), tripId + " removed", Toast.LENGTH_SHORT).show();
                        myRef.child(tripId).removeValue();

                        // Check if the list is empty after removal
                        if (tripList.isEmpty()) {
                            nextTripNumber = 1; // Reset counter when the list is empty
                        }
                    } else {
                        Toast.makeText(getContext(), "Invalid position", Toast.LENGTH_SHORT).show();
                    }
                },
                position -> {
                    // Handle the schedule button click
                    Intent intent = new Intent(getContext(), TripPlansActivity.class);
                    String tripDestination = tripList.get(position); // Get the trip destination from the clicked position
                    String tripId = getKeyByValue(tripIdToDestinationMap, tripDestination);
                    Toast.makeText(getContext(), "Schedule Activity", Toast.LENGTH_SHORT).show();
                    intent.putExtra("Trips", tripId); // Pass the trip ID to the new activity
                    startActivity(intent);
                }
        );

        recyclerView.setAdapter(tripAdapter);

        // Fetch trips from Firebase
        fetchTripsFromFirebase();

        // MARK: Button to add new trip
        com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton addButton = root.findViewById(R.id.list_BTN_planner);
        addButton.setOnClickListener(v -> {
            String tripId = "Trip " + nextTripNumber++;
           if(addNewTrip(tripId)) {
               saveDataToFirebase(tripId);
           }

        });

        return root;
    }

    private void fetchTripsFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Trips");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tripList.clear(); // Clear the existing trip list
                tripIdToDestinationMap.clear(); // Clear the map
                for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                    String tripId = tripSnapshot.getKey();
                    String tripDestination = tripSnapshot.child("tripDestination").getValue(String.class);
                    if (tripDestination != null) {
                        tripList.add(tripDestination); // Add each trip destination to the list
                        tripIdToDestinationMap.put(tripId, tripDestination); // Map the trip ID to the destination
                    }
                }

                tripAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed

                // Update nextTripNumber if needed
                nextTripNumber = tripList.size() + 1; // Or any other logic based on your needs
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch trips: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean addNewTrip(String tripName) {
        if (tripList.contains(tripName)) {
            // If the trip name already exists in the list
            Toast.makeText(getContext(), "'"+ tripName +"' already exists,Please change name", Toast.LENGTH_SHORT).show();
            return false;

        }
            // If the trip name does not exist, add it to the list
            tripList.add(tripName);
            tripAdapter.notifyItemInserted(tripList.size() - 1);
            Toast.makeText(getContext(), "Trip added successfully", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void saveDataToFirebase(String tripName) {
        DatabaseReference myRefUnderTrips1 = myRef.child(tripName);

        myRefUnderTrips1.setValue(DataManager.createTripsWithDailySchedules())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                      //  Toast.makeText(getContext(), "New trip added", Toast.LENGTH_SHORT).show();
                        myRef.child(tripName).child("tripDestination").setValue(tripName);
                    } else {
                        Toast.makeText(getContext(), "Failed to save data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getKeyByValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}