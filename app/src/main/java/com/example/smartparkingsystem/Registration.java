package com.example.smartparkingsystem;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Registration extends AppCompatActivity {
    TextInputEditText textInputEditTextUsername, textInputEditTextConfirmPass, textInputEditTextEmail, textInputEditTextPassword;
    Button buttonSignUp;
    TextView textViewLogin;
    ProgressBar progressbar;
    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextConfirmPass = findViewById(R.id.confirmPass);
        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);
        progressbar = findViewById(R.id.progress);
        checkBox = findViewById(R.id.checkbox);

        textInputEditTextUsername.addTextChangedListener(signupTextWatcher);
        textInputEditTextPassword.addTextChangedListener(signupTextWatcher);
        textInputEditTextConfirmPass.addTextChangedListener(signupTextWatcher);
        textInputEditTextEmail.addTextChangedListener(signupTextWatcher);



        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, confirmpass, email, password;
                email = String.valueOf(textInputEditTextEmail.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                confirmpass = String.valueOf(textInputEditTextConfirmPass.getText());
                username = String.valueOf(textInputEditTextUsername.getText());

                if(!username.equals("") && !confirmpass.equals("") && !email.equals("") && !password.equals("")) {
                    progressbar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "email";
                            field[1] = "password";
                            field[2] = "username";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = email;
                            data[1] = password;
                            data[2] = username;

                            PutData putData = new PutData("http://192.168.8.122/loginregister/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressbar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")){
                                        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"This Username exist!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "All Fields Required !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private TextWatcher signupTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @SuppressLint("ResourceType")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String usernameInput = textInputEditTextUsername.getText().toString().trim();
            String passwordInput = textInputEditTextPassword.getText().toString().trim();
            String emailInput = textInputEditTextEmail.getText().toString().trim();
            String confirmPassword = textInputEditTextConfirmPass.getText().toString().trim();


            if(!usernameInput.isEmpty() && !passwordInput.isEmpty() && !emailInput.isEmpty() && !confirmPassword.isEmpty()){

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            buttonSignUp.setEnabled(true);
                            buttonSignUp.setBackgroundResource(R.drawable.squarepurple);

                        } else
                            buttonSignUp.setEnabled(false);
                    }
                });



            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}