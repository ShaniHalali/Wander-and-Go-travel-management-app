package com.example.myapplication.ui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class DailyScheduleActivity extends AppCompatActivity {
    private TextInputEditText day_TIN_morning;
    private TextInputEditText day_TIN_noon;
    private TextInputEditText day_TIN_evening;
    private TextInputEditText day_TIN_reervations;
    private TextInputEditText day_TIN_notes;
    private String dayName;
    private TextView tv_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_schedule);
        // Get the day name from the intent
        Intent intent = getIntent();
        dayName = intent.getStringExtra("DAY_NAME");

        findViews();
        initViews();
    }

    private void findViews() {
        // Initialize the TextInputEditText
        day_TIN_morning = findViewById(R.id.day_TIN_morning);
        day_TIN_noon = findViewById(R.id.day_TIN_noon);
        day_TIN_evening = findViewById(R.id.day_TIN_evening);
        day_TIN_reervations = findViewById(R.id.day_TIN_reservations);
        day_TIN_notes = findViewById(R.id.day_TIN_notes);
        tv_day = findViewById(R.id.tv_day);
    }

    private void initViews() {
        if(dayName !=null){
            tv_day.setText(dayName);
        }
        day_TIN_morning.setText("HELLO");
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
