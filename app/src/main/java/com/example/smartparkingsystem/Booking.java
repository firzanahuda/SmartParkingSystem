package com.example.smartparkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smartparkingsystem.databinding.ActivityBookingBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Calendar;
import java.util.Vector;


public class Booking extends AppCompatActivity {

    TextInputEditText textInputCarPlate, textInputVehicle, textInputStart, textInputEnd;
    Button buttonViewBooking;
    LinearLayout linearLayout;
    private BookingClass bookings;
    private Vector<BookingClass> booking;
    private ActivityBookingBinding binding;
    private BookingAdapter adapter;
    private DatePickerDialog datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linearLayout = findViewById(R.id.linear_layout);

        booking = new Vector<>();
        adapter = new BookingAdapter(getLayoutInflater(),booking);

        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textInputCarPlate = findViewById(R.id.carPlate);
        textInputVehicle = findViewById(R.id.vehicle);
        textInputStart = findViewById(R.id.start);
        textInputEnd = findViewById(R.id.end);
        buttonViewBooking = findViewById(R.id.buttonViewBooking);

        binding.buttonViewBooking.setOnClickListener(this:: fnAdd);

        binding.start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    fnInvokeDatePicker();
                }

                if(!hasFocus){
                    fnFormValidation();
                }
            }
        });

        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnInvokeDatePicker();
            }
        });

        binding.end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    fnInvokeDatePickerEnd();
                }

                if(!hasFocus){
                    fnFormValidation();
                }
            }
        });

        binding.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnInvokeDatePickerEnd();
            }
        });

        binding.rcvStud.setAdapter(adapter);
        binding.rcvStud.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(binding.rcvStud);

    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            booking.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
        }
    };

    private void fnFormValidation() {
    }

    private void fnInvokeDatePicker()
    {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog

        datePicker = new DatePickerDialog(Booking.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.start.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        },year,month,day);
        datePicker.show();
    }

    private void fnInvokeDatePickerEnd()
    {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog

        datePicker = new DatePickerDialog(Booking.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.end.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        },year,month,day);
        datePicker.show();
    }

    private void fnAdd(View view)
    {
        textInputCarPlate = findViewById(R.id.carPlate);
        textInputVehicle = findViewById(R.id.vehicle);
        textInputStart = findViewById(R.id.start);
        textInputEnd = findViewById(R.id.end);

        String carPlate, vehicle, start, end;

        carPlate = String.valueOf(textInputCarPlate.getText().toString());
        vehicle = String.valueOf(textInputVehicle.getText().toString());
        start = String.valueOf(textInputStart.getText().toString());
        end = String.valueOf(textInputEnd.getText().toString());

        bookings = new BookingClass(carPlate,vehicle,start,end);

        booking.add(bookings);
        adapter.notifyItemInserted(booking.size());

        if(!carPlate.equals("") && !vehicle.equals("") && !start.equals("") && !end.equals("")) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[4];
                    field[0] = "carPlate";
                    field[1] = "vehicle";
                    field[2] = "start";
                    field[3] = "end";
                    //Creating array for data
                    String[] data = new String[4];
                    data[0] = carPlate;
                    data[1] = vehicle;
                    data[2] = start;
                    data[3] = end;

                    PutData putData = new PutData("http://192.168.8.122/loginregister/booking.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if(result.equals("Booking Success!")){
                                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                //startActivity(intent);
                                //finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    //End Write and Read data with URL
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "All Fields Required !", Toast.LENGTH_SHORT).show();
        }



    }


}