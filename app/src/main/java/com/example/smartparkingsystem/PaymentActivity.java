package com.example.smartparkingsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    String username, carPlate, plateNumber, bookingID;
    String totalPay;
    TextView textplatenumber, parkingstation, start, endTime, duration, parkingslot, penalty;
    TextView normalDay, holiday, normalHour, holidayHour, Total;
    View v;
    Button payment;
    int Tot;
    int jumlah;

    String PublishableKey = "pk_test_51MPSdPDBT7fQ7TQlbaKw4HpmCIfxiRQgIWanGmPWSlGTICOZpH1cmtFATKk5KdEB4Wby2I9Z2L5YBvXTNBW9AROE007JPUNFRK";
    String SecretKey = "sk_test_51MPSdPDBT7fQ7TQlsZgrBBvgoxVKijNU3J9zypgblHSe1Ix5ZvEa8dSFhxODYpvpv1ybRmQnHCyOqf0RrTqGKsXq00eovqtVxh";
    String CustomerID;
    String EphericalKey;
    String ClientSecret;
    PaymentSheet paymentSheet;
    String Normal_Price_Per_Hour, Holiday_Price_Per_Hour, Normal_Price_Per_Day, Holiday_Price_Per_Day, Penalty_Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        plateNumber = getIntent().getStringExtra("carPlate");
        Log.e("carPlate", plateNumber);

        textplatenumber = findViewById(R.id.textPlateNumber);
        parkingstation = findViewById(R.id.parkingStation);
        start = findViewById(R.id.start);
        endTime = findViewById(R.id.endTime);
        //duration = findViewById(R.id.duration);
        parkingslot = findViewById(R.id.parkingslot);
        penalty = findViewById(R.id.penalty);
        payment = findViewById(R.id.pay);

        normalDay = findViewById(R.id.normalDay);
        holiday = findViewById(R.id.holiday);
        normalHour = findViewById(R.id.normalHour);
        holidayHour = findViewById(R.id.holidayhour);
        Total = findViewById(R.id.Total);

        //jumlah = Integer.parseInt(Tot);

        retrieveData();

        PaymentConfiguration.init(getApplicationContext(), PublishableKey);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentFlow();
            }
        });

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            CustomerID = object.getString("id");

                            Toast.makeText(getApplicationContext(), CustomerID, Toast.LENGTH_SHORT).show();

                            getEmphericalKey();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+SecretKey);

                return header;


            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);


    }




    private void paymentFlow() {

        paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration("Learn with me", new PaymentSheet.CustomerConfiguration(
                CustomerID,
                EphericalKey
        )));
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {

        if(paymentSheetResult instanceof PaymentSheetResult.Completed){

            Toast.makeText(getApplicationContext(),"Payment Success", Toast.LENGTH_SHORT).show();

            fnGetBookingID(carPlate);
        }
    }

    private void getEmphericalKey() {


        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            EphericalKey = object.getString("id");

                            Toast.makeText(getApplicationContext(), CustomerID, Toast.LENGTH_SHORT).show();

                            getClientSecret(CustomerID, EphericalKey);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);

                header.put("Stripe-Version", "2022-11-15");

                return header;


            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void getClientSecret(String customerID, String ephericalKey) {


        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            ClientSecret = object.getString("client_secret");

                            Toast.makeText(getApplicationContext(), ClientSecret, Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Connecting....", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);

                return header;


            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerID);
                params.put("amount", Tot + "00");
                params.put("currency", "MYR");
                params.put("automatic_payment_methods[enabled]", "true");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }



    public void sendScanning(String bookingID){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String status = "paid";
                username = User.getInstance().getUsername();

                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[3];
                //field[0] = "plateNumber";
                field[0] = "username";
                field[1] = "status";
                field[2] = "bookingID";
                //Creating array for data
                String[] data = new String[3];
                //data[0] = carPlate;
                data[0] = username;
                data[1] = status;
                data[2] = bookingID;

                PutData putData = new PutData("http://192.168.8.122/loginregister/RetrieveScanning.php", "POST", field, data);
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




    public void sendData(String bookingID){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String status = "Paid";
                username = User.getInstance().getUsername();

                Date currentTime = Calendar.getInstance().getTime();
                String date = currentTime.toString().trim();
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[4];
                field[0] = "date";
                field[1] = "username";
                field[2] = "status";
                field[3] = "bookingID";
                //Creating array for data
                String[] data = new String[4];
                data[0] = date;
                data[1] = username;
                data[2] = status;
                data[3] = bookingID;

                PutData putData = new PutData("http://192.168.8.122/loginregister/updatePayment.php", "POST", field, data);
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

    public void fnGetBookingID(String carplate)
    {
        // for getting the scan number
        //ArrayList<Integer> scanIDIntList = new ArrayList<>();
        ArrayList<String> bookingID = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.8.122/loginregister/getBookingIDOCR.php?carplate=" + carplate;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject scanObj = jsonArray.getJSONObject(i);
                        //JSONObject scanObj = response.getJSONObject(i);
                        String bookID = scanObj.getString("ID");
                        bookingID.add(bookID);

                        // check whether the carplate exist in the scanning database
                        sendScanning(bookingID.get(0));
                        sendData(bookingID.get(0));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void retrieveData(){

        String url = "http://192.168.8.122/loginregister/RetrievePayment.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    Log.e("anyText",response);

                    //converting the string to json array object
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String startDate = obj.getString("Starting_Date");
                        String endDate = obj.getString("End_Date");
                        String startTime = obj.getString("Start_Time");
                        String endtime = obj.getString("End_Time");
                        String parkingStation = obj.getString("station");
                        carPlate = obj.getString("carPlate");
                        String Duration = obj.getString("duration");
                        String floor = obj.getString("floor");
                        String code = obj.getString("code");
                        String sequence = obj.getString("sequence");

                        bookingID = obj.getString("bookingID");


                        textplatenumber.append(carPlate);
                        parkingstation.append(parkingStation);
                        start.append(startDate + " " + startTime);
                        endTime.append(endDate + endtime);
                        //duration.append(Duration);
                        parkingslot.append(floor + code + sequence);

                        priceUpdate();

                        User.getInstance().setBooking(bookingID);

                        fnGetPenaltyTable(carPlate);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                username = User.getInstance().getUsername();

                Map<String, String> params = new HashMap<>();
                params.put("Customer_Username", username);
                params.put("Plate_Number", plateNumber);

                return params;

            }

        };



        requestQueue.add(request);
    }

    public void priceUpdate(){

        String url = "http://192.168.8.122/loginregister/priceUpdate.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    Log.e("anyText",response);

                    //converting the string to json array object
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String ID = obj.getString("ID");
                        Normal_Price_Per_Hour = obj.getString("Normal_Price_Per_Hour");
                        Holiday_Price_Per_Hour = obj.getString("Holiday_Price_Per_Hour");
                        Normal_Price_Per_Day = obj.getString("Normal_Price_Per_Day");
                        Holiday_Price_Per_Day = obj.getString("Holiday_Price_Per_Day");
                        Penalty_Price = obj.getString("Penalty_Price");




                    }

                    dayUpdate();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                username = User.getInstance().getUsername();

                Map<String, String> params = new HashMap<>();
                params.put("Customer_Username", username);
                params.put("Plate_Number", plateNumber);

                return params;

            }

        };



        requestQueue.add(request);

    }

    public void dayUpdate()
    {

        String url = "http://192.168.8.122/loginregister/dayUpdate.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    Log.e("anyText",response);

                    //converting the string to json array object
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String Normal_Hour = obj.getString("Normal_Hour");
                        String Normal_Day = obj.getString("Normal_Day");
                        String Holi_Hour = obj.getString("Holi_Hour");
                        String Holi_Day = obj.getString("Holi_Day");
                        Tot = obj.getInt("Total");


                       /* if (car.equals(""))
                            penalty.append("No");
                        else
                            penalty.append(Penalty_Price);*/

                        normalDay.append("RM " + Normal_Price_Per_Day + " x " + Normal_Day + " days");
                        holiday.append("RM " + Holiday_Price_Per_Day + " x " + Holi_Day + " days");
                        normalHour.append("RM " + Normal_Price_Per_Hour + " x " + Normal_Hour + " days");
                        holidayHour.append("RM " + Holiday_Price_Per_Hour + " x " + Holi_Hour + " days");
                        Total.append(String.valueOf(Tot));


                       // double d=Double.parseDouble("Tot");

                       // jumlah = (Tot);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                username = User.getInstance().getUsername();

                Map<String, String> params = new HashMap<>();
                params.put("Customer_Username", username);
                params.put("Plate_Number", plateNumber);

                return params;

            }

        };



        requestQueue.add(request);



    }

    public void fnGetPenaltyTable(String carplate)
    {
        ArrayList<String> carplateList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.8.122/loginregister/getAllPenaltyCarPlate.php";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("No overtime yet")){
                        carplateList.add("0");
                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject scanObj = jsonArray.getJSONObject(i);
                            //JSONObject scanObj = response.getJSONObject(i);
                            String curCarplate = scanObj.getString("Plate_Number");

                            carplateList.add(curCarplate);
                        }
                    }

                    // if you carplate is in the Arraylist, show the Yes
                    boolean carplateExist = carplateList.contains(carplate);

                    // set the Payment interface status
                    if(carplateExist)
                    {
                        penalty.append("Yes");
                    }
                    else
                    {
                        penalty.append("No");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }




}