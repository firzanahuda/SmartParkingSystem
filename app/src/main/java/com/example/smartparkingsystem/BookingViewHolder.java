package com.example.smartparkingsystem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

public class BookingViewHolder extends RecyclerView.ViewHolder {

    private final TextView lblInputCarPlate, lblInputVehicle, lblInputStart, lblInputEnd;

    public BookingViewHolder(@NonNull View itemView) {
        super(itemView);
        this.lblInputCarPlate = itemView.findViewById(R.id.lblInputCarPlate);
        this.lblInputVehicle = itemView.findViewById(R.id.lblInputVehicle);
        this.lblInputStart = itemView.findViewById(R.id.lblInputStart);
        this.lblInputEnd = itemView.findViewById(R.id.lblInputEnd);
    }


    public void setBooking(BookingClass bookingClass){

        lblInputCarPlate.setText(bookingClass.getTextInputCarPlate());
        lblInputVehicle.setText(bookingClass.getTextInputVehicle());
        lblInputStart.setText(bookingClass.getTextInputStart());
        lblInputEnd.setText(bookingClass.getTextInputEnd());

    }

}