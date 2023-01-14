package com.example.smartparkingsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Calendar;
import java.util.Date;

public class PaymentFragment extends Fragment {

   String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sendData();

        return inflater.inflate(R.layout.fragment_payment, container, false);
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
}