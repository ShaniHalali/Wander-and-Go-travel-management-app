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

public class TripScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_schedule);

        // קבלת שם היום שהועבר דרך ה-Intent
        Intent intent = getIntent();
        String dayName = intent.getStringExtra("DAY_NAME");

        // שימוש בשם היום לצורך הצגה ב-TextView
        TextView tvDay = findViewById(R.id.tv_day); // נניח שה-TextView שלך נקרא tv_day
        if (dayName != null) {
            tvDay.setText(dayName);
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
