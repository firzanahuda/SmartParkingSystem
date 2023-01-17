package com.example.smartparkingsystem;


import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.CurrentViewHolder> {

    private Context ctx;
    private List<CurrentClass> currentList;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private Button mButtonSet;
    private Button mButtonStartPause;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;


    // add for extend
    private Button btnExtend;
    private EditText edtExtend;

    // notification
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;


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
        holder.carplate.append(currentClass.getCarPlate());
        holder.txtFloor.append(currentClass.getFloor());
        holder.txtParking.append(currentClass.getCode() + " 00" + currentClass.getSequence());

        String startDate = currentClass.getStarting_Date();
        String endDate = currentClass.getEnd_Date();
        String startTime = currentClass.getStart_Time();
        String endTime = currentClass.getEnd_Time();
        String carPlate = currentClass.getCarPlate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        String calculateEnd = endDate + " " + endTime;


        //countDown(calculateEnd);

        Date dateEndDate = new Date();

        // change string to date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:aa");
        try {
            dateEndDate = sdf.parse(calculateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // take the end date - current time
        final long[] diff = {dateEndDate.getTime() - System.currentTimeMillis()};


        // start the timer again
        new CountDownTimer(diff[0], 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                diff[0] = millisUntilFinished;
                int days = (int) (diff[0] / 1000) / 3600 / 24;
                int hours = (int) (diff[0] / 1000) / 3600 % 24;
                int minutes = (int) ((diff[0] / 1000) % 3600) / 60;
                int seconds = (int) (diff[0] / 1000) % 60;

                if (days == 0 && hours == 1 && minutes == 0 && seconds == 0) {
                    // send notificaiton if 1 hour left
                    //fnCloseNotification();

                    // notification
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(channel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mTextViewCountDown.getContext(), "myCh")
                            .setSmallIcon(android.R.drawable.star_on)
                            .setContentTitle("First Notification")
                            .setContentText("Hello World");

                    notification = builder.build();
                    notificationManagerCompat = NotificationManagerCompat.from(mTextViewCountDown.getContext());
                    // notification till here
                    notificationManagerCompat.notify(1, notification);*/

                }


                String timeLeftFormatted;

                timeLeftFormatted = String.format(Locale.getDefault(),
                        "%02d:%02d:%02d", days, hours, minutes);

                Log.e("time", timeLeftFormatted);

                holder.txtTimer.setText(timeLeftFormatted);


                // the timer text
                //txtDisplay.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                // give warning notification if times up
                //fnWarnNotification();

                // notification
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(mTextViewCountDown.getContext(), "myCh")
                        .setSmallIcon(android.R.drawable.star_on)
                        .setContentTitle("Times Up!")
                        .setContentText("Your booking time for vehicle is up.");

                notification = builder.build();
                notificationManagerCompat = NotificationManagerCompat.from(mTextViewCountDown.getContext());
                // notification till here
                notificationManagerCompat.notify(2, notification);*/


                // set to Penalty
            }
        }.start();


        holder.extend.setOnClickListener(view -> {

            int selectedPosition = holder.getAdapterPosition();

            if (selectedPosition == position) {


                Bundle bundle = new Bundle();
                bundle.putString("startDate", startDate);
                bundle.putString("endDate", endDate);
                bundle.putString("startTime", startTime);
                bundle.putString("endTime", endTime);
                bundle.putString("carPlate", carPlate);
                Intent intent = new Intent(view.getContext(), ExtendActivity.class);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);

                /*intent.putExtra("startDate", startDate);
                intent.putExtra("endDate", endDate);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);*/

            }
        });

    }

    private Object getSystemService(Class<NotificationManager> notificationManagerClass) {
        return null;
    }

    @Override
    public int getItemCount() {
        return currentList.size();
    }

    class CurrentViewHolder extends RecyclerView.ViewHolder {

        TextView station, txtTimer, carplate, txtFloor, txtParking;
        Button extend;

        public CurrentViewHolder(View itemView) {
            super(itemView);

            station = itemView.findViewById(R.id.txtStation);
            carplate = itemView.findViewById(R.id.carPlate);
            extend = itemView.findViewById(R.id.extend);
            txtTimer = itemView.findViewById(R.id.txtTimer);
            txtFloor = itemView.findViewById(R.id.txtFloor);
            txtParking = itemView.findViewById(R.id.txtParking);



        }
    }



/*
    // notification
    public void fnCloseNotification()
    {

        NotificationManagerCompat notificationManagerCompat;
        Notification notification;
        // notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mTextViewCountDown.getContext(), "myCh")
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("First Notification")
                .setContentText("Hello World");

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(mTextViewCountDown.getContext());
        // notification till here
        notificationManagerCompat.notify(1, notification);
    }

    // warning notification
    public void fnWarnNotification()
    {

        NotificationManagerCompat notificationManagerCompat;
        Notification notification;
        // notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mTextViewCountDown.getContext(), "myCh")
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Times Up!")
                .setContentText("Your booking time for vehicle is up.");

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(mTextViewCountDown.getContext());
        // notification till here
        notificationManagerCompat.notify(2, notification);
    }*/






}



