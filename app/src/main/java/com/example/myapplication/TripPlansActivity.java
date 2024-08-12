package com.example.myapplication;

import static com.example.myapplication.Data.DataManager.NewDailyDay;

import android.content.Intent;
import android.os.Bundle;
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
    private String tripName;
    private DatabaseReference tripRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_plans);

        // Get the trip name from the intent
        tripName = getIntent().getStringExtra("trip_name");

        findViews();
        initViews();

        // Initialize the list and adapter for the days
        daysList = new ArrayList<>();
        dayAdapter = new DayAdapter(this, daysList, this::removeDayFromFirebase, day -> {
            // Handle the daily schedule button click event
            Intent scheduleIntent = new Intent(TripPlansActivity.this, DailyScheduleActivity.class);
            scheduleIntent.putExtra("DAY_NAME", day);
            startActivity(scheduleIntent);
        });

        trip_LST_days.setLayoutManager(new LinearLayoutManager(this));
        trip_LST_days.setAdapter(dayAdapter);

        // Set up the Add Day button
        MaterialButton addDayButton = findViewById(R.id.addDayButton);
        addDayButton.setOnClickListener(v -> addNewDay());

        // Fetch days from Firebase
        fetchDaysFromFirebase();
    }

    private void initViews() {
        trip_Title_Input.setText(tripName);
    }

    private void findViews() {
        trip_Title_Input = findViewById(R.id.trip_Title_Input);
        trip_LST_days = findViewById(R.id.trip_LST_days);
        day_BTN_dayEdit = findViewById(R.id.day_BTN_dayEdit);
    }

    private void fetchDaysFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tripRef = database.getReference("Trips").child(tripName).child("allDays");

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
        String newDayTitle = "Day " + (daysList.size() + 1);

        // Create a new DailySchedule object using DataManager
        DailySchedule newDaySchedule = DataManager.NewDailyDay(newDayTitle);

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
        if (position >= 0 && position < daysList.size()) {
            String dayToRemove = daysList.get(position);
            tripRef.child(dayToRemove).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            daysList.remove(position);
                            dayAdapter.notifyItemRemoved(position);
                            dayAdapter.notifyItemRangeChanged(position, daysList.size());
                            Toast.makeText(getApplicationContext(), "Day removed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to remove day: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}