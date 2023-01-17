package com.example.smartparkingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private Context ctx;
    private List<HistoryClass> historyList;

    public HistoryAdapter(Context ctx, List<HistoryClass> historyList) {
        this.ctx = ctx;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_history, null);
        return new HistoryAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {

        HistoryClass historyClass = historyList.get(position);

        holder.startDate.setText(historyClass.getStartDate());
        holder.endDate.setText(historyClass.getEndDate());
        holder.carPlate.setText(historyClass.getCarPlate());
        holder.duration.setText(historyClass.getDuration());
        holder.startTime.setText(historyClass.getStartTime());
        holder.endTime.setText(historyClass.getEndTime());
        holder.station.setText(historyClass.getStation() + " Station");
        holder.total.setText("RM " + historyClass.getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView startTime, endTime, duration, startDate, endDate, station, carPlate, total;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            this.startTime = itemView.findViewById(R.id.startTime);
            this.endTime = itemView.findViewById(R.id.endTime);
            this.duration = itemView.findViewById(R.id.duration);
            this.startDate = itemView.findViewById(R.id.startDate);
            this.endDate = itemView.findViewById(R.id.endDate);
            this.station = itemView.findViewById(R.id.station);
            this.carPlate = itemView.findViewById(R.id.carPlate);
            this.total = itemView.findViewById(R.id.total);



        }
    }

}
