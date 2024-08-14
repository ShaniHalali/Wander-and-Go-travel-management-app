package com.example.myapplication.ui.packingList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Models.PackingItems;
import com.example.myapplication.databinding.FragmentPackingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PackingListFragment extends Fragment {

    private FragmentPackingBinding binding;
    private ArrayList<String> packingList;
    private ArrayAdapter<String> adapter;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PackingListViewModel packingListViewModel =
                new ViewModelProvider(this).get(PackingListViewModel.class);

        binding = FragmentPackingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("PackingItems");

        // Initialize packing list and adapter
        packingList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, packingList);
        ListView listView = binding.packingList;
        listView.setAdapter(adapter);

        // Load existing items from Firebase
        loadItemsFromFirebase();

        // Set up EditText and Button
        EditText editText = binding.packingEditText;
        Button addButton = binding.buttonAddPacking;

        // Add button onClickListener
        addButton.setOnClickListener(v -> {
            String item = editText.getText().toString();
            if (!item.isEmpty()) {
                saveItemToFirebase(item);
                packingList.add(item);
                adapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        // Set up ListView item long click listener for deleting an item
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            String item = packingList.get(position);
            removeItemFromFirebase(item);
            packingList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), item + " deleted", Toast.LENGTH_SHORT).show();
            return true;
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Load items from Firebase and update the list
    private void loadItemsFromFirebase() {
        databaseReference.child("allItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                packingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PackingItems packingItem = snapshot.getValue(PackingItems.class);
                    if (packingItem != null) {
                        packingList.add(packingItem.getItem());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Save item to Firebase under "allItems" using the item name as the key
    private void saveItemToFirebase(String item) {
        databaseReference.child("allItems").child(item).setValue(new PackingItems().setItem(item));
    }

    // Remove item from Firebase under "allItems"
    private void removeItemFromFirebase(String item) {
        databaseReference.child("allItems").child(item).removeValue();
    }
}