package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<String> tripList;
    private OnItemLongClickListener onItemLongClickListener;
    private OnScheduleClickListener onScheduleClickListener;

    public TripAdapter(List<String> tripList, OnItemLongClickListener onItemLongClickListener, OnScheduleClickListener onScheduleClickListener) {
        this.tripList = tripList;
        this.onItemLongClickListener = onItemLongClickListener;
        this.onScheduleClickListener = onScheduleClickListener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_trips_item, parent, false);
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
                return true; // Return true to indicate the long click event is consumed
            }
            return false;
        });

        // Handle schedule button click
        holder.scheduleButton.setOnClickListener(v -> {
            if (onScheduleClickListener != null) {
                onScheduleClickListener.onScheduleClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < tripList.size()) {
            tripList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, tripList.size());
        }
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {

        TextView tripName;
        com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton scheduleButton;

        TripViewHolder(View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.trips_LBL_name);
            scheduleButton = itemView.findViewById(R.id.list_BTN_scedule);
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public interface OnScheduleClickListener {
        void onScheduleClick(int position);
    }
}
