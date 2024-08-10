package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private List<String> daysList;
    private OnItemLongClickListener onItemLongClickListener;
    private OnScheduleClickListener onScheduleClickListener;

    public DayAdapter(List<String> daysList, OnItemLongClickListener onItemLongClickListener, OnScheduleClickListener onScheduleClickListener) {
        this.daysList = daysList;
        this.onItemLongClickListener = onItemLongClickListener;
        this.onScheduleClickListener = onScheduleClickListener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_days_item, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        String day = daysList.get(position);
        holder.dayName.setText(day);

        // Handle long click to remove item
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(position);
                return true; // Return true to indicate the long click event is consumed
            }
            return false;
        });

        // Handle schedule button click
        holder.dayScheduleButton.setOnClickListener(v -> {
            if (onScheduleClickListener != null) {
                onScheduleClickListener.onScheduleClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return daysList.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < daysList.size()) {
            daysList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, daysList.size()); // Update item positions
        }
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {

        TextView dayName;
        com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton dayScheduleButton;

        DayViewHolder(View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.day_LBL_name);
            dayScheduleButton = itemView.findViewById(R.id.day_BTN_dayEdit);
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public interface OnScheduleClickListener {
        void onScheduleClick(int position);
    }
}
