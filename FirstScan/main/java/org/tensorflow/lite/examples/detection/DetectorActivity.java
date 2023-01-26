/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.detection;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.util.TypedValue;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.detection.customview.OverlayView;
import org.tensorflow.lite.examples.detection.customview.OverlayView.DrawCallback;
import org.tensorflow.lite.examples.detection.env.BorderedText;
import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.env.Logger;
import org.tensorflow.lite.examples.detection.tflite.Classifier;
import org.tensorflow.lite.examples.detection.tflite.DetectorFactory;
import org.tensorflow.lite.examples.detection.tflite.YoloV5Classifier;
import org.tensorflow.lite.examples.detection.tracking.MultiBoxTracker;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();

    private static final DetectorMode MODE = DetectorMode.TF_OD_API;
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.85f;
    private static final boolean MAINTAIN_ASPECT = true;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 640);
    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private Integer sensorOrientation;

    private YoloV5Classifier detector;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private boolean computingDetection = false;

    private long timestamp = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private MultiBoxTracker tracker;

    private BorderedText borderedText;

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        tracker = new MultiBoxTracker(this);

        final int modelIndex = modelView.getCheckedItemPosition();
        final String modelString = modelStrings.get(modelIndex);

        try {
            detector = DetectorFactory.getDetector(getAssets(), modelString);
        } catch (final IOException e) {
            e.printStackTrace();
            LOGGER.e(e, "Exception initializing classifier!");
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

        int cropSize = detector.getInputSize();

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        cropSize, cropSize,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                new DrawCallback() {
                    @Override
                    public void drawCallback(final Canvas canvas) {
                        tracker.draw(canvas);
                        if (isDebug()) {
                            tracker.drawDebug(canvas);
                        }
                    }
                });

        tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
    }

    protected void updateActiveModel() {
        // Get UI information before delegating to background
        final int modelIndex = modelView.getCheckedItemPosition();
        final int deviceIndex = deviceView.getCheckedItemPosition();
        String threads = threadsTextView.getText().toString().trim();
        final int numThreads = Integer.parseInt(threads);

        handler.post(() -> {
            if (modelIndex == currentModel && deviceIndex == currentDevice
                    && numThreads == currentNumThreads) {
                return;
            }
            currentModel = modelIndex;
            currentDevice = deviceIndex;
            currentNumThreads = numThreads;

            // Disable classifier while updating
            if (detector != null) {
                detector.close();
                detector = null;
            }

            // Lookup names of parameters.
            String modelString = modelStrings.get(modelIndex);
            String device = deviceStrings.get(deviceIndex);

            LOGGER.i("Changing model to " + modelString + " device " + device);

            // Try to load model.

            try {
                detector = DetectorFactory.getDetector(getAssets(), modelString);
                // Customize the interpreter to the type of device we want to use.
                if (detector == null) {
                    return;
                }
            }
            catch(IOException e) {
                e.printStackTrace();
                LOGGER.e(e, "Exception in updateActiveModel()");
                Toast toast =
                        Toast.makeText(
                                getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }


            if (device.equals("CPU")) {
                detector.useCPU();
            } else if (device.equals("GPU")) {
                detector.useGpu();
            } else if (device.equals("NNAPI")) {
                detector.useNNAPI();
            }
            detector.setNumThreads(numThreads);

            int cropSize = detector.getInputSize();


            croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

            frameToCropTransform =
                    ImageUtils.getTransformationMatrix(
                            previewWidth, previewHeight,
                            cropSize, cropSize,
                            sensorOrientation, MAINTAIN_ASPECT);

            cropToFrameTransform = new Matrix();
            frameToCropTransform.invert(cropToFrameTransform);
        });
    }

    @Override
    protected void processImage() {
        ++timestamp;
        final long currTimestamp = timestamp;
        trackingOverlay.postInvalidate();

        // No mutex needed as this method is not reentrant.
        if (computingDetection) {
            readyForNextImage();
            return;
        }
        computingDetection = true;
        LOGGER.i("Preparing image " + currTimestamp + " for detection in bg thread.");

        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);


        //readyForNextImage();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readyForNextImage();
            }
        }, 1500);

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap);
        }

        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        LOGGER.i("Running detection on image " + currTimestamp);
                        final long startTime = SystemClock.uptimeMillis();
                        final List<Classifier.Recognition> results = detector.recognizeImage(croppedBitmap);
                        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

                        // Log.e("CHECK", "run: " + results.size());

                        if (results.size() == 1) {
                            String detectedText = detect(croppedBitmap);
                            // remove the spaces between the detectedText
                            detectedText = detectedText.replace(" ", "");

                            // check whether the carplate is in the detected string
                            // then carry out fnSubmit inside
                            fnReturnCarplate(detectedText);

                            Log.e("CHECK", detectedText);
                        }
                        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
                        final Canvas canvas = new Canvas(cropCopyBitmap);
                        final Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setStyle(Style.STROKE);
                        paint.setStrokeWidth(2.0f);

                        float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                        switch (MODE) {
                            case TF_OD_API:
                                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                                break;
                        }

                        final List<Classifier.Recognition> mappedRecognitions =
                                new LinkedList<Classifier.Recognition>();

                        for (final Classifier.Recognition result : results) {
                            final RectF location = result.getLocation();
                            if (location != null && result.getConfidence() >= minimumConfidence) {
                                canvas.drawRect(location, paint);

                                cropToFrameTransform.mapRect(location);

                                result.setLocation(location);
                                mappedRecognitions.add(result);
                            }
                        }

                        tracker.trackResults(mappedRecognitions, currTimestamp);
                        trackingOverlay.postInvalidate();

                        computingDetection = false;

                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showFrameInfo(previewWidth + "x" + previewHeight);
                                        showCropInfo(cropCopyBitmap.getWidth() + "x" + cropCopyBitmap.getHeight());
                                        showInference(lastProcessingTimeMs + "ms");
                                    }
                                });
                    }
                });
    }



    /**----------------------------------------------------------------------------
     *                                function
     * ----------------------------------------------------------------------------
     */
    // check carplate with database and insert new scan row
    // btnSubmit function
    private void fnSubmit(String carplate)
    {
        // check with database, if same, then will continue
        // remove the spaces between the carplate
        carplate = carplate.replace(" ", "");

        // check if the carplate is in the detected string, if yes, take that carplate from database


        // check with database to see if car plate exist
        if (!carplate.equals("")){
            Handler handler = new Handler();
            String finalTxtCarPlate = carplate;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // Starting Write and Read data with URL
                    // Creating array for php parameters
                    String[] field = new String[1];
                    field[0] = "vehicleCarPlate";

                    // Creating data for php
                    String[] data = new String[1];
                    data[0] = finalTxtCarPlate;

                    // connect to database (check whether booked in booking table))
                    // (if passed, stop scanning for 5s, else stop scanning for 2 second)
                    PutData putData = new PutData("http://10.131.76.99/loginregister/carPlateOCR.php", "POST", field, data);

                    if(putData.startPut()){
                        if(putData.onComplete()) {
                            String result = putData.getResult();
                            if(result.equals("Car plate exist")) {
                                // produce beep sound
                                fnPassSound();

                                // encrypt the carplate, to keep under QRCode (AES)
                                // declare secret key
                                final String secretKey = "dontSay";

                                // take the car plate to encrypt
                                String encryptedPlate = AES.encrypt(finalTxtCarPlate, secretKey);


                                // get the bookingID, where carplate == finalTxtCarplate (use php)
                                // insert into Scanning database (insert in Scanning table [booking_ID, PlateNumber and ecrypted QRCode, status == First (first scan)]
                                fnGetBookID(finalTxtCarPlate, encryptedPlate, "first");


                            }
                            else {
                                // produce warning sound
                                fnFailSound();
                                // stop the scanning for 5s
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // a method that want to run after the wait
                                    }
                                }, 2000);
                                // stop the scanning for 2s
                                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
    }

    // AAA0000
    // get the bookingID, where carplate == finalTxtCarplate (use php)
    public void fnGetBookID(String finalTxtCarPlate, String qrCode, String status)
    {
        // for getting the scan number
        //ArrayList<Integer> scanIDIntList = new ArrayList<>();
        ArrayList<String> bookingID = new ArrayList<>();

        String carplate = finalTxtCarPlate;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://10.131.76.99/loginregister/getBookingIDOCR.php?carplate=" + carplate;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject scanObj = jsonArray.getJSONObject(i);
                        //JSONObject scanObj = response.getJSONObject(i);
                        String bookID = scanObj.getString("ID");
                        bookingID.add(bookID);

                        // check whether the carplate exist in the scanning database
                        fnCheckCarPlateScan(bookingID.get(0), carplate, qrCode, status);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    // remove the leading zero of string during retrieve of scan ID
    public static String removeZero(String str)
    {

        // Count leading zeros

        // Initially setting loop counter to 0
        int i = 0;
        while (i < str.length() && str.charAt(i) == '0')
            i++;

        // Converting string into StringBuffer object
        // as strings are immutable
        StringBuffer sb = new StringBuffer(str);

        // The StringBuffer replace function removes
        // i characters from given index (0 here)
        sb.replace(0, i, "");

        // Returning string after removing zeros
        return sb.toString();
    }

    // check if there is the same carplate, if yes, passed
    // insert a new Scan row
    public void fnInsertNewScan(String bookingID, String plateNumber, String qrCode, String status)
    {
        ArrayList<Integer> scanIDIntList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://10.131.76.99/loginregister/getNewScanID.php";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("No scanning yet")){
                        scanIDIntList.add(0);
                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject scanObj = jsonArray.getJSONObject(i);
                            //JSONObject scanObj = response.getJSONObject(i);
                            String scanID = scanObj.getString("ID");
                            // get substr and convert to int
                            scanID = scanID.substring(2);
                            scanID = removeZero(scanID);
                            int number = Integer.parseInt(scanID);

                            scanIDIntList.add(number);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // get the largest number and create the new ID
                int max = Collections.max(scanIDIntList) + 1;
                String newScanID;
                if (max >= 10000 && max <= 99999)
                {
                    newScanID = "S" + Integer.toString(max);
                }
                else if (max >= 1000 && max < 10000)
                {
                    newScanID = "S0" + Integer.toString(max);
                }
                else if (max >= 100 && max < 1000)
                {
                    newScanID = "S00" + Integer.toString(max);
                }
                else if (max >= 10 && max < 100)
                {
                    newScanID = "S000" + Integer.toString(max);
                }
                else
                {
                    newScanID = "S0000" + Integer.toString(max);
                }

                // insert all into the scanning database
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Starting Write and Read data with URL
                        // Creating array for parameters
                        String[] field = new String[5];
                        field[0] = "scanID";
                        field[1] = "bookingID";
                        field[2] = "plateNumber";
                        field[3] = "qrCode";
                        field[4] = "status";

                        // Creating array for data
                        String[] data = new String[5];
                        data[0] = newScanID;
                        data[1] = bookingID;
                        data[2] = plateNumber;
                        data[3] = qrCode;
                        data[4] = status;

                        PutData putData = new PutData("http://10.131.76.99/loginregister/insertNewScan.php", "POST", field, data);
                        if(putData.startPut()) {
                            if(putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Insert Scan row success")) {
                                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        //End Write and Read Data with URL
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    // check if have same carplate in scanning database
    public void fnCheckCarPlateScan(String bookingID, String carplate, String qrCode, String status){
        Handler handler = new Handler();
        String finalTxtCarPlate = carplate;
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Starting Write and Read data with URL
                // Creating array for php parameters
                String[] field = new String[1];
                field[0] = "vehicleCarPlate";

                // Creating data for php
                String[] data = new String[1];
                data[0] = finalTxtCarPlate;

                // connect to database (check whether booked in booking table))
                // (if passed, stop scanning for 5s, else stop scanning for 2 second)
                PutData putData = new PutData("http://10.131.76.99/loginregister/carPlateOCRCheckScanTable.php", "POST", field, data);

                if(putData.startPut()){
                    if(putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Car plate absent")) {
                            fnInsertNewScan(bookingID, carplate, qrCode, status);
                        }
                        else {
                            fnFailSound();
                        }
                    }
                }
            }
        });
    }

    // take all the carplate from Booking to an arraylist
    // use for loop to check whether it is in the detected string
    // if yes, return that string
    public void fnReturnCarplate(String detectedStr)
    {
        ArrayList<String> bookingCarPlateList = new ArrayList<>();
        final String[] correctCarPlate = new String[1];
        correctCarPlate[0] = "No match";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://10.131.76.99/loginregister/getAllBookingCarPlate.php";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("No booking yet")){
                        bookingCarPlateList.add("No booking");
                    }
                    else{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject scanObj = jsonArray.getJSONObject(i);
                            //JSONObject scanObj = response.getJSONObject(i);
                            String bookedCarPlate = scanObj.getString("Plate_Number");

                            bookingCarPlateList.add(bookedCarPlate);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (bookingCarPlateList.get(0) == "No booking")
                {
                    correctCarPlate[0] = bookingCarPlateList.get(0);
                }
                else
                {
                    //  use for loop to check whether the carplate is in the detectedStr
                    // if yes, returned the carplate, else, return no carplate
                    for (int j = 0; j < bookingCarPlateList.size(); j++){
                        String curCarPlate = bookingCarPlateList.get(j);

                        if (detectedStr.contains(curCarPlate))
                        {
                            correctCarPlate[0] = curCarPlate;
                            break;
                        }
                    }
                }

                // do the next step
                if (correctCarPlate[0] == "No match" || correctCarPlate[0] == "No booking")
                {
                    fnFailSound();
                }
                else
                {
                    fnSubmit(correctCarPlate[0]);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
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

    // the end of the function

    // image to text
    public String detect(Bitmap bitmap) {
        //perform text detection here

        //TODO 1. define TextRecognizer
        TextRecognizer recognizer = new TextRecognizer.Builder(DetectorActivity.this).build();

        //TODO 2. Get bitmap from imageview
        // Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        //TODO 3. get frame from bitmap
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        //TODO 4. get data from frame
        SparseArray<TextBlock> sparseArray =  recognizer.detect(frame);

        //TODO 5. set data on textview
        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0;i < sparseArray.size(); i++){
            TextBlock tx = sparseArray.get(i);
            String str = tx.getValue();

            stringBuilder.append(str);
        }

        String ans = stringBuilder.toString();
        return ans;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tfe_od_camera_connection_fragment_tracking;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
    // checkpoints.
    private enum DetectorMode {
        TF_OD_API;
    }

    @Override
    protected void setUseNNAPI(final boolean isChecked) {
        runInBackground(() -> detector.setUseNNAPI(isChecked));
    }

    @Override
    protected void setNumThreads(final int numThreads) {
        runInBackground(() -> detector.setNumThreads(numThreads));
    }


}
