package com.example.myapplication.ui.flight;

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

import com.example.myapplication.Adapter.FlightAdapter;
import com.example.myapplication.Models.Flight;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentFlightBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FlightFragment extends Fragment {

    private FragmentFlightBinding binding;
    private RecyclerView recyclerView;
    private FlightAdapter adapter;
    private ArrayList<Flight> flightList = new ArrayList<>();
    private DatabaseReference allFlightsReference;
    private String userID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFlightBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the current user's ID
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize RecyclerView and adapter
        recyclerView = root.findViewById(R.id.list_LST_flights);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FlightAdapter(flightList, this::onFlightLongClick, this::onFlightEditClick);
        recyclerView.setAdapter(adapter);

        allFlightsReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("allFlights");

        // Load existing flights from Firebase
        loadFlights();

        binding.listBTNPlanner.setOnClickListener(v -> addNewFlight());

        return root;
    }

    private void loadFlights() {
        allFlightsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flightList.clear(); // Clear the existing list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Flight flight = snapshot.getValue(Flight.class);
                    if (flight != null) {
                        flightList.add(flight);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter of data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load flights", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void addNewFlight() {
        generateUniqueFlightDescription(description -> {
            allFlightsReference.child(description).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        // If flight with this description already exists, do not add it
                        Toast.makeText(getContext(), "Flight already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        // Create a new flight with dummy values
                        Flight newFlight = new Flight(
                                description,
                                "",
                                "",
                                "00:00",
                                "",
                                "",
                                "",
                                "00:00",
                                description // Use description for flightDescription field
                        );

                        // Add to Firebase with description as the key
                        Map<String, Object> flightMap = new HashMap<>();
                        flightMap.put("flightTitle", newFlight.getFlightTitle());
                        flightMap.put("flightNumber", newFlight.getFlightNumber());
                        flightMap.put("departureAirport", newFlight.getDepartureAirport());
                        flightMap.put("departureTime", newFlight.getDepartureTime());
                        flightMap.put("departureDate", newFlight.getDepartureDate());
                        flightMap.put("arrivalDate", newFlight.getArrivalDate());
                        flightMap.put("arrivalAirport", newFlight.getArrivalAirport());
                        flightMap.put("arrivalTime", newFlight.getArrivalTime());
                        flightMap.put("flightDescription", newFlight.getFlightDescription()); // Add description to the map

                        allFlightsReference.child(description).setValue(flightMap)
                                .addOnSuccessListener(aVoid -> {
                                    loadFlights(); // Reload flights from Firebase to ensure consistency

                                    Intent intent = new Intent(getContext(), FlightScheduleActivity.class);
                                    intent.putExtra("FLIGHT_ID", description);
                                    intent.putExtra("FLIGHT_DESCRIPTION", newFlight.getFlightDescription());
                                    startActivity(intent);

                                    Toast.makeText(getContext(), "New flight added!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to add flight", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        });
    }


    private void generateUniqueFlightDescription(OnUniqueDescriptionGeneratedListener listener) {
        allFlightsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 1;
                String baseDescription = "Flight ";
                String uniqueDescription;

                // Check for existing flight descriptions and increment counter until unique
                do {
                    uniqueDescription = baseDescription + counter;
                    counter++;
                } while (dataSnapshot.child(uniqueDescription).exists());

                listener.onUniqueDescriptionGenerated(uniqueDescription);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to check existing flights", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Interface for generating unique flight description
    private interface OnUniqueDescriptionGeneratedListener {
        void onUniqueDescriptionGenerated(String uniqueDescription);
    }

    private void onFlightLongClick(int position) {
        Flight flight = flightList.get(position);
        String flightDescription = flight.getFlightDescription();

        // Remove from RecyclerView
        flightList.remove(position);
        adapter.notifyItemRemoved(position);

        // Remove from Firebase
        allFlightsReference.child(flightDescription).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Flight deleted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to delete flight", Toast.LENGTH_SHORT).show());
    }

    private void onFlightEditClick(int position) {
        Flight flight = flightList.get(position);
        String flightID = flight.getFlightDescription();
        String flightDescription = flight.getFlightDescription();

        // Start FlightScheduleActivity and pass the FlightID and FlightDescription
        Intent intent = new Intent(getContext(), FlightScheduleActivity.class);
        intent.putExtra("FLIGHT_ID", flightID);
        intent.putExtra("FLIGHT_DESCRIPTION", flightDescription);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
