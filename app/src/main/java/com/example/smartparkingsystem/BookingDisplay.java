package com.example.smartparkingsystem;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

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
import java.util.Map;

public class BookingDisplay<HttpClient> extends AppCompatActivity {

    TextView textStartInput, textEndInput, textVehiclePNInput, textVehicleTypeInput, textStartTimeInput, textEndTimeInput;
    FetchBookingAdapter fetchBookingAdapter;
    public static ArrayList<BookingClass> bookingClassArrayList = new ArrayList<>();

    BookingClass booking;
    private User user;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.platecar_list_item);

        fetchBookingAdapter = new FetchBookingAdapter(this, bookingClassArrayList);
        textStartInput = findViewById(R.id.textStartInput);
        textEndInput = findViewById(R.id.textEndInput);
        textVehiclePNInput = findViewById(R.id.textVehiclePNInput);
        textVehicleTypeInput = findViewById(R.id.textVehicleTypeInput);
        textEndTimeInput = findViewById(R.id.textEndTimeInput);
        textStartTimeInput = findViewById(R.id.textStartTimeInput);



        retrieveData();

    }


    public void retrieveData(){

        String url = "http://192.168.8.122/loginregister/getDataBooking.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        bookingClassArrayList.clear();
                        try{

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


                                    textStartInput.append(startDate);
                                    textEndInput.append(endDate);
                                    textVehiclePNInput.append(carPlate);
                                    textStartTimeInput.append(startTime);
                                    textEndTimeInput.append(endTime);
                                    textVehicleTypeInput.append(vehicle_Type);


                                    booking = new BookingClass(startDate, endDate, carPlate, duration);
                                    bookingClassArrayList.add(booking);
                                    fetchBookingAdapter.notifyDataSetChanged();

                                }
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
    }
}
