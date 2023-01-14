package com.example.smartparkingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.CurrentViewHolder>{

    private Context ctx;
    private List<CurrentClass> currentList;

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

        TextView station;
        Button extend;

        public CurrentViewHolder(View itemView) {
            super(itemView);

            station = itemView.findViewById(R.id.txtStation);

            extend = itemView.findViewById(R.id.extend);



        }
    }
}



