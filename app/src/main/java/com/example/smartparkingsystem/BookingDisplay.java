package com.example.smartparkingsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextParams;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingDisplay<HttpClient> extends AppCompatActivity {

    TextView textStartInput, textEndInput, textVehiclePNInput, textVehicleTypeInput, textView;
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



        retrieveData();

    }


    public void retrieveData(){

        String url = "http://192.168.8.122/loginregister/getData.php";
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

                                    textStartInput.append(startDate);
                                    textEndInput.append(endDate);
                                    textVehiclePNInput.append(carPlate);


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
            params.put("selectFn", "fnSaveData");
            params.put("Customer_Username", username);

            return params;

        }

    };



        requestQueue.add(request);
    }
}
