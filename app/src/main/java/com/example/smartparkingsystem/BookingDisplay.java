package com.example.smartparkingsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;

import android.util.Log;
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

    //TextView textStartInput, textEndInput, textVehiclePNInput, textVehicleTypeInput, textStartTimeInput, textEndTimeInput;
    //FetchBookingAdapter fetchBookingAdapter;
    //public static ArrayList<BookingClass> bookingClassArrayList = new ArrayList<>();

    List<BookingDisplayClass> bookingDisplayList;
    RecyclerView recyclerView;
    //BookingClass booking;
    private User user;
    String username, station;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_display);


        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookingDisplayList = new ArrayList<>();

        /*fetchBookingAdapter = new FetchBookingAdapter(this, bookingClassArrayList);
        textStartInput = findViewById(R.id.textStartInput);
        textEndInput = findViewById(R.id.textEndInput);
        textVehiclePNInput = findViewById(R.id.textVehiclePNInput);
        textVehicleTypeInput = findViewById(R.id.textVehicleTypeInput);
        textEndTimeInput = findViewById(R.id.textEndTimeInput);
        textStartTimeInput = findViewById(R.id.textStartTimeInput);*/



        retrieveData();

    }


    public void retrieveData(){

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

                            //creating adapter object and setting it to recyclerview
                            BookingDisplayAdapter adapter = new BookingDisplayAdapter(BookingDisplay.this, bookingDisplayList);
                            recyclerView.setAdapter(adapter);
                            /*
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("booking");

                            if(success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    String carPlate = obj.getString("Plate_Number");
                                    String startDate = obj.getString("Starting_Date");
                                    String endDate = obj.getString("End_Date");
                                    String duration = obj.getString("Duration");
                                    String startTime = obj.getString("Start_Time");
                                    String endTime = obj.getString("End_Time");
                                    String vehicle_Type = obj.getString("Vehicle_Type");
                                    //String station = obj.getString("station");


                                    textStartInput.append(startDate);
                                    textEndInput.append(endDate);
                                    textVehiclePNInput.append(carPlate);
                                    textStartTimeInput.append(startTime);
                                    textEndTimeInput.append(endTime);
                                    textVehicleTypeInput.append(vehicle_Type);


                                    booking = new BookingClass(startDate, endDate, carPlate, duration);
                                    bookingClassArrayList.add(booking);
                                    fetchBookingAdapter.notifyDataSetChanged();*/

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
    }
}
