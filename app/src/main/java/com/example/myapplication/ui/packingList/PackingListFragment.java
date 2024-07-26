package com.example.myapplication.ui.packingList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentPackingBinding;

import java.util.ArrayList;

public class PackingListFragment extends Fragment {

    private FragmentPackingBinding binding;
    private ArrayList<String> packingList;
    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PackingListViewModel packingListViewModel =
                new ViewModelProvider(this).get(PackingListViewModel.class);

        binding = FragmentPackingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        packingList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, packingList);
        ListView listView = binding.packingList;
        listView.setAdapter(adapter);

        EditText editText = binding.packingEditText;
        Button addButton = binding.buttonAddPacking;

        addButton.setOnClickListener(v -> {
            String item = editText.getText().toString();
            if (!item.isEmpty()) {
                packingList.add(item);
                adapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
