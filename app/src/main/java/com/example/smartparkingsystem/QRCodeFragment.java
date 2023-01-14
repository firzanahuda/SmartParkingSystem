package com.example.smartparkingsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
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
import com.vishnusivadas.advanced_httpurlconnection.PutData;

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
    String username, carPlate;

    QRGenerator qrGenerator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_q_r_code, container, false);

        imgQR = v.findViewById(R.id.qrcode);

        String qrCode = UpcomingClass.getInstance().getQrCode();

        imgQR.setImageBitmap(qrGenerator.generateQRCode(qrCode));

        return v;
    }

/*
    public void sendData(){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String status = "Park";
                username = User.getInstance().getUsername();
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[3];
                field[0] = "carPlate";
                field[1] = "username";
                field[2] = "status";
                //Creating array for data
                String[] data = new String[3];
                data[0] = carPlate;
                data[1] = username;
                data[2] = status;

                PutData putData = new PutData("http://192.168.8.122/loginregister/scanPark.php", "POST", field, data);
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

        String url = "http://192.168.8.122/loginregister/QRRetrieve.php";
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

                            carPlate = obj.getString("Plate_Number");
                            String type = obj.getString("Vehicle_Type");

                            // create new QRGenerator object
                            qrGenerator = new QRGenerator(carPlate);

                            // encrypt the carplate
                            String encryptedCarPlate = qrGenerator.thirdScanEncryption();

                            imgQR.setImageBitmap(qrGenerator.generateQRCode(encryptedCarPlate));

                            sendData();

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
    }*/
}