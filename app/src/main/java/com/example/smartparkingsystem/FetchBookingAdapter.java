package com.example.smartparkingsystem;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class FetchBookingAdapter extends ArrayAdapter<BookingClass> {

    Context context;
    List<BookingClass> arrayListBooking;
    public FetchBookingAdapter(@NonNull Context context, List<BookingClass> arrayListBooking) {
        super(context, R.layout.platecar_list_item, arrayListBooking);

        this.context = context;
        this.arrayListBooking = arrayListBooking;
    }
}
