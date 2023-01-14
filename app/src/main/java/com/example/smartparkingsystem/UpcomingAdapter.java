package com.example.smartparkingsystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.security.AccessController;
import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder>{

    private Context ctx;
    private List<UpcomingClass> upcomingList;

    QRGenerator qrGenerator;

    public UpcomingAdapter(Context ctx, List<UpcomingClass> upcomingList) {
        this.ctx = ctx;
        this.upcomingList = upcomingList;
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_upcoming, null);
        return new UpcomingAdapter.UpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, int position) {

        UpcomingClass upcomingClass = upcomingList.get(position);

        holder.startDate.setText(upcomingClass.getStartDate());
        holder.endDate.setText(upcomingClass.getEndDate());
        holder.carPlate.setText(upcomingClass.getCarPlate());
        holder.duration.setText(upcomingClass.getDuration());
        holder.startTime.setText(upcomingClass.getStartTime());
        holder.endTime.setText(upcomingClass.getEndTime());
        holder.station.setText(upcomingClass.getStation());

        String plateNumber = upcomingClass.getCarPlate();
        // create new QRGenerator object
        qrGenerator = new QRGenerator(plateNumber);

        // encrypt the carplate
        String encryptedCarPlate = qrGenerator.thirdScanEncryption();

        UpcomingClass.getInstance().setQrCode(encryptedCarPlate);

        holder.qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new QRCodeFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return upcomingList.size();
    }

    class UpcomingViewHolder extends RecyclerView.ViewHolder {

        TextView startTime, endTime, duration, startDate, endDate, station, carPlate;
        Button qrcode;

        public UpcomingViewHolder(View itemView) {
            super(itemView);

            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            duration = itemView.findViewById(R.id.duration);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            station = itemView.findViewById(R.id.station);
            carPlate = itemView.findViewById(R.id.carPlate);
            qrcode = itemView.findViewById(R.id.qrcode);



        }
    }
}
