package com.example.smartparkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDisplay<HttpClient> extends AppCompatActivity {

    TextView textStartInput, textEndInput, textVehiclePNInput, textVehicleTypeInput, textStartTimeInput, textEndTimeInput, textStation, inputtotalPrice;
    double total;
    BookingDisplayClass booking;
    //BookingClass booking;
    private User user;
    String username, station;
    String totalPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.platecar_list_item);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.mybutton);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        textStartInput = findViewById(R.id.textStartInput);
        textEndInput = findViewById(R.id.textEndInput);
        textVehiclePNInput = findViewById(R.id.textVehiclePNInput);
        textVehicleTypeInput = findViewById(R.id.textVehicleTypeInput);
        textStartTimeInput = findViewById(R.id.textStartTimeInput);
        textEndTimeInput = findViewById(R.id.textEndTimeInput);
        textStation = findViewById(R.id.textStation);
        inputtotalPrice = findViewById(R.id.inputtotalPrice);

        String start = BookingDisplayClass.getInstance().getTextInputStart();
        String end = BookingDisplayClass.getInstance().getTextInputEnd();
        String startTime = BookingDisplayClass.getInstance().getTextInputStartTime();
        String endTime = BookingDisplayClass.getInstance().getTextInputEndTime();
        String carPlate = BookingDisplayClass.getInstance().getTextInputCarPlate();
        String type = BookingDisplayClass.getInstance().getTextInputVehicle();
        String station = BookingDisplayClass.getInstance().getStation();
        Long duration = BookingDisplayClass.getInstance().getDuration();

        if(duration <= 7){
            total = duration * 1;
        }
        else if(duration>7 && duration <= 12){
            total = 10;
        }
        else if(duration >= 24){
            total = 20;
        }

        totalPrice = "RM " + total;

        textStartInput.append(start);
        textEndTimeInput.append(endTime);
        textStartTimeInput.append(startTime);
        textEndInput.append(end);
        textVehiclePNInput.append(carPlate);
        textVehicleTypeInput.append(type);
        textStation.append(station);
        inputtotalPrice.append(totalPrice);

        sendData();

    }

    // this event will enable the back
    // function to the button on press


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        return true;
    }

    public void sendData(){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String carPlate = BookingDisplayClass.getInstance().getTextInputCarPlate();
                String bookingID = User.getInstance().getBookingID();

                String status = "Not Paid";
                username = User.getInstance().getUsername();
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[5];
                field[0] = "total";
                field[1] = "username";
                field[2] = "status";
                field[3] = "carPlate";
                field[4] = "bookingID";
                //Creating array for data
                String[] data = new String[5];
                data[0] = totalPrice;
                data[1] = username;
                data[2] = status;
                data[3] = carPlate;
                data[4] = bookingID;

                PutData putData = new PutData("http://192.168.8.122/loginregister/paymentBooking.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Data save")){
                            //Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                            Log.e("anyText", result);
                        }
                    }
                }
                //End Write and Read data with URL
            }
        });

    }

}
