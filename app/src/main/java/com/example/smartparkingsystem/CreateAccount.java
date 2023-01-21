package com.example.smartparkingsystem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.example.smartparkingsystem.databinding.ActivityCreateAccountBinding;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Vector;

public class CreateAccount extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;
    private User user;
    private Vector<User> users;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // calling the action bar
        //ActionBar actionBar = getSupportActionBar();

        // Customize the back button
        //actionBar.setHomeAsUpIndicator(R.drawable.mybutton);

        // showing the back button in action bar
        //actionBar.setDisplayHomeAsUpEnabled(true);



        binding.fabAdd.setOnClickListener(this:: fnAdd );
        users = new Vector<>();
    }

    private void fnAdd(View view) {

        String firstName = binding.edtFirstName.getText().toString();
        String lastName = binding.edtLastName.getText().toString();
        String noPhone = binding.edtNoPhone.getText().toString();
        String noIC = binding.edtNoIC.getText().toString();
        String carNumber = binding.spnState.getSelectedItem().toString();
        String carPlate1 = binding.carPlateNum.getText().toString();
        String carPlate2 = binding.carPlateNum2.getText().toString();
        String carPlate3 = binding.carPlateNum3.getText().toString();
        String carPlate4 = binding.carPlateNum4.getText().toString();
        String carPlate5 = binding.carPlateNum5.getText().toString();

        user = new User(firstName, lastName, noPhone, noIC, carNumber, carPlate1, carPlate2,
                carPlate3, carPlate4, carPlate5);

        users.add(user);

        username = User.getInstance().getUsername();


        if (!firstName.equals("") && !lastName.equals("") && !noPhone.equals("") && !noIC.equals("")
                && !carNumber.equals("") && !carPlate1.equals("") ) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[5];
                    field[0] = "firstName";
                    field[1] = "lastName";
                    field[2] = "noPhone";
                    field[3] = "noIC";
                    field[4] = "username";
                    //Creating array for data
                    String[] data = new String[5];
                    data[0] = firstName;
                    data[1] = lastName;
                    data[2] = noPhone;
                    data[3] = noIC;
                    data[4] = username;

                    String[] fieldVehicle = new String[7];
                    fieldVehicle[0] = "carPlate1";
                    fieldVehicle[1] = "carPlate2";
                    fieldVehicle[2] = "carPlate3";
                    fieldVehicle[3] = "carPlate4";
                    fieldVehicle[4] = "carPlate5";
                    fieldVehicle[5] = "carNumber";
                    fieldVehicle[6] = "username";

                    String[] dataVehicle = new String[7];
                    dataVehicle[0] = carPlate1;
                    dataVehicle[1] = carPlate2;
                    dataVehicle[2] = carPlate3;
                    dataVehicle[3] = carPlate4;
                    dataVehicle[4] = carPlate5;
                    dataVehicle[5] = carNumber;
                    dataVehicle[6] = username;


                    PutData putData = new PutData("http://192.168.8.122/loginregister/profile.php", "POST", field, data);
                    PutData putDataVehicle = new PutData("http://192.168.8.122/loginregister/carPlateRegister.php", "POST", fieldVehicle, dataVehicle);
                    if (putData.startPut() && putDataVehicle.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (result.equals("Profile Success")) {
                                String resultVehicle = putDataVehicle.getResult();
                                if (resultVehicle.equals("Successfully Completing Your Profile")) {
                                    Toast.makeText(getApplicationContext(), resultVehicle, Toast.LENGTH_SHORT).show();
                                        /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();*/
                                } else {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }
                    //End Write and Read data with URL
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "All Fields Required !", Toast.LENGTH_SHORT).show();
        }
    }
}