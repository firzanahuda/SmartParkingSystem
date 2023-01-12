package com.example.smartparkingsystem;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class QRGenerator {

    private String carplate;

    // create the constructor
    // encrypt the carplate (if is to park car, use thirdScan,
    // if is retrieve car, use fourthScan)
    // then only pass the carplate into the parameter
    public QRGenerator(String carplate){
        this.carplate = carplate;
    }

    public void setCarplate(String carplate) {this.carplate = carplate;}

    public String getCarplate(){return carplate;}

    // produce the QRCode with the carplate
    public Bitmap generateQRCode(String txtCarplate)
    {
        Bitmap qrCode = null;
        // convert text to QRCode
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            // initialize bit matrix
            BitMatrix matrix = writer.encode(txtCarplate, BarcodeFormat.QR_CODE, 350, 350);
            // initialize barcode encoder
            BarcodeEncoder encoder = new BarcodeEncoder();
            // initialize bitmap
            qrCode = encoder.createBitmap(matrix);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return qrCode;
    }

    // for upcoming QRCode parameter String
    public String thirdScanEncryption()
    {
        // encrypt the carplate, to keep under QRCode (AES)
        // declare secret key
        String secretKey = "dontSay";

        // take the car plate to encrypt
        String encryptedPlate = AES.encrypt(carplate, secretKey);

        return encryptedPlate;
    }

    // for retrieve car QRCode
    public String fourthScanEncryption()
    {
        // encrypt the carplate, to keep under QRCode (AES)
        // declare secret key
        String secretKey = "dontSay";

        // take the car plate to encrypt
        String encryptedPlate = AES.encrypt(carplate, secretKey);

        // change the QRCode (encrypt again)
        String encryptStr = "now";
        String nextScanQR = encryptedPlate + encryptStr;

        return nextScanQR;
    }

}