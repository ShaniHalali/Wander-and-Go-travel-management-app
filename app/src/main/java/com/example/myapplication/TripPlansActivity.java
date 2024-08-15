package com.example.myapplication;

import static com.example.myapplication.Data.DataManager.NewDailyDay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.DayAdapter;
import com.example.myapplication.Data.DataManager;
import com.example.myapplication.Models.DailySchedule;
import com.example.myapplication.ui.ui.DailyScheduleActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TripPlansActivity extends AppCompatActivity {
    private DayAdapter dayAdapter;
    private List<String> daysList;
    private TextInputEditText trip_Title_Input;
    private RecyclerView trip_LST_days;
    private ExtendedFloatingActionButton day_BTN_dayEdit;
    private String tripID;
    private DatabaseReference tripRef;
    private int count = 1;
    private DailySchedule newDaySchedule;
    private String newDayTitle;

    private String tripDestination;
    private DatabaseReference tripDestinationRef;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_plans);

        // Get the tripID from the intent
        tripID = getIntent().getStringExtra("Trips");
        Log.d("trips1", tripID);

        // Initialize views and UI components
        findViews();
        initViews();

        // Get user ID from Firebase authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        }

        // Initialize the list and adapter for the days
        daysList = new ArrayList<>();
        dayAdapter = new DayAdapter(this, daysList, tripID, this::removeDayFromFirebase, (day) -> {
            // Handle the daily schedule button click event
            Intent scheduleIntent = new Intent(this, DailyScheduleActivity.class);
            Log.d("trips4", tripID);
            scheduleIntent.putExtra("DAY_NAME", day);
            scheduleIntent.putExtra("TRIP", tripID);
            startActivity(scheduleIntent);
        });

        trip_LST_days.setLayoutManager(new LinearLayoutManager(this));
        trip_LST_days.setAdapter(dayAdapter);

        // Set up the Add Day button
        MaterialButton addDayButton = findViewById(R.id.addDayButton);
        addDayButton.setOnClickListener(v -> addNewDay());

        // Fetch days and trip destination from Firebase
        fetchDaysFromFirebase();
    }

    private void initViews() {
        // Set the trip destination to the input field
        trip_Title_Input.setText(tripDestination);
    }

    private void findViews() {
        // Link the XML components to Java objects
        trip_Title_Input = findViewById(R.id.trip_Title_Input);
        trip_LST_days = findViewById(R.id.trip_LST_days);
        day_BTN_dayEdit = findViewById(R.id.day_BTN_dayEdit);
    }

    private void fetchDaysFromFirebase() {
        // Initialize Firebase references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tripRef = database.getReference("users").child(userID).child("Trips").child(tripID).child("allDays");
        tripDestinationRef = database.getReference("users").child(userID).child("Trips").child(tripID).child("tripDestination");

        // Fetch the tripDestination value from Firebase
        tripDestinationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tripDestination = snapshot.getValue(String.class);
                if (tripDestination != null) {
                    trip_Title_Input.setText(tripDestination);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load trip destination: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch the days associated with the trip from Firebase
        tripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> newDaysList = new ArrayList<>();
                for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                    String dayName = daySnapshot.child("dayTitle").getValue(String.class);
                    if (dayName != null && !newDaysList.contains(dayName)) {
                        newDaysList.add(dayName);
                    }
                }

                // Clear existing list and update with new data
                daysList.clear();
                daysList.addAll(newDaysList);
                dayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load days: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewDay() {
        // Generate the new day title
        newDayTitle = "Day " + (daysList.size() + 1);

        if (daysList.contains(newDayTitle)) {
            newDayTitle = "New Day " + count;
            count++;
        }

        // Create a new daily schedule object
        newDaySchedule = DataManager.NewDailyDay(newDayTitle);

        // Save the new day to Firebase
        tripRef.child(newDayTitle).setValue(newDaySchedule)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Add new day directly to the list and notify adapter
                        if (!daysList.contains(newDayTitle)) {
                            daysList.add(newDayTitle);
                            dayAdapter.notifyItemInserted(daysList.size() - 1);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to add day: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeDayFromFirebase(int position) {
        // Remove a day from Firebase and update the list
        if (position >= 0 && position < daysList.size()) {
            String dayToRemove = daysList.get(position);
            tripRef.child(dayToRemove).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Fetch days again to update the list
                            fetchDaysFromFirebase();
                            Toast.makeText(getApplicationContext(), "Day removed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to remove day: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.item_done) {
            // Get the new trip destination from the input field
            String newTripDestination = trip_Title_Input.getText().toString().trim();

            if (!newTripDestination.isEmpty()) {
                // Update Firebase Database with the new trip destination
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("Trips").child(tripID);

                // Update the tripDestination field
                databaseRef.child("tripDestination").setValue(newTripDestination)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Update the local tripDestination variable
                                tripDestination = newTripDestination;

                                // Show a success message
                                message("Trip destination updated successfully.");
                            } else {
                                message("Failed to update trip destination: " + task.getException().getMessage());
                            }
                        });
            } else {
                message("Please enter a valid trip destination.");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Utility method for showing toast messages
    public void message(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}