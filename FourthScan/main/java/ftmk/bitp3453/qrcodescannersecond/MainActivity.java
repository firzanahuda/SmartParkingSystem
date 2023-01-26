package ftmk.bitp3453.qrcodescannersecond;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.zxing.Result;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    // declare variable
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView ScannerView;
    private static int cam = Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        int currentapiVersion = Build.VERSION.SDK_INT;
        if(currentapiVersion >= Build.VERSION_CODES.M){
            // if the permission granted, show the text
            if (checkPermission()){
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
            }
            else{
                // else, ask the permission again from user
                requestPermission();
            }
        }
    }

    // check the permission
    // if not granted, will ask from user
    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    // request the permission
    private void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }


    @Override
    public void onResume(){
        super.onResume();

        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M){
            if (checkPermission()){
                if (ScannerView == null){
                    ScannerView = new ZXingScannerView(this);
                    setContentView(ScannerView);
                }
                ScannerView.setResultHandler(this);
                ScannerView.startCamera();
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ScannerView.stopCamera();
        ScannerView = null;
    }

    @Override
    public void handleResult(Result result) {
        final String rawResult = result.getText();

        // Toast.makeText(getApplicationContext(), rawResult, Toast.LENGTH_LONG).show();


        // scan or using nfc and compare to the encrypted qrcode (the user qrcode is at the upcoming page)


        // pay if not surpass 10 minutes from the paytime, can retrieve, else set status to overtime and the datetime to the final pass 10 minutes time
        // if same, and the status is paid, set the status to "retrieve", produce correct sound
        fnCheckQRCode(rawResult);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // a method that want to run after the wait
                ScannerView.resumeCameraPreview(MainActivity.this);
            }
        }, 2000);

    }


    // pass sound
    private void fnPassSound()
    {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        // pass sound
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
    }

    // fail sound
    private void fnFailSound()
    {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        // fail sound
        toneGen1.startTone(ToneGenerator.TONE_SUP_ERROR,150);
    }

    // check with the Scanning encrypted QRCode
    public void fnCheckQRCode(String qrCode)
    {
        String scanQR = qrCode;

        // check with database to see if qrcode exist
        if (!scanQR.equals("")){
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // Starting Write and Read data with URL
                    // Creating array for php parameters
                    String[] field = new String[1];
                    field[0] = "qrcode";

                    // Creating data for php
                    String[] data = new String[1];
                    data[0] = scanQR;
                    // connect to database (check the encrypted "now" qrcode, if status == "paid"))
                    // (if passed, stop scanning for 5s, else stop scanning for 2 second)
                    PutData putData = new PutData("http://10.131.76.99/loginregister/qrCodeOCRFourth.php", "POST", field, data);


                    if(putData.startPut()){
                        if(putData.onComplete()) {
                            String result = putData.getResult();
                            System.out.println("Result: " + putData.getResult());
                            if(result.equals("QRCode exist")) {
                                // produce beep sound
                                fnPassSound();

                                // set the status to "retrieve"
                                fnUpdateStatus(scanQR);

                                // System.out.println("Yes");
                            }
                            else {
                                // produce warning sound
                                fnFailSound();
                                // stop the scanning for 2s
                                // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                // System.out.println("Oh no");
                            }
                        }
                    }
                }
            });
        }

    }


    // set status to retrieve, if the status is paid
    private void fnUpdateStatus(String qrcode)
    {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Starting Write and Read data with URL
                // Creating array for php parameters
                String[] field = new String[1];
                field[0] = "qrcode";

                // Creating data for php
                String[] data = new String[1];
                data[0] = qrcode;

                // connect to database
                // (if passed, stop scanning for 5s, else stop scanning for 2 second)
                PutData putData = new PutData("http://10.131.76.99/loginregister/fourthScanUpdate.php", "POST", field, data);

                if(putData.startPut()){
                    if(putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Status updated")) {
                            // produce line
                            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                        }
                        else {
                            // produce error line
                            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }


}