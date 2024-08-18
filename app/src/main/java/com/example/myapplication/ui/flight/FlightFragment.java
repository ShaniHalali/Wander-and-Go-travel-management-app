package com.example.myapplication.ui.flight;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.databinding.FragmentFlightBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class FlightFragment extends Fragment {

    private static final int PICK_FILE_REQUEST = 1;

    private FragmentFlightBinding binding;
    private Calendar departureCalendar;
    private Calendar calendar;
    private Cursor cursor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FlightViewModel myPassportViewModel =
                new ViewModelProvider(this).get(FlightViewModel.class);

        // Inflate the layout using the correct binding
        binding = FragmentFlightBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize departureCalendar
        departureCalendar = Calendar.getInstance();

        // Set up listeners or any other logic you need
        setupListeners();

        return root;
    }

    private void setupListeners() {
        // Handle departure date selection
        binding.btnDepartureDate.setOnClickListener(v -> showDatePickerDialog(departureCalendar, (date, calendar) -> {
            binding.btnDepartureDate.setText(date);
            departureCalendar = calendar; // Save the selected departure date
            Toast.makeText(getContext(), "Departure Date: " + date, Toast.LENGTH_SHORT).show();
        }));

        // Handle arrival date selection
        binding.btnArrivalDate.setOnClickListener(v -> showDatePickerDialog(departureCalendar, (date, calendar) -> {
            binding.btnArrivalDate.setText(date);
            Toast.makeText(getContext(), "Arrival Date: " + date, Toast.LENGTH_SHORT).show();
        }));

        // Handle departure time selection
        binding.btnDepartureTime.setOnClickListener(v -> showTimePickerDialog(departureCalendar, (time) -> {
            binding.btnDepartureTime.setText(time);
            Toast.makeText(getContext(), "Departure Time: " + time, Toast.LENGTH_SHORT).show();
        }));

        // Handle arrival time selection
        binding.btnArrivalTime.setOnClickListener(v -> showTimePickerDialog(departureCalendar, (time) -> {
            binding.btnArrivalTime.setText(time);
            Toast.makeText(getContext(), "Arrival Time: " + time, Toast.LENGTH_SHORT).show();
        }));

        // Handle ticket upload
        binding.btnUploadTicket.setOnClickListener(v -> openFilePicker());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*"); // Allow any file type. You can specify "application/pdf" or "image/*" to restrict to specific types
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();

                // Display the file (e.g., image, PDF)
                displaySelectedFile(fileUri);
            }
        }
    }

    private void displaySelectedFile(Uri fileUri) {
        String mimeType = getContext().getContentResolver().getType(fileUri);

        if (mimeType != null && mimeType.startsWith("image/")) {
            // Load image into ImageView
            binding.ticketImage.setImageURI(fileUri);
            binding.ticketImage.setVisibility(View.VISIBLE);
            binding.ticketText.setVisibility(View.GONE);
        } else if (mimeType != null && mimeType.equals("application/pdf")) {
            // Render PDF to Bitmap and display in ImageView
            try {
                File pdfFile = new File(getContext().getCacheDir(), "temp.pdf");
                try (InputStream inputStream = getContext().getContentResolver().openInputStream(fileUri);
                     OutputStream outputStream = new FileOutputStream(pdfFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                }

                ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
                PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);

                if (pdfRenderer.getPageCount() > 0) {
                    PdfRenderer.Page page = pdfRenderer.openPage(0);
                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    binding.ticketImage.setImageBitmap(bitmap);
                    binding.ticketImage.setVisibility(View.VISIBLE);
                    binding.ticketText.setVisibility(View.GONE);
                    page.close();
                }

                pdfRenderer.close();
                parcelFileDescriptor.close();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Failed to display PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Display file name in TextView
            String fileName = getFileName(fileUri);
            binding.ticketText.setText(fileName);
            binding.ticketText.setVisibility(View.VISIBLE);
            binding.ticketImage.setVisibility(View.GONE);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) { // Check if the column index is valid
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void showDatePickerDialog(Calendar minDate, OnDateSelectedListener listener) {
        // Get current date if minDate is not provided
        calendar = Calendar.getInstance();

        if (minDate != null && minDate.after(calendar)) {
            calendar = minDate;
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Update the calendar with the selected date
                    calendar.set(selectedYear, selectedMonth, selectedDay);

                    // Format the selected date
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    listener.onDateSelected(formattedDate, calendar);
                },
                year, month, day);

        // Set the minimum selectable date to the provided minDate or the current date
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog(Calendar calendar, OnTimeSelectedListener listener) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, selectedHour, selectedMinute) -> {
                    // Format the selected time
                    String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    listener.onTimeSelected(formattedTime);
                },
                hour, minute, true);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up the binding object to prevent memory leaks
    }

    // Interface to handle date selection
    private interface OnDateSelectedListener {
        void onDateSelected(String date, Calendar calendar);
    }

    // Interface to handle time selection
    private interface OnTimeSelectedListener {
        void onTimeSelected(String time);
    }
}