package com.example.smartparkingsystem;


import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;


public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.CurrentViewHolder> {

    private Context ctx;
    private List<CurrentClass> currentList;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private Button mButtonSet;
    private Button mButtonStartPause;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;


    // add for extend
    private Button btnExtend;
    private EditText edtExtend;

    // notification
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;
    final AtomicLong i = new AtomicLong();


    public CurrentAdapter(Context ctx, List<CurrentClass> currentList) {
        this.ctx = ctx;
        this.currentList = currentList;
    }

    @NonNull
    @Override
    public CurrentAdapter.CurrentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.activity_current_item, null);
        return new CurrentAdapter.CurrentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentAdapter.CurrentViewHolder holder, int position) {

        CurrentClass currentClass = currentList.get(position);

        holder.station.append(currentClass.getStation() + " Station");
        holder.carplate.append(currentClass.getCarPlate());
        holder.txtFloor.append(currentClass.getFloor());
        holder.txtParking.append(currentClass.getCode() + " 00" + currentClass.getSequence());

        String startDate = currentClass.getStarting_Date();
        String endDate = currentClass.getEnd_Date();
        String startTime = currentClass.getStart_Time();
        String endTime = currentClass.getEnd_Time();
        String carPlate = currentClass.getCarPlate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        String calculateEnd = endDate + " " + endTime;


        //countDown(calculateEnd);

        Date dateEndDate = new Date();

        // change string to date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:aa");
        try {
            dateEndDate = sdf.parse(calculateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // take the end date - current time
        final long[] diff = {dateEndDate.getTime() - System.currentTimeMillis()};


        // start the timer again
        new CountDownTimer(diff[0], 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                diff[0] = millisUntilFinished;
                int days = (int) (diff[0] / 1000) / 3600 / 24;
                int hours = (int) (diff[0] / 1000) / 3600 % 24;
                int minutes = (int) ((diff[0] / 1000) % 3600) / 60;
                int seconds = (int) (diff[0] / 1000) % 60;

                if (days == 0 && hours == 1 && minutes == 0 && seconds == 0) {


                }


                String timeLeftFormatted;

                timeLeftFormatted = String.format(Locale.getDefault(),
                        "%02d:%02d:%02d", days, hours, minutes);

                Log.e("time", timeLeftFormatted);

                holder.txtTimer.setText(timeLeftFormatted);


                // the timer text
                //txtDisplay.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {

                String carPlate = currentClass.getCarPlate();

                // set to Penalty

//                holder.extend.setEnabled(false);
                fnGetBookingIDPenalty(carPlate);

            }
        }.start();




    }

    @Override
    public int getItemCount() {
        return currentList.size();
    }

    class CurrentViewHolder extends RecyclerView.ViewHolder {

        TextView station, txtTimer, carplate, txtFloor, txtParking;
        Button extend;

        public CurrentViewHolder(View itemView) {
            super(itemView);

            station = itemView.findViewById(R.id.txtStation);
            carplate = itemView.findViewById(R.id.carPlate);
            //extend = itemView.findViewById(R.id.extend);
            txtTimer = itemView.findViewById(R.id.txtTimer);
            txtFloor = itemView.findViewById(R.id.txtFloor);
            txtParking = itemView.findViewById(R.id.txtParking);



        }
    }




    public void fnGetBookingIDPenalty(String carplate)
    {
        ArrayList<String> bookingIDList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        String url = "http://192.168.8.122/loginregister/getBookingIDTimer.php?carplate=" + carplate;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject scanObj = jsonArray.getJSONObject(i);
                        //JSONObject scanObj = response.getJSONObject(i);
                        String curBookingID = scanObj.getString("Booking_ID");
                        bookingIDList.add(curBookingID);


                        // used both string to get the floor, code, capacity and status
                        fnGetPenaltyPrice(bookingIDList.get(0), carplate);
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


    // get the Penalty price
    public void fnGetPenaltyPrice(String bookingID, String carplate)
    {
        ArrayList<Double> penaltyPriceList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        String url = "http://192.168.8.122/loginregister/getPenaltyPrice.php";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("No prices yet")){
                        penaltyPriceList.add((double) 0);
                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject scanObj = jsonArray.getJSONObject(i);
                            //JSONObject scanObj = response.getJSONObject(i);
                            Double curPenalty = scanObj.getDouble("Penalty_Price");

                            penaltyPriceList.add(curPenalty);

                        }
                    }

                    // run the next function
                    fnPenaltyTotal(bookingID, carplate, penaltyPriceList.get(0));

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

    // update the total at the Payment table
    public void fnPenaltyTotal(String bookingID, String carplate, Double penaltyPrice)
    {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Starting Write and Read data with URL
                // Creating array for php parameters
                String[] field = new String[2];
                field[0] = "bookingID";
                field[1] = "penaltyPrice";


                // Creating data for php
                String[] data = new String[2];
                data[0] = bookingID;
                data[1] = String.valueOf(penaltyPrice);

                PutData putData = new PutData("http://192.168.8.122/loginregister/paymentPenaltyUpdate.php", "POST", field, data);

                if(putData.startPut()){
                    if(putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Status updated")) {
                            Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
                            // insert a new row into Overtime_Vehicle
                            fnInsertOvertime(carplate);
                        }
                        else {
                            Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void fnInsertOvertime(String carplate)
    {
        ArrayList<Integer> penaltyIDIntList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        String url = "http://192.168.8.122/loginregister/getNewPenaltyID.php";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("No overtime vehicle yet")){
                        penaltyIDIntList.add(0);
                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject scanObj = jsonArray.getJSONObject(i);
                            //JSONObject scanObj = response.getJSONObject(i);
                            String penaltyID = scanObj.getString("ID");
                            // get substr and convert to int
                            penaltyID = penaltyID.substring(1);
                            penaltyID = removeZero(penaltyID);
                            int number = Integer.parseInt(penaltyID);

                            penaltyIDIntList.add(number);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // get the largest number and create the new ID
                int max = Collections.max(penaltyIDIntList) + 1;
                String newPenaltyID;

                if (max >= 1000 && max < 10000)
                {
                    newPenaltyID = "O" + Integer.toString(max);
                }
                else if (max >= 100 && max < 1000)
                {
                    newPenaltyID = "O0" + Integer.toString(max);
                }
                else if (max >= 10 && max < 100)
                {
                    newPenaltyID = "O00" + Integer.toString(max);
                }
                else
                {
                    newPenaltyID = "O000" + Integer.toString(max);
                }

                // insert all into the scanning database
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Starting Write and Read data with URL
                        // Creating array for parameters
                        String[] field = new String[2];
                        field[0] = "penaltyID";
                        field[1] = "carplate";

                        // Creating array for data
                        String[] data = new String[2];
                        data[0] = newPenaltyID;
                        data[1] = carplate;


                        PutData putData = new PutData("http://192.168.8.122/loginregister/insertOvertime.php", "POST", field, data);
                        if(putData.startPut()) {
                            if(putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Insert Scan row success")) {
                                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        //End Write and Read Data with URL
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public static String removeZero(String str)
    {

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

    public class Mycountdowntimer extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Mycountdowntimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {

        }
    }




/*
    // notification
    public void fnCloseNotification()
    {

        NotificationManagerCompat notificationManagerCompat;
        Notification notification;
        // notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mTextViewCountDown.getContext(), "myCh")
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("First Notification")
                .setContentText("Hello World");

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(mTextViewCountDown.getContext());
        // notification till here
        notificationManagerCompat.notify(1, notification);
    }

    // warning notification
    public void fnWarnNotification()
    {

        NotificationManagerCompat notificationManagerCompat;
        Notification notification;
        // notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mTextViewCountDown.getContext(), "myCh")
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Times Up!")
                .setContentText("Your booking time for vehicle is up.");

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(mTextViewCountDown.getContext());
        // notification till here
        notificationManagerCompat.notify(2, notification);
    }*/






}



