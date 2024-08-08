package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<String> tripList; // Use your actual Trip model instead of String
    private OnItemLongClickListener onItemLongClickListener;

    public TripAdapter(List<String> tripList, OnItemLongClickListener onItemLongClickListener) {
        this.tripList = tripList;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_player_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        String trip = tripList.get(position);
        holder.tripName.setText(trip);

        // Handle long click
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(position);
                return true; // Return true to indicate that the long click has been handled
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {

        TextView tripName; // Use actual views from horizontal_player_item layout

        TripViewHolder(View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.trips_LBL_name); // Adjust to match your item layout
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}
