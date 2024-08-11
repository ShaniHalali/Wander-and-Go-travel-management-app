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
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private List<String> tripList;
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
        tripAdapter = new TripAdapter(tripList,
                position -> {
                    // Handle the long click event
                    if (position >= 0 && position < tripList.size()) {
                        tripName = tripList.get(position);
                        tripList.remove(position);
                        tripAdapter.notifyItemRemoved(position);
                        tripAdapter.notifyItemRangeChanged(position, tripList.size());
                        Toast.makeText(getContext(), tripName + " removed", Toast.LENGTH_SHORT).show();
                        myRef.child(tripName).removeValue();

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
                    tripName = tripList.get(position); // Get the trip name from the clicked position
                    Toast.makeText(getContext(), "Schedule Activity", Toast.LENGTH_SHORT).show();
                    intent.putExtra("trip_name", tripName); // Pass the trip name to the new activity
                    startActivity(intent);
                }
        );

        recyclerView.setAdapter(tripAdapter);

        // Fetch trips from Firebase
        fetchTripsFromFirebase();

        // MARK: Button to add new trip
        com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton addButton = root.findViewById(R.id.list_BTN_planner);
        addButton.setOnClickListener(v -> {
            String tripName = "Trip " + nextTripNumber++;
            addNewTrip(tripName);
            saveDataToFirebase(tripName);
        });

        return root;
    }

    private void fetchTripsFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Trips");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TripItem> trips = new ArrayList<>();
                for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                    String tripName = tripSnapshot.getKey();
                    try {
                        int tripNumber = Integer.parseInt(tripName.replace("Trip ", ""));
                        trips.add(new TripItem(tripName, tripNumber));
                    } catch (NumberFormatException e) {
                        // Handle the case where the trip name does not follow the expected format
                        e.printStackTrace();
                    }
                }

                // Sort trips based on their numeric value
                trips.sort((t1, t2) -> Integer.compare(t1.getTripNumber(), t2.getTripNumber()));

                // Clear and update the trip list
                tripList.clear();
                for (TripItem trip : trips) {
                    tripList.add(trip.getTripName());
                }
                tripAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed

                // Update the nextTripNumber to be one more than the maximum existing trip number
                if (!trips.isEmpty()) {
                    nextTripNumber = trips.get(trips.size() - 1).getTripNumber() + 1;
                } else {
                    nextTripNumber = 1; // Reset counter if there are no trips
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch trips: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewTrip(String tripName) {
        tripList.add(tripName);
        tripAdapter.notifyItemInserted(tripList.size() - 1);
    }

    private void saveDataToFirebase(String tripName) {
        DatabaseReference myRefUnderTrips1 = myRef.child(tripName);

        myRefUnderTrips1.setValue(DataManager.createTripsWithDailySchedules())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "New trip added", Toast.LENGTH_SHORT).show();
                        myRef.child(tripName).child("tripDestination").setValue(tripName);
                    } else {
                        Toast.makeText(getContext(), "Failed to save data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static class TripItem {
        private final String tripName;
        private final int tripNumber;

        public TripItem(String tripName, int tripNumber) {
            this.tripName = tripName;
            this.tripNumber = tripNumber;
        }

        public String getTripName() {
            return tripName;
        }

        public int getTripNumber() {
            return tripNumber;
        }
    }
}
