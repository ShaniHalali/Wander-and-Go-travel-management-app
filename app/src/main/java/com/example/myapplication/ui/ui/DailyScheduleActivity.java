package com.example.myapplication.ui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.DailySchedule;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DailyScheduleActivity extends AppCompatActivity {
    private TextInputEditText day_TIN_morning;
    private TextInputEditText day_TIN_noon;
    private TextInputEditText day_TIN_evening;
    private TextInputEditText day_TIN_reservations;
    private TextInputEditText day_TIN_notes;
    private String dayName;
    private TextView tv_day;
    private TextView tv_trip;
    private String tripKey;
    private String userId;
    private DatabaseReference databaseReference;
    private DatabaseReference dataTripDestination;
    private String tripDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_schedule);

        // Get current user's ID
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get tripKey and dayName from intent
        tripKey = getIntent().getStringExtra("TRIP");
        dayName = getIntent().getStringExtra("DAY_NAME");

        Log.d("tripKey", "Trip Key: " + tripKey);
        Log.d("DailyScheduleActivity", "Day Name: " + dayName);

        findViews();
        initViews();

        // Setup Firebase
        setupFirebase();
    }

    private void findViews() {
        day_TIN_morning = findViewById(R.id.day_TIN_morning);
        day_TIN_noon = findViewById(R.id.day_TIN_noon);
        day_TIN_evening = findViewById(R.id.day_TIN_evening);
        day_TIN_reservations = findViewById(R.id.day_TIN_reservations);
        day_TIN_notes = findViewById(R.id.day_TIN_notes);
        tv_day = findViewById(R.id.tv_day);
        tv_trip = findViewById(R.id.tv_trip);
    }

    private void initViews() {
        if (dayName != null) {
            tv_day.setText(dayName.toUpperCase());
            Toast.makeText(this, dayName, Toast.LENGTH_SHORT).show();
        }
        if (tripKey != null) {
            dataTripDestination = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("Trips").child(tripKey).child("tripDestination");

            dataTripDestination.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        tripDestination = dataSnapshot.getValue(String.class);

                        if (tripDestination != null) {
                            tv_trip.setText(tripDestination.toUpperCase());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    message("Failed to load data.");
                }
            });

        }

    }

    private void setupFirebase() {
        if (tripKey != null && dayName != null) {
            // Set up database reference to the specific day within the specific trip for the current user
            databaseReference = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("Trips").child(tripKey).child("allDays").child(dayName);
            Log.d("SetupFirebase", "DataSnapshot received for tripKey: " + tripKey);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("dayName", dayName);
                    Log.d("tripKey", tripKey);

                    if (dataSnapshot.exists()) {
                        Log.d("tripKeyExists", tripKey);
                        // Extract data and update the views
                        String morningSchedule = dataSnapshot.child("morningSchedule").getValue(String.class);
                        String noonSchedule = dataSnapshot.child("noonSchedule").getValue(String.class);
                        String eveningSchedule = dataSnapshot.child("eveningSchedule").getValue(String.class);
                        String reservations = dataSnapshot.child("reservations").getValue(String.class);
                        String notes = dataSnapshot.child("notes").getValue(String.class);

                        if (morningSchedule != null) {
                            Log.d("morning", morningSchedule);
                            day_TIN_morning.setText(morningSchedule);
                        }
                        if (noonSchedule != null) {
                            day_TIN_noon.setText(noonSchedule);
                        }
                        if (eveningSchedule != null) {
                            day_TIN_evening.setText(eveningSchedule);
                        }
                        if (reservations != null) {
                            day_TIN_reservations.setText(reservations);
                        }
                        if (notes != null) {
                            day_TIN_notes.setText(notes);
                        }
                    } else {
                        Toast.makeText(DailyScheduleActivity.this, "No data found for the day", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    message("Failed to load data.");
                }
            });
        } else {
            Toast.makeText(this, "Trip key or day name is null", Toast.LENGTH_SHORT).show();
        }
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
            saveDataToFirebase();
            message("Data Saved");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDataToFirebase() {
        String morningSchedule = day_TIN_morning.getText().toString().trim();
        String noonSchedule = day_TIN_noon.getText().toString().trim();
        String eveningSchedule = day_TIN_evening.getText().toString().trim();
        String reservations = day_TIN_reservations.getText().toString().trim();
        String notes = day_TIN_notes.getText().toString().trim();

        if (tripKey != null && dayName != null) {
            // Set up database reference to the specific day within the specific trip for the current user
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("Trips").child(tripKey).child("allDays").child(dayName);

            // Saving each value directly to Firebase
            databaseReference.child("morningSchedule").setValue(morningSchedule);
            databaseReference.child("noonSchedule").setValue(noonSchedule);
            databaseReference.child("eveningSchedule").setValue(eveningSchedule);
            databaseReference.child("reservations").setValue(reservations);
            databaseReference.child("notes").setValue(notes)
                    .addOnSuccessListener(aVoid -> {
                        message("Daily schedule saved successfully");
                    })
                    .addOnFailureListener(e -> {
                        message("Failed to save daily schedule");
                        Log.e("FirebaseError", "Error saving daily schedule", e);
                    });
        } else {
            Toast.makeText(this, "Trip key or day name is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void message(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}