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
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BookingDisplay<HttpClient> extends AppCompatActivity {

    TextView textStartInput, textEndInput, textVehiclePNInput, textVehicleTypeInput, textStartTimeInput, textEndTimeInput, textStation, textParkingSlotInput;
    BookingDisplayClass booking;
    //BookingClass booking;
    private User user;
    String username, carPlate;
    ImageView home;

    int normalDay = 0, normalHour = 0, holiDay = 0, holiHour = 0;
    double priceNormalDay = 0, priceNormalHour = 0, priceHoliDay = 0, priceHoliHour = 0, total = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.platecar_list_item);

        textStartInput = findViewById(R.id.textStartInput);
        textEndInput = findViewById(R.id.textEndInput);
        textVehiclePNInput = findViewById(R.id.textVehiclePNInput);
        textVehicleTypeInput = findViewById(R.id.textVehicleTypeInput);
        textStartTimeInput = findViewById(R.id.textStartTimeInput);
        textEndTimeInput = findViewById(R.id.textEndTimeInput);
        textStation = findViewById(R.id.textStation);
        home = findViewById(R.id.home);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        //textParkingSlotInput = findViewById(R.id.textParkingSlotInput);

        String start = BookingDisplayClass.getInstance().getTextInputStart();
        String end = BookingDisplayClass.getInstance().getTextInputEnd();
        String startTime = BookingDisplayClass.getInstance().getTextInputStartTime();
        String endTime = BookingDisplayClass.getInstance().getTextInputEndTime();
        carPlate = BookingDisplayClass.getInstance().getTextInputCarPlate();
        String type = BookingDisplayClass.getInstance().getTextInputVehicle();
        String station = BookingDisplayClass.getInstance().getStation();
        Long duration = BookingDisplayClass.getInstance().getDuration();


        textStartInput.append(start);
        textEndTimeInput.append(endTime);
        textStartTimeInput.append(startTime);
        textEndInput.append(end);
        textVehiclePNInput.append(carPlate);
        textVehicleTypeInput.append(type);
        textStation.append(station);

        fnGetWeekPrices(start, startTime, end, endTime);
        //RetrieveParkingSlot();



    }


    public void RetrieveParkingSlot(){


        String url = "http://192.168.8.122/loginregister/RetrieveParkingSlot.php";
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

                        String floor = obj.getString("floor");
                        String code = obj.getString("code");
                        String sequence = obj.getString("sequence");


                        textParkingSlotInput.append(floor + code + sequence);



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
                params.put("Plate_Number", carPlate);

                return params;

            }

        };



        requestQueue.add(request);


    }

    // this event will enable the back
    // function to the button on press


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        return true;
    }

    // take the prices from the price_update table
    public void fnGetWeekPrices(String startDate, String startTime, String endDate, String endTime)
    {
        ArrayList<Double> normalHourPriceList = new ArrayList<>();
        ArrayList<Double> normalDayPriceList = new ArrayList<>();
        ArrayList<Double> holiHourPriceList = new ArrayList<>();
        ArrayList<Double> holiDayPriceList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.8.122/loginregister/getAllPrices.php";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("No prices yet")){
                        normalHourPriceList.add((double) 0);
                        normalDayPriceList.add((double) 0);
                        holiHourPriceList.add((double) 0);
                        holiDayPriceList.add((double) 0);
                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject scanObj = jsonArray.getJSONObject(i);
                            //JSONObject scanObj = response.getJSONObject(i);
                            Double curNormalHour = scanObj.getDouble("Normal_Price_Per_Hour");
                            Double curNormalDay = scanObj.getDouble("Normal_Price_Per_Day");
                            Double curHoliHour = scanObj.getDouble("Holiday_Price_Per_Hour");
                            Double curHoliDay = scanObj.getDouble("Holiday_Price_Per_Day");

                            normalHourPriceList.add(curNormalHour);
                            normalDayPriceList.add(curNormalDay);
                            holiHourPriceList.add(curHoliHour);
                            holiDayPriceList.add(curHoliDay);
                        }
                    }

                    // run the next function
                    fnPaymentTotal(normalHourPriceList.get(0), normalDayPriceList.get(0),
                            holiHourPriceList.get(0), holiDayPriceList.get(0), startDate, startTime, endDate, endTime);

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

    // the startDate format = 16/1/2023, time = 01:00pm
    // take the time,
    public void fnPaymentTotal(Double norHourPrice, Double norDayPrice, Double holiHourPrice, Double holiDayPrice, String startDate, String startTime, String endDate, String endTime)
    {



        // combine the startDate and startTime together become datetime format
        // date become datetime format (start)
        String[] sDate = startDate.split("/");
        int startDayInt = Integer.parseInt(sDate[0]);
        int startMonthInt = Integer.parseInt(sDate[1]);
        int startYearInt = Integer.parseInt(sDate[2]);

        String startDayStr, startMonthStr, startYearStr;
        if (startDayInt < 10)
        {
            startDayStr = "0" + Integer.toString(startDayInt);
        }
        else
            startDayStr = Integer.toString(startDayInt);

        if (startMonthInt < 10)
        {
            startMonthStr = "0" + Integer.toString(startMonthInt);
        }
        else
            startMonthStr = Integer.toString(startMonthInt);

        startYearStr = Integer.toString(startYearInt);

        startDate = startDayStr + "-" + startMonthStr + "-" + startYearStr;


        // time become datetime format (start)
        if (startTime.contains("am"))
        {
            startTime = startTime.substring(0, 5) + ":00";
        }
        else if (startTime.contains("pm"))
        {
            String hour = startTime.substring(0, 2);
            int hourInt = Integer.parseInt(hour);

            if (hourInt != 12)
            {
                hourInt += 12;
            }


            startTime = Integer.toString(hourInt) + startTime.substring(2,5)  + ":00";

        }

        // for end date
        String[] eDate = endDate.split("/");
        int endDayInt = Integer.parseInt(eDate[0]);
        int endMonthInt = Integer.parseInt(eDate[1]);
        int endYearInt = Integer.parseInt(eDate[2]);

        String endDayStr, endMonthStr, endYearStr;
        if (endDayInt < 10)
        {
            endDayStr = "0" + Integer.toString(endDayInt);
        }
        else
            endDayStr = Integer.toString(endDayInt);

        if (endMonthInt < 10)
        {
            endMonthStr = "0" + Integer.toString(endMonthInt);
        }
        else
            endMonthStr = Integer.toString(endMonthInt);

        endYearStr = Integer.toString(endYearInt);

        endDate = endDayStr + "-" + endMonthStr + "-" + endYearStr;


        // time become datetime format (start)
        if (endTime.contains("am"))
        {
            endTime = endTime.substring(0, 5) + ":00";
        }
        else if (endTime.contains("pm"))
        {
            String hour = endTime.substring(0, 2);
            int hourInt = Integer.parseInt(hour);

            if (hourInt != 12)
            {
                hourInt += 12;
            }

            endTime = Integer.toString(hourInt) + startTime.substring(2,5)  + ":00";

        }

        String startDateTime = startDate + " " + startTime;
        String endDateTime = endDate + " " + endTime;

        // end date
        Date dateEndDate = new Date();

        // start date
        Date dateStartDate = new Date();

        Calendar c = Calendar.getInstance();

        // change string to date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            dateEndDate = sdf.parse(endDateTime);
            dateStartDate = sdf.parse(startDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // create the long endTime in millis
        long end = dateEndDate.getTime();
        long start = dateStartDate.getTime();

        c.setTimeInMillis(end);
        int endHour = c.get(Calendar.HOUR_OF_DAY);

        c.setTimeInMillis(start);
        int startHour = c.get(Calendar.HOUR_OF_DAY);

        // calculate the difference in days between 2 dates
        long diff = dateEndDate.getTime() - dateStartDate.getTime();
        long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        // get the startDate Day
        c.setTime(dateStartDate);
        int dow = c.get(Calendar.DAY_OF_WEEK); // Sunday is 1

        // for start hour
        if (dow == 1 || dow == 7)
        {
            int curHour = 24 - startHour;
            holiHour += curHour;
        }
        else
        {
            int curHour = 24 - startHour;
            normalHour += curHour;
        }


        // for end hour
        c.setTime(dateEndDate);
        int endDow = c.get(Calendar.DAY_OF_WEEK);

        if (endDow == 1 || endDow == 7)
        {
            holiHour += endHour;
        }
        else
            normalHour += endHour;

        // take the number of daysDiff / 7 to get the number weeks [holiday = num * 2, normal day = nu, * 5]
        // daysDiff % 7 to get the number of days left
        // start Date Day + daysDiff%7, use for loop, add 1 at a time, if it is not 1 or 7, then is weekDay
        long numOfWeek = daysDiff / 7;
        long leftDays = daysDiff % 7;

        holiDay = (int) (numOfWeek * 2);
        normalDay = (int) (numOfWeek * 5);

        for (int i = 0; i < leftDays; i++)
        {
            dow++;

            if (dow > 7)
            {
                dow -= 7;
            }

            if (dow != 1 || dow != 7)
            {
                normalDay++;
            }
            else
                holiDay++;
        }

        // calculate the price
        //(String bookingID, String newEndDate, String startDate, Double total, Double bookingPayment, Double extendPrice, Double norHourPrice, Double norDayPrice, Double holiHourPrice, Double holiDayPrice)
        // double priceNormalDay = 0, priceNormalHour = 0, priceHoliDay = 0, priceHoliHour = 0;
        priceNormalDay = norDayPrice * normalDay;
        priceNormalHour = norHourPrice * normalHour;
        priceHoliDay = holiDayPrice * holiDay;
        priceHoliHour = holiHourPrice * holiHour;

        total = priceNormalDay + priceNormalHour + priceHoliDay + priceHoliHour;

        // update the Payment table with total, normalDay + normalHour, holiDay, holiHour


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
                String[] field = new String[9];
                field[0] = "total";
                field[1] = "username";
                field[2] = "status";
                field[3] = "carPlate";
                field[4] = "bookingID";
                field[5] = "Normal_Hour";
                field[6] = "Normal_Day";
                field[7] = "Holi_Hour";
                field[8] = "Holi_Day";
                //Creating array for data
                String[] data = new String[9];
                data[0] = String.valueOf(total);
                data[1] = username;
                data[2] = status;
                data[3] = carPlate;
                data[4] = bookingID;
                data[5] = String.valueOf(normalHour);
                data[6] = String.valueOf(normalDay);
                data[7] = String.valueOf(holiHour);
                data[8] = String.valueOf(holiDay);

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
