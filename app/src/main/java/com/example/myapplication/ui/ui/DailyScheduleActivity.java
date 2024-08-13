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
    private String tripKey;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_schedule);

        // Get tripKey and dayName from intent
        tripKey = getIntent().getStringExtra("TRIP");
        dayName = getIntent().getStringExtra("DAY_NAME");

        Log.d("tripKey", "Trip Key: " + tripKey);
        Log.d("DailyScheduleActivity", "Day Name: " + dayName);

        // Check if the inputs are null
        if (tripKey == null) {
            Toast.makeText(this, "Trip key is null", Toast.LENGTH_SHORT).show();
        }
        if (dayName == null) {
            Toast.makeText(this, "Day name is null", Toast.LENGTH_SHORT).show();
        }

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
    }

    private void initViews() {
        if (tripKey != null) {
            tv_day.setText(dayName);
        }
    }

    private void setupFirebase() {
        if (tripKey != null && dayName != null) {
            // Set up database reference to the specific day within the specific trip
            databaseReference = FirebaseDatabase.getInstance().getReference("Trips")
                    .child(tripKey).child("allDays").child(dayName.toLowerCase());
            Log.d("SetupFirebase", "DataSnapshot received for tripKey: " + tripKey);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("LIOR", dayName );
                    Log.d("LIOR3", tripKey );

                    if (dataSnapshot.exists()) {
                        Log.d("LIOR2", tripKey );
                        // Extract data and update the views
                        String morningSchedule = dataSnapshot.child("morningSchedule").getValue(String.class);
                        String noonSchedule = dataSnapshot.child("noonSchedule").getValue(String.class);
                        String eveningSchedule = dataSnapshot.child("eveningSchedule").getValue(String.class);
                        String reservations = dataSnapshot.child("reservations").getValue(String.class);
                        String notes = dataSnapshot.child("notes").getValue(String.class);

                        if (morningSchedule != null) {
                            Log.d("moriningggg", morningSchedule);
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
            message("Done");
        }
        return super.onOptionsItemSelected(item);
    }

    public void message(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}