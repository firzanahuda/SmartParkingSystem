package com.example.smartparkingsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CurrentFragment extends Fragment {

    String username;
    View v;
    List<CurrentClass> currentList;
    //filtered list
    List<CurrentClass> filteredList= new ArrayList<CurrentClass>();

    //filters
    List<String> filters = new ArrayList<String>();
    RecyclerView recyclerView;

    ImageView imageView;
    TextView textView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentList = new ArrayList<>();
        //status();

        v = inflater.inflate(R.layout.fragment_current, container, false);

        imageView = v.findViewById(R.id.retrieve);
        textView = v.findViewById(R.id.textretrieve);
        recyclerView = v.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        retrieveData();

        return v;
    }


    /*public void status() {

        String url = "http://192.168.8.122/loginregister/status.php";
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
                        JSONObject retrieve = array.getJSONObject(i);

                        String status = retrieve.getString("status");
                        String plateNumber = retrieve.getString("plateNumber");

                        if(status.equals("parked")){
                            retrieveData();
                        }

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

    }*/




    public void retrieveData() {

        String url = "http://192.168.8.122/loginregister/getCurrent.php";
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
                        JSONObject current = array.getJSONObject(i);

                        //adding the product to product list
                        currentList.add(new CurrentClass(
                                current.getString("Starting_Date"),
                                current.getString("End_Date"),
                                current.getString("Start_Time"),
                                current.getString("End_Time"),
                                current.getString("station"),
                                current.getString("carPlate"),
                                current.getString("floor"),
                                current.getString("code"),
                                current.getString("sequence"),
                                current.getString("status")

                        ));
                    }

                    filters.add("parked");

                    //now filter the original list

                    for(int i = 0 ; i<currentList.size() ; i++){

                        CurrentClass item = currentList.get(i);

                        if(filters.contains(item.getStatus())){

                            filteredList.add(item);

                        }
                    }

                    //creating adapter object and setting it to recyclerview
                    CurrentAdapter adapter = new CurrentAdapter(getContext(), filteredList);
                    recyclerView.setAdapter(adapter);

                    if(filteredList.size() == 0){

                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);

                    }else
                    {
                        imageView.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
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