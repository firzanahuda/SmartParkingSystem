package com.example.smartparkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button buttonSignup;
    Button buttonLogin;
    TextView textViewHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSignup = findViewById(R.id.btnSignUp);
        textViewHome = findViewById(R.id.textViewHome);
        buttonLogin = findViewById(R.id.btnLogin);

    }

    public void goToLoginActivity (View view){
        Intent intent = new Intent (this, Login.class);
        startActivity(intent);
    }

    public void goToSignupActivity (View view){
        Intent intent = new Intent (this, Registration.class);
        startActivity(intent);
    }
}