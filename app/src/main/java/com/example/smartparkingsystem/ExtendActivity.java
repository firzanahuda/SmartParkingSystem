package com.example.smartparkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ExtendActivity extends AppCompatActivity {

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend);

        text = findViewById(R.id.text);

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            String startDate = bundle.getString("startDate");
            String endDate = bundle.getString("endDate");
            String startTime = bundle.getString("startTime");
            String endTime = bundle.getString("endTime");

            text.append(startDate + " " + endDate + " " + startTime + " " + endTime);


        }


    }
}