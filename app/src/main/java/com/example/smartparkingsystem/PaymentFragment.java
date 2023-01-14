package com.example.smartparkingsystem;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentFragment extends Fragment {

   String username;
   String totalPay;
   TextView bookingpayment, extendPayment, totalPayment;
   View v;
   Button payment;

    String PublishableKey = "pk_test_51MPSdPDBT7fQ7TQlbaKw4HpmCIfxiRQgIWanGmPWSlGTICOZpH1cmtFATKk5KdEB4Wby2I9Z2L5YBvXTNBW9AROE007JPUNFRK";
    String SecretKey = "sk_test_51MPSdPDBT7fQ7TQlsZgrBBvgoxVKijNU3J9zypgblHSe1Ix5ZvEa8dSFhxODYpvpv1ybRmQnHCyOqf0RrTqGKsXq00eovqtVxh";
    String CustomerID;
    String EphericalKey;
    String ClientSecret;
    PaymentSheet paymentSheet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_payment, container, false);

        bookingpayment = v.findViewById(R.id.bookingpayment);
        extendPayment = v.findViewById(R.id.extendPayment);
        totalPayment = v.findViewById(R.id.totalPayment);
        payment = v.findViewById(R.id.paymentflow);

        retrieveData();

        PaymentConfiguration.init(getContext(), PublishableKey);

        paymentSheet = new PaymentSheet(this,paymentSheetResult -> {

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

                            Toast.makeText(getContext(), CustomerID, Toast.LENGTH_SHORT).show();

                            getEmphericalKey();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+SecretKey);

                return header;


            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        return v;
    }


    private void paymentFlow() {

        paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration("Learn with me", new PaymentSheet.CustomerConfiguration(
                CustomerID,
                EphericalKey
        )));
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {

        if(paymentSheetResult instanceof PaymentSheetResult.Completed){

            Toast.makeText(getContext(),"Payment Success", Toast.LENGTH_SHORT).show();
            sendData();
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

                            Toast.makeText(getContext(), CustomerID, Toast.LENGTH_SHORT).show();

                            getClientSecret(CustomerID, EphericalKey);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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

                            Toast.makeText(getContext(), ClientSecret, Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

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
                params.put("amount", totalPay + "00");
                params.put("currency", "MYR");
                params.put("automatic_payment_methods[enabled]", "true");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }


    public void sendData(){

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
                String[] field = new String[3];
                field[0] = "date";
                field[1] = "username";
                field[2] = "status";
                //Creating array for data
                String[] data = new String[3];
                data[0] = date;
                data[1] = username;
                data[2] = status;

                PutData putData = new PutData("http://192.168.8.122/loginregister/updatePayment.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Data save")){
                            //Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(getContext(),result, Toast.LENGTH_SHORT).show();
                            Log.e("anyText", result);
                        }
                    }
                }
                //End Write and Read data with URL
            }
        });

    }



    public void retrieveData(){

        String url = "http://192.168.8.122/loginregister/RetrievePayment.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    Log.e("anyText",response);

                    //converting the string to json array object
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String bookingPayment = obj.getString("bookingPayment");
                        String extend = obj.getString("extend");
                        String total = obj.getString("total");

                        int num = Integer.parseInt(total.replaceAll("[\\D]", ""));
                        num = num/10;

                        totalPay = String.valueOf(num);
                        System.out.println(totalPay);

                        if(extend.equals(""))
                            extend = "0";

                        bookingpayment.append(bookingPayment);
                        extendPayment.append(extend);
                        totalPayment.append(total);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

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
}