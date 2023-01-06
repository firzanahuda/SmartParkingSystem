package com.example.smartparkingsystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryViewHolder extends RecyclerView.ViewHolder{

    private final TextView startTime, endTime, duration, startDate, endDate, station, carPlate;

    public void setUpcoming(BookingClass bookingClass){

        //startTime.setText(student.getStrFullname());
        //endTime.setText(student.getStrStudNo());
        //duration.setText(student.getStrEmail());
        //startDate.setText(student.getStrBirthdate());
        //endDate.setText(student.getStrGender());
        //station.setText(student.getStrState());
        //carPlate.setText(student.getStrState());

    }

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);


        this.startTime = itemView.findViewById(R.id.startTime);
        this.endTime = itemView.findViewById(R.id.endTime);
        this.duration = itemView.findViewById(R.id.duration);
        this.startDate = itemView.findViewById(R.id.startDate);
        this.endDate = itemView.findViewById(R.id.endDate);
        this.station = itemView.findViewById(R.id.station);
        this.carPlate = itemView.findViewById(R.id.carPlate);
    }
}
