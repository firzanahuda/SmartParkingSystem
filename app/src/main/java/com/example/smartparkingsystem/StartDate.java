package com.example.smartparkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

public class StartDate extends AppCompatActivity {

    com.example.smartparkingsystem.databinding.ActivityEndDateBinding binding;
    CalendarView calendarView;
    TextView myDate;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = com.example.smartparkingsystem.databinding.ActivityEndDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        calendarView = (CalendarView) findViewById(R.id.calendar);
        myDate = (TextView) findViewById(R.id.dateend);

        calendarView.setMinDate(System.currentTimeMillis() - 1000);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month+1) + "/" + year;
                myDate.setText(date);
            }
        });

        save = (Button) findViewById(R.id.Save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result  = myDate.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", result);

                setResult(RESULT_OK,resultIntent);
                finish();

            }
        });

    }

}