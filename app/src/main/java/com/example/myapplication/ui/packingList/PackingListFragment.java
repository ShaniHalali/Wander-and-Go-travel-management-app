package com.example.myapplication.ui.packingList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentPackingBinding;

public class PackingListFragment extends Fragment {

    private FragmentPackingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PackingListViewModel galleryViewModel =
                new ViewModelProvider(this).get(PackingListViewModel.class);

        binding = FragmentPackingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPacking;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}