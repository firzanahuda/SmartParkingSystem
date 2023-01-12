package com.example.smartparkingsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

public class QRCodeFragment extends Fragment {

    private static SecretKeySpec secretKey;
    private static byte[] key;
    List<UpcomingClass> upcomingList;
    ImageView imgQR;

    QRGenerator qrGenerator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_q_r_code, container, false);

        imgQR = v.findViewById(R.id.qrcode);

        retrieveData();
        return v;
    }

    public void retrieveData(){

        String url = "http://10.131.74.52/loginregister/QRRetrieve.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("carPlate");

                    if(success.equals("1")){
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject obj = jsonArray.getJSONObject(i);

                            String carPlate = obj.getString("Plate_Number");
                            String type = obj.getString("Vehicle_Type");

                            // create new QRGenerator object
                            qrGenerator = new QRGenerator(carPlate);

                            // encrypt the carplate
                            String encryptedCarPlate = qrGenerator.thirdScanEncryption();

                            imgQR.setImageBitmap(qrGenerator.generateQRCode(encryptedCarPlate));

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


                String username = User.getInstance().getUsername();

                Map<String, String> params = new HashMap< >();
                params.put("Customer_Username", username);

                return params;

            }

        };

        requestQueue.add(request);
    }
}