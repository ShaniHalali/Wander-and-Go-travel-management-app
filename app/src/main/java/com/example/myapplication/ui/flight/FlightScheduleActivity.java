package com.example.myapplication.ui.flight;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class FlightScheduleActivity extends AppCompatActivity {
    private String userId;
    private String flightID;
    private DatabaseReference flightReference;

    private EditText etDepartureAirport, etArrivalAirport, etFlightTitle;
    private Button btnDepartureDate, btnDepartureTime, btnArrivalDate, btnArrivalTime;

    private Calendar departureCalendar; // Keep track of the selected departure date

    // Variables to store user input before saving to Firebase
    private String updatedDepartureAirport;
    private String updatedArrivalAirport;
    private String updatedDepartureDate;
    private String updatedDepartureTime;
    private String updatedArrivalDate;
    private String updatedArrivalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_schedule);

        // Initialize views
        etDepartureAirport = findViewById(R.id.et_departure_airport);
        etArrivalAirport = findViewById(R.id.et_arrival_airport);
        etFlightTitle = findViewById(R.id.flight_Title_Input); // Added for flight title
        btnDepartureDate = findViewById(R.id.btn_departure_date);
        btnDepartureTime = findViewById(R.id.btn_departure_time);
        btnArrivalDate = findViewById(R.id.btn_arrival_date);
        btnArrivalTime = findViewById(R.id.btn_arrival_time);

        // Retrieve FlightID from the intent
        flightID = getIntent().getStringExtra("FLIGHT_ID");

        // Get current user's ID
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get reference to the specific flight in Firebase
        flightReference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId)
                .child("allFlights")
                .child(flightID);

        // Load flight details from Firebase
        loadFlightDetails();

        // Set up listeners to handle user inputs
        setupInputListeners();
    }

    private void loadFlightDetails() {
        flightReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String departureAirport = snapshot.child("departureAirport").getValue(String.class);
                    String arrivalAirport = snapshot.child("arrivalAirport").getValue(String.class);
                    String departureDate = snapshot.child("departureDate").getValue(String.class);
                    String departureTime = snapshot.child("departureTime").getValue(String.class);
                    String arrivalDate = snapshot.child("arrivalDate").getValue(String.class);
                    String arrivalTime = snapshot.child("arrivalTime").getValue(String.class);
                    String flightTitle = snapshot.child("flightTitle").getValue(String.class); // Added for flight title

                    // Update the UI fields
                    etDepartureAirport.setText(departureAirport);
                    etArrivalAirport.setText(arrivalAirport);
                    etFlightTitle.setText(flightTitle); // Set flight title in EditText

                    btnDepartureDate.setText(TextUtils.isEmpty(departureDate) ? "Select Departure Date" : departureDate);
                    btnDepartureTime.setText(TextUtils.isEmpty(departureTime) ? "Select Departure Time" : departureTime);
                    btnArrivalDate.setText(TextUtils.isEmpty(arrivalDate) ? "Select Arrival Date" : arrivalDate);
                    btnArrivalTime.setText(TextUtils.isEmpty(arrivalTime) ? "Select Arrival Time" : arrivalTime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FlightScheduleActivity.this, "Failed to load flight details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupInputListeners() {
        etDepartureAirport.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updatedDepartureAirport = etDepartureAirport.getText().toString().trim();
            }
        });

        etArrivalAirport.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updatedArrivalAirport = etArrivalAirport.getText().toString().trim();
            }
        });

        etFlightTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // Nothing specific to do here since the title will be updated in saveAllDetailsToFirebase
            }
        });

        btnDepartureDate.setOnClickListener(v -> openDepartureDatePicker());

        btnArrivalDate.setOnClickListener(v -> openArrivalDatePicker());

        btnDepartureTime.setOnClickListener(v -> openDepartureTimePicker());

        btnArrivalTime.setOnClickListener(v -> openArrivalTimePicker());
    }

    private void openDepartureDatePicker() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Open DatePickerDialog with the current date and a minimum date of today
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                FlightScheduleActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Update the departure calendar
                    departureCalendar = Calendar.getInstance();
                    departureCalendar.set(year1, monthOfYear, dayOfMonth);

                    // Store the selected date locally
                    updatedDepartureDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    btnDepartureDate.setText(updatedDepartureDate);
                },
                year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void openArrivalDatePicker() {
        if (departureCalendar == null) {
            Toast.makeText(this, "Please select a departure date first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Open DatePickerDialog with a minimum date of the selected departure date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                FlightScheduleActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Store the selected date locally
                    updatedArrivalDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    btnArrivalDate.setText(updatedArrivalDate);
                },
                departureCalendar.get(Calendar.YEAR),
                departureCalendar.get(Calendar.MONTH),
                departureCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(departureCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void openDepartureTimePicker() {
        // Get the current time to set as the default value in the TimePickerDialog
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog that allows the user to select a time for departure
        TimePickerDialog timePickerDialog = new TimePickerDialog(FlightScheduleActivity.this,
                (view, selectedHour, selectedMinute) -> {
                    // Format the selected time and store it locally
                    updatedDepartureTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    btnDepartureTime.setText(updatedDepartureTime);
                }, hour, minute, true);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    private void openArrivalTimePicker() {
        // Get the current time to set as the default value in the TimePickerDialog
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog that allows the user to select a time for arrival
        TimePickerDialog timePickerDialog = new TimePickerDialog(FlightScheduleActivity.this,
                (view, selectedHour, selectedMinute) -> {
                    // Format the selected time and store it locally
                    updatedArrivalTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    btnArrivalTime.setText(updatedArrivalTime);
                }, hour, minute, true);

        // Show the TimePickerDialog
        timePickerDialog.show();
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
            saveAllDetailsToFirebase();
            message("Data Saved");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAllDetailsToFirebase() {
        // Ensure Firebase reference is not null
        if (flightReference == null) {
            Toast.makeText(this, "Firebase reference is null", Toast.LENGTH_SHORT).show();
            return;
        }

        // Collect and validate user input
        updatedDepartureAirport = etDepartureAirport.getText().toString().trim();
        updatedArrivalAirport = etArrivalAirport.getText().toString().trim();
        String updatedFlightTitle = etFlightTitle.getText().toString().trim(); // Get flight title input

        // Update Firebase with local values
        flightReference.child("departureAirport").setValue(updatedDepartureAirport)
                .addOnFailureListener(e -> {
                    Toast.makeText(FlightScheduleActivity.this, "Failed to update departure airport", Toast.LENGTH_SHORT).show();
                });

        flightReference.child("arrivalAirport").setValue(updatedArrivalAirport)
                .addOnFailureListener(e -> {
                    Toast.makeText(FlightScheduleActivity.this, "Failed to update arrival airport", Toast.LENGTH_SHORT).show();
                });

        if (updatedDepartureDate != null) {
            flightReference.child("departureDate").setValue(updatedDepartureDate)
                    .addOnFailureListener(e -> {
                        Toast.makeText(FlightScheduleActivity.this, "Failed to update departure date", Toast.LENGTH_SHORT).show();
                    });
        }

        if (updatedDepartureTime != null) {
            flightReference.child("departureTime").setValue(updatedDepartureTime)
                    .addOnFailureListener(e -> {
                        Toast.makeText(FlightScheduleActivity.this, "Failed to update departure time", Toast.LENGTH_SHORT).show();
                    });
        }

        if (updatedArrivalDate != null) {
            flightReference.child("arrivalDate").setValue(updatedArrivalDate)
                    .addOnFailureListener(e -> {
                        Toast.makeText(FlightScheduleActivity.this, "Failed to update arrival date", Toast.LENGTH_SHORT).show();
                    });
        }

        if (updatedArrivalTime != null) {
            flightReference.child("arrivalTime").setValue(updatedArrivalTime)
                    .addOnFailureListener(e -> {
                        Toast.makeText(FlightScheduleActivity.this, "Failed to update arrival time", Toast.LENGTH_SHORT).show();
                    });
        }

        if (!updatedFlightTitle.isEmpty()) {
            flightReference.child("flightTitle").setValue(updatedFlightTitle)
                    .addOnFailureListener(e -> {
                        Toast.makeText(FlightScheduleActivity.this, "Failed to update flight title", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    public void message(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

    }
}