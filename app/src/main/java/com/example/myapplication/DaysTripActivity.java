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
import com.example.myapplication.ui.ui.TripScheduleActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class DaysTripActivity extends AppCompatActivity {
    private DayAdapter dayAdapter;
    private List<String> daysList;
    int currentDay=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_trip);

        // Get the trip name from the intent
        Intent intent = getIntent();
        String tripName = intent.getStringExtra("trip_name");
        // Use tripName as needed

        // Initialize RecyclerView and Adapter
        daysList = new ArrayList<>(); // Initialize with any existing data if needed
        dayAdapter = new DayAdapter(this, daysList, position -> {
            // Handle long click to remove item
            dayAdapter.removeItem(position);
        }, day -> {
            // Handle schedule button click event
            Intent scheduleIntent = new Intent(DaysTripActivity.this, TripScheduleActivity.class);
            scheduleIntent.putExtra("DAY_NAME", day); // Ensure "DAY_NAME" is the same key used in TripScheduleActivity
            startActivity(scheduleIntent);

        });

        RecyclerView recyclerView = findViewById(R.id.trip_LST_days);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Ensure the LayoutManager is set
        recyclerView.setAdapter(dayAdapter);

        // Set up Add Day button
        MaterialButton addDayButton = findViewById(R.id.addDayButton);
        addDayButton.setOnClickListener(v -> addNewDay());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_done){
            message("Done");
        }
        return super.onOptionsItemSelected(item);
    }

    public void message(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void addNewDay() {
        // Generate a new day item, here it's just a placeholder
        String newDay = "Day " + dayNumber();
        daysList.add(newDay);
        dayAdapter.notifyItemInserted(daysList.size() - 1); // Notify adapter of the new item
    }

    private int dayNumber() {
        return currentDay++;
    }
}
