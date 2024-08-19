package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Flight;
import com.example.myapplication.R;

import java.util.ArrayList;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private ArrayList<Flight> flightList;
    private OnItemLongClickListener longClickListener;
    private OnItemEditClickListener editClickListener;

    // Define an interface for the long-click listener
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    // Define an interface for the edit button click listener
    public interface OnItemEditClickListener {
        void onItemEditClick(int position);
    }

    public FlightAdapter(ArrayList<Flight> flightList, OnItemLongClickListener longClickListener, OnItemEditClickListener editClickListener) {
        this.flightList = flightList;
        this.longClickListener = longClickListener;
        this.editClickListener = editClickListener;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_flights_item, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.bind(flight);

        // Set up the long-click listener
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(position);
            return true; // Return true to indicate the long-click was handled
        });

        // Set up the edit button click listener
        holder.flightEditButton.setOnClickListener(v -> {
            editClickListener.onItemEditClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        private TextView flightLabelName;
        private Button flightEditButton; // Adjust according to your layout

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightLabelName = itemView.findViewById(R.id.flight_LBL_name); // Adjust according to your layout
            flightEditButton = itemView.findViewById(R.id.flight_BTN_flightEdit); // Adjust according to your layout
        }

        public void bind(Flight flight) {
            flightLabelName.setText(flight.getFlightTitle()); // Bind the flight title instead of description
        }
    }
}