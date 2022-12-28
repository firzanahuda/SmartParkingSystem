package com.example.smartparkingsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartparkingsystem.databinding.ActivityBookingBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;


public class Booking extends AppCompatActivity {

    TextInputEditText textInputCarPlate, textInputVehicle, textInputStart, textInputEnd;
    Button buttonViewBooking;
    LinearLayout linearLayout;
    TextView tvStart, tvEnd;
    int startHour, startMinute, endHour, endMinute;
    private User user;
    String username;

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
        tvStart = findViewById(R.id.startTime);
        tvEnd = findViewById(R.id.endTime);

                binding.buttonViewBooking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            fnAdd();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

        binding.start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    Intent intent = new Intent(Booking.this, StartDate.class);
                    startActivityForResult(intent, 1);
                }

                if(!hasFocus){
                    fnFormValidation();
                }
            }
        });

        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking.this, StartDate.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    Intent intent = new Intent(Booking.this, EndDate.class);
                    startActivityForResult(intent, 2);
                }

                if(!hasFocus){
                    fnFormValidation();
                }
            }
        });

        binding.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking.this, EndDate.class);
                startActivityForResult(intent, 2);
            }
        });

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnInvokeTimePicker();
            }
        });

        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnInvokeTimePickerEnd();
            }
        });

        binding.rcvStud.setAdapter(adapter);
        binding.rcvStud.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(binding.rcvStud);

    }

    private void fnInvokeTimePickerEnd(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                Booking.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endHour = hourOfDay;
                        endMinute = minute;
                        String time = endHour + ":" + endMinute;
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date dateEnd= f24Hours.parse(time);
                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm:aa"
                            );
                            tvEnd.setText(f12Hours.format(dateEnd));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },12,0,false
        );

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();


    }

    private void fnInvokeTimePicker(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                Booking.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startHour = hourOfDay;
                        startMinute = minute;
                        String time = startHour + ":" + startMinute;
                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date dateStart = f24Hours.parse(time);
                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm:aa"
                            );
                            tvStart.setText(f12Hours.format(dateStart));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },12,0,false
        );

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode == RESULT_OK){
                String result = data.getStringExtra("result");
                textInputStart.setText(result);
            }
        }

        if(requestCode==2){
            if(resultCode == RESULT_OK){
                String result = data.getStringExtra("resultEnd");
                textInputEnd.setText(result);
            }
        }
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

    private void fnAdd() throws ParseException {
        textInputCarPlate = findViewById(R.id.carPlate);
        textInputVehicle = findViewById(R.id.vehicle);
        textInputStart = findViewById(R.id.start);
        textInputEnd = findViewById(R.id.end);
        tvStart = findViewById(R.id.startTime);
        tvEnd = findViewById(R.id.endTime);

        username = User.getInstance().getUsername();

        String carPlate, vehicle, start, end, startTime, endTime;

        carPlate = String.valueOf(textInputCarPlate.getText().toString());
        vehicle = String.valueOf(textInputVehicle.getText().toString());
        start = String.valueOf(textInputStart.getText().toString());
        end = String.valueOf(textInputEnd.getText().toString());
        startTime = String.valueOf(tvStart.getText().toString());
        endTime = String.valueOf(tvEnd.getText().toString());

        // Creating a SimpleDateFormat object
        // to parse time in the format HH:MM:SS
        SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("HH:mm");

        // Parsing the Time Period
        Date dateStart = simpleDateFormat.parse(startTime);
        Date dateEnd = simpleDateFormat.parse(endTime);

        // Calculating the difference in milliseconds
        long differenceInMilliSeconds
                = Math.abs(dateEnd.getTime() - dateStart.getTime());

        // Calculating the difference in Hours
        long differenceInHours
                = (differenceInMilliSeconds / (60 * 60 * 1000))
                % 24;

        // Calculating the difference in Minutes
        long differenceInMinutes
                = (differenceInMilliSeconds / (60 * 1000)) % 60;

        String duration = differenceInHours + " hours " + differenceInMinutes + " minutes ";

        bookings = new BookingClass(carPlate,vehicle,start,end, duration);

        booking.add(bookings);
        adapter.notifyItemInserted(booking.size());

        if(!carPlate.equals("") && !vehicle.equals("") && !start.equals("") && !end.equals("")) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[6];
                    field[0] = "carPlate";
                    field[1] = "vehicle";
                    field[2] = "start";
                    field[3] = "end";
                    field[4] = "duration";
                    field[5] = "username";
                    //Creating array for data
                    String[] data = new String[6];
                    data[0] = carPlate;
                    data[1] = vehicle;
                    data[2] = start;
                    data[3] = end;
                    data[4] = duration;
                    data[5] = username;

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