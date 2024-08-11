package com.example.myapplication.ui.tripPlanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Data.DataManager;
import com.example.myapplication.databinding.FragmentTripPlannerBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TripPlannerFragment extends Fragment {

    private FragmentTripPlannerBinding binding;
    private TextInputEditText main_ET_text;
    private MaterialButton main_BTN_updateText;
    private MaterialButton main_BTN_save;
    private TextView main_LBL_title;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TripPlannerViewModel tripPlannerViewModel =
                new ViewModelProvider(this).get(TripPlannerViewModel.class);

        binding = FragmentTripPlannerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        main_ET_text = binding.mainETText;
        main_BTN_updateText = binding.mainBTNUpdateText;
        main_BTN_save = binding.mainBTNSave;
        main_LBL_title = binding.mainLBLTitle;

        //tripPlannerViewModel.getText().observe(getViewLifecycleOwner(), main_LBL_title::setText);
        updateTitleFromDB();

        main_BTN_updateText.setOnClickListener(v -> {
            String text = main_ET_text.getText().toString();
            if (!text.isEmpty()) {
                setLable(text);
                updateTitleFromDB();
            } else {
                Toast.makeText(getContext(), "Text field is empty", Toast.LENGTH_SHORT).show();
            }
        });

        main_BTN_save.setOnClickListener(v -> {
            saveDataToFirebase();
            Toast.makeText(getContext(), "Data Saved", Toast.LENGTH_SHORT).show();
        });



        return root;
    }

    private void updateTitleFromDB() {
        DatabaseReference titleRef = FirebaseDatabase.getInstance().getReference("title");
        //  titleRef.addListenerForSingleValueEvent( //For one time fetching from DB
        titleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value == null || value.equals("")) {
                    main_LBL_title.setText("Hello World!");
                } else {
                    main_LBL_title.setText(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load title: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setLable(String string) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("title");
        myRef.setValue(string);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void saveDataToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("trips");
        DatabaseReference myRefUnderTrips = myRef.child("trip3");
        DatabaseReference myRefUnderTrips1 = myRef.child("trip1");


        myRefUnderTrips1.setValue(DataManager.createTripsWithDailySchedules())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Data successfully saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to save data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
