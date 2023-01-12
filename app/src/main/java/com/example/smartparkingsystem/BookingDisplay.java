package com.example.smartparkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDisplay<HttpClient> extends AppCompatActivity {

    TextView textStartInput, textEndInput, textVehiclePNInput, textVehicleTypeInput, textStartTimeInput, textEndTimeInput, textStation;

    BookingDisplayClass booking;
    //BookingClass booking;
    private User user;
    String username, station;


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

        String start = BookingDisplayClass.getInstance().getTextInputStart();
        String end = BookingDisplayClass.getInstance().getTextInputEnd();
        String startTime = BookingDisplayClass.getInstance().getTextInputStartTime();
        String endTime = BookingDisplayClass.getInstance().getTextInputEndTime();
        String carPlate = BookingDisplayClass.getInstance().getTextInputCarPlate();
        String type = BookingDisplayClass.getInstance().getTextInputVehicle();
        String station = BookingDisplayClass.getInstance().getStation();

        textStartInput.append(start);
        textEndTimeInput.append(endTime);
        textStartTimeInput.append(startTime);
        textEndInput.append(end);
        textVehiclePNInput.append(carPlate);
        textVehicleTypeInput.append(type);
        textStation.append(station);







    }

    // this event will enable the back
    // function to the button on press


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        return true;
    }

    /*public void retrieveData(){

        String url = "http://192.168.8.122/loginregister/getDataBooking.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{

                            Log.e("anyText",response);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject booking = array.getJSONObject(i);

                                //adding the product to product list
                                bookingDisplayList.add(new BookingDisplayClass(
                                        booking.getString("Plate_Number"),
                                        booking.getString("Starting_Date"),
                                        booking.getString("End_Date"),
                                        booking.getString("Start_Time"),
                                        booking.getString("End_Time"),
                                        booking.getString("Vehicle_Type"),
                                        booking.getString("Station")
                                ));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookingDisplay.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {


            username = User.getInstance().getUsername();

            Map<String, String> params = new HashMap< >();
            //params.put("selectFn", "fnSaveData");
            params.put("Customer_Username", username);

            return params;

        }

    };



        requestQueue.add(request);
    }*/
}
