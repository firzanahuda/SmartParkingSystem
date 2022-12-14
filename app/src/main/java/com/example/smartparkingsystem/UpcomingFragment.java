package com.example.smartparkingsystem;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class UpcomingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView edit, startTime, endTime, startDate, endDate, carPlate, duration;
    private User user;
    String username;
    View v;
    List<UpcomingClass> upcomingList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        upcomingList = new ArrayList<>();
        retrieveData();

            v = inflater.inflate(R.layout.fragment_upcoming, container, false);

            recyclerView = v.findViewById(R.id.recylcerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            //v = inflater.inflate(R.layout.fragment_empty_upcoming, container, false);



        //upcomingList = new ArrayList<>();

        /*edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Booking.class);
                startActivity(intent);
            }
        });*/

        //retrieveData();


            return v;

        }

    public void retrieveData() {

        String url = "http://10.131.74.52/loginregister/getUpcomingData.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("anyText", response);
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject upcoming = array.getJSONObject(i);

                        //adding the product to product list
                        upcomingList.add(new UpcomingClass(

                                upcoming.getString("Starting_Date"),
                                upcoming.getString("End_Date"),
                                upcoming.getString("Duration"),
                                upcoming.getString("Start_Time"),
                                upcoming.getString("End_Time"),
                                upcoming.getString("Station"),
                                upcoming.getString("Plate_Number")
                        ));
                    }


                    //creating adapter object and setting it to recyclerview
                    UpcomingAdapter adapter = new UpcomingAdapter(getContext(), upcomingList);
                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

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