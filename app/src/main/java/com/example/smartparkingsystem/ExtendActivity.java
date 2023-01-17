package com.example.smartparkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ExtendActivity extends AppCompatActivity {

    TextView text;
    EditText extendhours;
    String carPlate, start, end, startDate, startTime, endDate, endTime, sDate, eDate;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend);

        extendhours = findViewById(R.id.extendhours);
        button = findViewById(R.id.send);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {


            }
        });

    }

    private void parseData(long hours) throws ParseException{

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            startDate = bundle.getString("startDate");
            endDate = bundle.getString("endDate");
            startTime = bundle.getString("startTime");
            endTime = bundle.getString("endTime");
            carPlate = bundle.getString("carPlate");

            Log.e("Error", startDate);
            Log.e("Error", endDate);
            Log.e("Error", startTime);
            Log.e("Error", endTime);
            Log.e("Error", carPlate);

        }


        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = String.valueOf(formatter.parse(startDate));
        String enDate = String.valueOf(formatter.parse(endDate));


        Format f = new SimpleDateFormat("hh:mm");
        String strResult = f.format(startTime);
        String endResult = f.format(endTime);

        start = strDate + " " + strResult;
        end = enDate + " " + endResult;

        extendTime(hours, end, start, carPlate);

    }





    // when the confirmed hour button is pressed, passed Date format endDate, startDate, carplate, and
    // entered hours
    // past the same format of endDate from currentActivity to the ExtendedActivity
    // extend function
    private void extendTime(long hours, String endDate, String startDate, String carplate)
    {
        // the end date
        Date passEndDate = new Date();

        // change string to date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            passEndDate = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // turn it to millisecond, then add together
        long millisInput = hours * 60000 * 60;
        long extendedEndTime = passEndDate.getTime() + millisInput;

        ///// but for here I change it to date format again
        String newEndDate = sdf.format(extendedEndTime);

        // change the endDate and Time in database
        fnGetBookingIDTimer(carplate, newEndDate, startDate);

    }

    // Method 1: get the bookingID with carplate, check with Scanning table if Status == "parked"
    public void fnGetBookingIDTimer(String carplate, String newEndDate, String startDate)
    {
        ArrayList<String> bookingIDList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.4/smartparkingsystem/getBookingIDTimer.php?carplate=" + carplate;
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
                        fnUpdateBookingEndDate(bookingIDList.get(0), newEndDate, startDate);
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

    // Method 2: use the bookingID to update the EndDate in the booking Table
    public void fnUpdateBookingEndDate(String bookingID, String newEndDate, String startDate)
    {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Starting Write and Read data with URL
                // Creating array for php parameters
                String[] field = new String[2];
                field[0] = "bookingID";
                field[1] = "newEndDate";

                // Creating data for php
                String[] data = new String[2];
                data[0] = bookingID;
                data[1] = newEndDate;

                // set the status == "done"
                PutData putData = new PutData("http://192.168.0.4/smartparkingsystem/extendEndDateUpdate.php", "POST", field, data);

                if(putData.startPut()){
                    if(putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("End date updated")) {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            getPaymentPrices(bookingID, newEndDate, startDate);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }


    // use the Booking_ID to get the Total, Booking_Payment, Extend
    // get the extended price at the Payment table, update the price of it (extended price + new price)
    // how do I differentiate for weekday and weekend???
    // get the price for weekday and weekend
    public void getPaymentPrices(String bookingID, String newEndDate, String startDate)
    {
        ArrayList<Double> totalList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.4/smartparkingsystem/getPaymentDetails.php?bookingID=" + bookingID;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject scanObj = jsonArray.getJSONObject(i);
                        //JSONObject scanObj = response.getJSONObject(i);
                        Double curTotal = scanObj.getDouble("Total");
                        Double curBookingPayment = scanObj.getDouble("BookingPayment");
                        Double curExtendPrice = scanObj.getDouble("Extend");
                        totalList.add(curTotal);
                    }
                    // past all parameter
                    fnGetWeekPrices(bookingID, newEndDate, startDate, totalList.get(0));
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

    // get the all 4 types of prices
    // use startdate and endDate calculate weekday and weekend numbers, also the weekday hour and weekend hours
    // update the Payment Total, Extend, weekday number, weekend number, weekday hour, weekend hour
    public void fnGetWeekPrices(String bookingID, String newEndDate, String startDate, Double total)
    {
        ArrayList<Double> normalHourPriceList = new ArrayList<>();
        ArrayList<Double> normalDayPriceList = new ArrayList<>();
        ArrayList<Double> holiHourPriceList = new ArrayList<>();
        ArrayList<Double> holiDayPriceList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.4/smartparkingsystem/getAllPrices.php";
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
                    fnCalculatePrices(bookingID, newEndDate, startDate, total, normalHourPriceList.get(0), normalDayPriceList.get(0),
                            holiHourPriceList.get(0), holiDayPriceList.get(0));

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

    // calculate the weekday, weekend, hours number
    // calculate the prices in total
    // use bookingID, update the Payment Total, Extend, weekday number, weekend number, weekday hour, weekend hour
    public void fnCalculatePrices(String bookingID, String newEndDate, String startDate, Double total, Double norHourPrice, Double norDayPrice, Double holiHourPrice, Double holiDayPrice)
    {
        int normalDay = 0, normalHour = 0, holiDay = 0, holiHour = 0;
        double priceNormalDay = 0, priceNormalHour = 0, priceHoliDay = 0, priceHoliHour = 0;

        // end date
        Date dateEndDate = new Date();

        // start date
        Date dateStartDate = new Date();

        Calendar c = Calendar.getInstance();

        // change string to date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            dateEndDate = sdf.parse(newEndDate);
            dateStartDate = sdf.parse(startDate);
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

        // update database with Booking_ID
        fnUpdatePayment(bookingID, total, normalDay, normalHour, holiDay, holiHour);

    }

    // update the database
    public void fnUpdatePayment(String bookingID, Double total, int normalDay, int normalHour, int holiDay, int holiHour)
    {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Starting Write and Read data with URL
                // Creating array for php parameters
                String[] field = new String[6];
                field[0] = "bookingID";
                field[1] = "total";
                field[2] = "normalDay";
                field[3] = "normalHour";
                field[4] = "holiDay";
                field[5] = "holiHour";

                // Creating data for php
                String[] data = new String[6];
                data[0] = bookingID;
                data[1] = String.valueOf(total);
                data[2] = String.valueOf(normalDay);
                data[3] = String.valueOf(normalHour);
                data[4] = String.valueOf(holiDay);
                data[5] = String.valueOf(holiHour);

                PutData putData = new PutData("http://10.131.76.38/smartparkingsystem/paymentPricesUpdate.php", "POST", field, data);

                if(putData.startPut()){
                    if(putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Status updated")) {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}