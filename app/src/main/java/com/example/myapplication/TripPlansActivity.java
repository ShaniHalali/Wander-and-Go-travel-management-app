package com.example.myapplication;

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
import java.util.List;

public class TripPlansActivity extends AppCompatActivity {
    private DayAdapter dayAdapter;
    private List<String> daysList;
    private int currentDay = 1;
    private TextInputEditText trip_Title_Input;
    private RecyclerView trip_LST_days;
    private ExtendedFloatingActionButton day_BTN_dayEdit;
    private String tripName;
    private DatabaseReference tripRef; // Reference to the trip's node in Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_plans);

        // Get the trip name from the intent
        tripName = getIntent().getStringExtra("trip_name");
        findViews();
        initViews();

        // Initialize RecyclerView and Adapter
        daysList = new ArrayList<>();
        dayAdapter = new DayAdapter(this, daysList, position -> {
            // Handle long click to remove item
            dayAdapter.removeItem(position);
        }, day -> {
            // Handle schedule button click event
            Intent scheduleIntent = new Intent(TripPlansActivity.this, DailyScheduleActivity.class);
            scheduleIntent.putExtra("DAY_NAME", day); // Ensure "DAY_NAME" is the same key used in DailyScheduleActivity
            startActivity(scheduleIntent);
        });

        trip_LST_days.setLayoutManager(new LinearLayoutManager(this)); // Ensure the LayoutManager is set
        trip_LST_days.setAdapter(dayAdapter);

        // Set up Add Day button
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
                daysList.clear(); // Clear the existing list
                for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                    String dayName = daySnapshot.getKey(); // Get the day name
                    daysList.add(dayName);
                }
                dayAdapter.notifyDataSetChanged(); // Notify adapter of data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load days: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_done) {
            message("Done");
        }
        return super.onOptionsItemSelected(item);
    }

    public void message(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void addNewDay() {
        String newDay = "Day " + dayNumber();
        tripRef.child(newDay).setValue(true) // Save new day to Firebase
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        daysList.add(newDay);
                        dayAdapter.notifyItemInserted(daysList.size() - 1); // Notify adapter of the new item
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to add day: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int dayNumber() {
        return currentDay++;
    }
}
