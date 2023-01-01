package com.example.smartparkingsystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    public UpcomingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingFragment newInstance(String param1, String param2) {
        UpcomingFragment fragment = new UpcomingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_upcoming, container, false);

        edit = v.findViewById(R.id.edit);
        startTime = v.findViewById(R.id.startTime);
        startDate = v.findViewById(R.id.startDate);
        endTime = v.findViewById(R.id.endTime);
        endDate = v.findViewById(R.id.endDate);
        carPlate = v.findViewById(R.id.carPlate);
        duration = v.findViewById(R.id.duration);


        retrieveData();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Booking.class);
                startActivity(intent);
            }
        });



        return v;
    }

    public void retrieveData(){

        String url = "http://192.168.8.122/loginregister/getUpcomingData.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("upcoming");

                    if(success.equals("1")){
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject obj = jsonArray.getJSONObject(i);

                            String Plate_Number= obj.getString("Plate_Number");
                            String Starting_Date = obj.getString("Starting_Date");
                            String End_Date = obj.getString("End_Date");
                            String Duration = obj.getString("Duration");
                            String Start_Time = obj.getString("Start_Time");
                            String End_Time = obj.getString("End_Time");

                            carPlate.append(Plate_Number);
                            startDate.append(Starting_Date);
                            endDate.append(End_Date);
                            duration.append(Duration);
                            startTime.append(Start_Time);
                            endTime.append(End_Time);

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
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                username = User.getInstance().getUsername();

                Map<String, String> params = new HashMap< >();
                params.put("Customer_Username", username);

                return params;

            }

        };



        requestQueue.add(request);
    }

}