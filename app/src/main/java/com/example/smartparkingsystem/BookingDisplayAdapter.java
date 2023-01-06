package com.example.smartparkingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Vector;

public class BookingDisplayAdapter extends RecyclerView.Adapter<BookingDisplayAdapter.BookingDisplayHolder>{


    private Context mCtx;
    private List<BookingDisplayClass> bookingDisplayList;

    public BookingDisplayAdapter(Context mCtx, List<BookingDisplayClass> bookingDisplayList) {
        this.mCtx = mCtx;
        this.bookingDisplayList = bookingDisplayList;
    }

    @NonNull
    @Override
    public BookingDisplayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.platecar_list_item, null);
        return new BookingDisplayHolder(view);


        //return new BookingDisplayHolder(layoutInflater.inflate(R.layout.platecar_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingDisplayHolder holder, int position) {

        BookingDisplayClass bookingClass = bookingDisplayList.get(position);

        holder.textStartInput.setText(bookingClass.getTextInputStart());
        holder.textEndInput.setText(bookingClass.getTextInputEnd());
        holder.textVehiclePNInput.setText(bookingClass.getTextInputCarPlate());
        holder.textVehicleTypeInput.setText(bookingClass.getTextInputVehicle());
        holder.textStartTimeInput.setText(bookingClass.getTextInputStartTime());
        holder.textEndTimeInput.setText(bookingClass.getTextInputEndTime());
        holder.textStation.setText(bookingClass.getStation());

    }

    @Override
    public int getItemCount() {
        return bookingDisplayList.size();
    }

    class BookingDisplayHolder extends RecyclerView.ViewHolder {

        TextView textStartInput , textEndInput, textStartTimeInput, textEndTimeInput , textVehiclePNInput , textVehicleTypeInput, textStation;

        public BookingDisplayHolder(View itemView) {
            super(itemView);

            textStartInput = itemView.findViewById(R.id.textStartInput);
            textEndInput = itemView.findViewById(R.id.textEndInput);
            textStartTimeInput = itemView.findViewById(R.id.textStartTimeInput);
            textEndTimeInput = itemView.findViewById(R.id.textEndTimeInput);
            textVehiclePNInput = itemView.findViewById(R.id.textVehiclePNInput);
            textVehicleTypeInput = itemView.findViewById(R.id.textVehicleTypeInput);
            textStation = itemView.findViewById(R.id.textStation);


        }
    }
}
