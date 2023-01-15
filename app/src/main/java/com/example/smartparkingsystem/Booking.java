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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Booking extends AppCompatActivity {

    TextInputEditText textInputCarPlate, textInputVehicle, textInputStart, textInputEnd;
    TextView textStartTime, textEndTime;
    Button buttonViewBooking, buttonConfirm;
    LinearLayout linearLayout;
    TextView tvStart, tvEnd;
    int startHour, startMinute, endHour, endMinute;
    private User user;
    String username, station;

    private BookingClass bookings;
    private Vector<BookingClass> booking;
    private Vector<BookingDisplayClass> bookingDisplay;
    private ActivityBookingBinding binding;
    private BookingAdapter adapter;
    private DatePickerDialog datePicker;

    String[] strCarPlate;
    String[] items = {"Small", "Large"};
    AutoCompleteTextView autoCompleteTextView, completeTextView;
    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterCarplate;
    String item;
    String carplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linearLayout = findViewById(R.id.linear_layout);

        booking = new Vector<>();
        adapter = new BookingAdapter(getLayoutInflater(), booking);

        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //textInputCarPlate = findViewById(R.id.carPlate);
        //textInputVehicle = findViewById(R.id.vehicle);
        textInputStart = findViewById(R.id.start);
        textInputEnd = findViewById(R.id.end);
        buttonViewBooking = findViewById(R.id.buttonViewBooking);
        tvStart = findViewById(R.id.startTime);
        tvEnd = findViewById(R.id.endTime);
        textStartTime = findViewById(R.id.startTime);
        textEndTime = findViewById(R.id.endTime);

        retrieveData();

        autoCompleteTextView = findViewById(R.id.auto_complete_text);
        adapterItems = new ArrayAdapter<String>(this, R.layout.item_list, items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }
        });


        buttonConfirm = findViewById(R.id.buttonConfirm);

        binding.buttonViewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fnRecyclerView();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    fnAdd();
                    Intent intent = new Intent(Booking.this, BookingDisplay.class);
                    startActivity(intent);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        binding.start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    Intent intent = new Intent(Booking.this, StartDate.class);
                    startActivityForResult(intent, 1);
                }

                if (!hasFocus) {
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

                if (hasFocus) {
                    Intent intent = new Intent(Booking.this, EndDate.class);
                    startActivityForResult(intent, 2);
                }

                if (!hasFocus) {
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


    public void retrieveData() {

        String url = "http://192.168.8.122/loginregister/getDataCarPlate.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("carPlate");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            String carPlate1 = obj.getString("Plate_Number1");
                            String carPlate2 = obj.getString("Plate_Number2");
                            String carPlate3 = obj.getString("Plate_Number3");
                            String carPlate4 = obj.getString("Plate_Number4");
                            String carPlate5 = obj.getString("Plate_Number5");

                            strCarPlate = new String[5];
                            strCarPlate[0] = carPlate1;
                            strCarPlate[1] = carPlate2;
                            strCarPlate[2] = carPlate3;
                            strCarPlate[3] = carPlate4;
                            strCarPlate[4] = carPlate5;

                            completeTextView = findViewById(R.id.complete_text);
                            adapterCarplate = new ArrayAdapter<>(Booking.this, R.layout.item_car_plate, strCarPlate);

                            completeTextView.setAdapter(adapterCarplate);
                            completeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    carplate = parent.getItemAtPosition(position).toString();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Booking.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                username = User.getInstance().getUsername();

                Map<String, String> params = new HashMap<>();
                params.put("Customer_Username", username);

                return params;

            }

        };

        requestQueue.add(request);
    }


    private void fnInvokeTimePickerEnd() {
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
                            Date dateEnd = f24Hours.parse(time);
                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm:aa"
                            );
                            tvEnd.setText(f12Hours.format(dateEnd));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 12, 0, false
        );

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();


    }

    private void fnInvokeTimePicker() {
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
                }, 12, 0, false
        );

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                textInputStart.setText(result);
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultEnd");
                textInputEnd.setText(result);
            }
        }
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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

    private void fnRecyclerView() throws ParseException {

        //textInputCarPlate = findViewById(R.id.carPlate);
        //textInputVehicle = findViewById(R.id.vehicle);
        textInputStart = findViewById(R.id.start);
        textInputEnd = findViewById(R.id.end);
        tvStart = findViewById(R.id.startTime);
        tvEnd = findViewById(R.id.endTime);

        username = User.getInstance().getUsername();

        String carPlate, vehicle, start, end, startTime, endTime;

        carPlate = carplate;
        vehicle = item;
        start = String.valueOf(textInputStart.getText().toString());
        end = String.valueOf(textInputEnd.getText().toString());
        startTime = String.valueOf(tvStart.getText().toString());
        endTime = String.valueOf(tvEnd.getText().toString());


        // Creating a SimpleDateFormat object
        // to parse time in the format HH:MM:SS

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:aa");

        String calculateStart = start + " " + startTime;
        String calculateEnd = end + " " + endTime;

        // Parsing the Time Period
        Date dateStart = simpleDateFormat.parse(calculateStart);
        Date dateEnd = simpleDateFormat.parse(calculateEnd);

        // Calculate time difference
        // in milliseconds
        long different = dateEnd.getTime() - dateStart.getTime();

        // Calculate time difference in
        // seconds, minutes, hours, years,
        // and days

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        //long elapsedDays = different / daysInMilli;
        //different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String duration = elapsedHours + " hours " + elapsedMinutes + " minutes ";


        bookings = new BookingClass(carPlate, vehicle, start, end, duration);

        booking.add(bookings);
        adapter.notifyItemInserted(booking.size());

    }

    private void fnAdd() throws ParseException {
        //textInputCarPlate = findViewById(R.id.carPlate);
        //textInputVehicle = findViewById(R.id.vehicle);
        textInputStart = findViewById(R.id.start);
        textInputEnd = findViewById(R.id.end);
        tvStart = findViewById(R.id.startTime);
        tvEnd = findViewById(R.id.endTime);

        username = User.getInstance().getUsername();
        station = BookingClass.getInstance().getStation();

        String carPlate, vehicle, start, end, startTime, endTime;

        carPlate = carplate;
        vehicle = item;
        start = String.valueOf(textInputStart.getText().toString());
        end = String.valueOf(textInputEnd.getText().toString());
        startTime = String.valueOf(tvStart.getText().toString());
        endTime = String.valueOf(tvEnd.getText().toString());


        // Creating a SimpleDateFormat object
        // to parse time in the format HH:MM:SS
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:aa");

        String calculateStart = start + " " + startTime;
        String calculateEnd = end + " " + endTime;

        // Parsing the Time Period
        Date dateStart = simpleDateFormat.parse(calculateStart);
        Date dateEnd = simpleDateFormat.parse(calculateEnd);

        // Calculate time difference
        // in milliseconds
        long different = dateEnd.getTime() - dateStart.getTime();

        // Calculate time difference in
        // seconds, minutes, hours, years,
        // and days

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        //long elapsedDays = different / daysInMilli;
        //different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String duration = elapsedHours + " hours " + elapsedMinutes + " minutes ";


        BookingDisplayClass.getInstance().setTextInputCarPlate(carPlate);
        BookingDisplayClass.getInstance().setTextInputStart(start);
        BookingDisplayClass.getInstance().setTextInputEnd(end);
        BookingDisplayClass.getInstance().setTextInputStartTime(startTime);
        BookingDisplayClass.getInstance().setTextInputEndTime(endTime);
        BookingDisplayClass.getInstance().setStation(station);
        BookingDisplayClass.getInstance().setTextInputVehicle(vehicle);
        BookingDisplayClass.getInstance().setDuration(elapsedHours);


        if (!carPlate.equals("") && !vehicle.equals("") && !start.equals("") && !end.equals("")) {

            ArrayList<Integer> bookIDIntList = new ArrayList<>();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "http://192.168.8.122/loginregister/getNewBookID.php";
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if (response.equals("No booking yet")) {
                            bookIDIntList.add(0);
                        } else {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject bookObj = jsonArray.getJSONObject(i);
                                //JSONObject scanObj = response.getJSONObject(i);
                                String bookID = bookObj.getString("ID");
                                // get substr and convert to int
                                bookID = bookID.substring(1);
                                bookID = removeZero(bookID);
                                int number = Integer.parseInt(bookID);

                                bookIDIntList.add(number);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // get the largest number and create the new ID
                    int max = Collections.max(bookIDIntList) + 1;
                    String bookingID;
                    if (max >= 10000 && max <= 99999) {
                        bookingID = "B" + Integer.toString(max);
                    } else if (max >= 1000 && max < 10000) {
                        bookingID = "B0" + Integer.toString(max);
                    } else if (max >= 100 && max < 1000) {
                        bookingID = "B00" + Integer.toString(max);
                    } else if (max >= 10 && max < 100) {
                        bookingID = "B000" + Integer.toString(max);
                    } else {
                        bookingID = "B0000" + Integer.toString(max);
                    }

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[10];
                            field[0] = "carPlate";
                            field[1] = "vehicle";
                            field[2] = "start";
                            field[3] = "end";
                            field[4] = "duration";
                            field[5] = "username";
                            field[6] = "startTime";
                            field[7] = "endTime";
                            field[8] = "station";
                            field[9] = "bookingID";

                            //Creating array for data
                            String[] data = new String[10];
                            data[0] = carPlate;
                            data[1] = vehicle;
                            data[2] = start;
                            data[3] = end;
                            data[4] = duration;
                            data[5] = username;
                            data[6] = startTime;
                            data[7] = endTime;
                            data[8] = station;
                            data[9] = bookingID;



                            //bookingDisplay.add(bookings);

                            PutData putData = new PutData("http://192.168.8.122/loginregister/booking.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Booking Success!")) {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        //startActivity(intent);
                                        //finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "All Fields Required !", Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(jsonObjectRequest);

        }
    }

    // remove the leading zero of string during retrieve of scan ID
    public static String removeZero(String str) {

        // Count leading zeros

        // Initially setting loop counter to 0
        int i = 0;
        while (i < str.length() && str.charAt(i) == '0')
            i++;

        // Converting string into StringBuffer object
        // as strings are immutable
        StringBuffer sb = new StringBuffer(str);

        // The StringBuffer replace function removes
        // i characters from given index (0 here)
        sb.replace(0, i, "");

        // Returning string after removing zeros
        return sb.toString();
    }
}