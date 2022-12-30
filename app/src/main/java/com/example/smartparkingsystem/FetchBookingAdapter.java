package com.example.smartparkingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;

public class FetchBookingAdapter extends ArrayAdapter<BookingClass> {

    Context context;
    List<BookingClass> arrayListBooking;
    public FetchBookingAdapter(@NonNull Context context, List<BookingClass> arrayListBooking) {
        super(context, R.layout.platecar_list_item, arrayListBooking);

        this.context = context;
        this.arrayListBooking = arrayListBooking;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.platecar_list_item, null, true);

        TextView textStartInput = view.findViewById(R.id.textStartInput);
        TextView textEndInput = view.findViewById(R.id.textEndInput);
        TextView textStartTimeInput = view.findViewById(R.id.textStartTimeInput);
        TextView textEndTimeInput = view.findViewById(R.id.textEndTimeInput);
        TextView textVehiclePNInput = view.findViewById(R.id.textVehiclePNInput);
        TextView textVehicleTypeInput = view.findViewById(R.id.textVehicleTypeInput);

        return super.getView(position, convertView, parent);
    }
}
