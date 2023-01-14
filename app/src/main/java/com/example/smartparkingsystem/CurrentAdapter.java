package com.example.smartparkingsystem;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.CurrentViewHolder>{

    private Context ctx;
    private List<CurrentClass> currentList;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    // notification
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    public CurrentAdapter(Context ctx, List<CurrentClass> currentList) {
        this.ctx = ctx;
        this.currentList = currentList;
    }

    @NonNull
    @Override
    public CurrentAdapter.CurrentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.activity_current_item, null);
        return new CurrentAdapter.CurrentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentAdapter.CurrentViewHolder holder, int position) {

        CurrentClass currentClass = currentList.get(position);

        holder.station.append(currentClass.getStation() + " Station");

        String startDate = currentClass.getStarting_Date();
        String endDate = currentClass.getEnd_Date();
        String startTime = currentClass.getStart_Time();
        String endTime = currentClass.getEnd_Time();

        // Creating a SimpleDateFormat object
        // to parse time in the format HH:MM:SS

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:aa");

        String calculateStart = startDate + " " + startTime;
        String calculateEnd = endDate + " " + endTime;

        // Parsing the Time Period
        Date dateStart = null;
        try {
            dateStart = simpleDateFormat.parse(calculateStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date dateEnd = null;
        try {
            dateEnd = simpleDateFormat.parse(calculateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Calculate time difference
        // in milliseconds
        long different = dateEnd.getTime() - dateStart.getTime();

        // Calculate time difference in
        // seconds, minutes, hours, years,
        // and days

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        //long elapsedDays = different / daysInMilli;
        //different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String duration = elapsedHours + " hours " + elapsedMinutes + " minutes ";


        holder.extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new ExtendFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return currentList.size();
    }

    class CurrentViewHolder extends RecyclerView.ViewHolder {

        TextView station, txtTimer;
        Button extend;

        public CurrentViewHolder(View itemView) {
            super(itemView);

            station = itemView.findViewById(R.id.txtStation);

            extend = itemView.findViewById(R.id.extend);
            txtTimer = itemView.findViewById(R.id.txtTimer);



        }
    }

    /*

    // add for extend
    // extend the time
    // limitation, can only be done if stop
    private void extendTime(long minutes)
    {
        pauseTimer();
        long millisInput = minutes * 60000;
        mStartTimeInMillis = mTimeLeftInMillis + millisInput;
        resetTimer();
        startTimer();
        closeKeyboard();
    }

    private void setTime(long milliseconds) {
        pauseTimer();
        mStartTimeInMillis = milliseconds;
        resetTimer();
        startTimer();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;
    }

    private void pauseTimer() {
        if (mTimerRunning == true)
        {
            mCountDownTimer.cancel();
        }
        mTimerRunning = false;
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
    }

    private void updateCountDownText() {
        int days = (int) (mTimeLeftInMillis / 1000) / 3600 / 24;
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600 % 24;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;

        timeLeftFormatted = String.format(Locale.getDefault(),
                "%02d:%02d:%02d", days, hours, minutes);

        mTextViewCountDown.setText(timeLeftFormatted);

        if (days == 0 && hours == 1 && minutes == 0 && seconds == 0)
        {
            // System.out.println("Notification done");
            // notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx.getApplicationContext(), "myCh")
                    .setSmallIcon(android.R.drawable.star_on)
                    .setContentTitle("First Notification")
                    .setContentText("Hello World");

            notification = builder.build();
            notificationManagerCompat = NotificationManagerCompat.from(ctx.getApplicationContext());
            // notification till here
            notificationManagerCompat.notify(1, notification);
        }
    }


    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis <= 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
            } else {
                startTimer();
            }
        }
    }*/
}



