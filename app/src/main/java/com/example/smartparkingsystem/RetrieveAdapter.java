package com.example.smartparkingsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetrieveAdapter extends RecyclerView.Adapter<RetrieveAdapter.RetrieveViewHolder>{

    private Context ctx;
    private List<RetrieveClass> retrieveList;


    QRCodeRetrieve qrCodeRetrieve;
    QRGenerator qrGenerator;



    public RetrieveAdapter(Context ctx, List<RetrieveClass> retrieveList) {
        this.ctx = ctx;
        this.retrieveList = retrieveList;
    }

    @NonNull
    @Override
    public RetrieveAdapter.RetrieveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.activity_retrieve_item, null);
        return new RetrieveAdapter.RetrieveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RetrieveAdapter.RetrieveViewHolder holder, int position) {

        RetrieveClass retrieveClass = retrieveList.get(position);

        holder.startDate.setText(retrieveClass.getStartDate());
        holder.endDate.setText(retrieveClass.getEndDate());
        holder.carPlate.setText(retrieveClass.getCarPlate());
        holder.vehicleType.setText(retrieveClass.getVehicleType());
        holder.startTime.setText(retrieveClass.getStartTime());
        holder.endTime.setText(retrieveClass.getEndTime());
        holder.parkingSlot.setText((retrieveClass.getFloor() + retrieveClass.getCode() + "00" + retrieveClass.getSequence()));



        String plateNumber = retrieveClass.getCarPlate();
        String qrcode = retrieveClass.getCarPlate();

        qrGenerator = new QRGenerator(qrcode);

        // encrypt the carplate
        String encryptedCarPlate = qrGenerator.thirdScanEncryption();

        Bitmap bitmap = qrGenerator.generateQRCode(encryptedCarPlate);
        holder.imageView2.setImageBitmap(bitmap);

        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), PaymentActivity.class);
                intent.putExtra("carPlate", retrieveClass.getCarPlate());
                v.getContext().startActivity(intent);



                /*AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new PaymentFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return retrieveList.size();
    }

    class RetrieveViewHolder extends RecyclerView.ViewHolder {

        TextView startTime, endTime, vehicleType, startDate, endDate, parkingSlot, penalty, carPlate;
        ImageView imageView2;
        Button pay;

        public RetrieveViewHolder(View itemView) {
            super(itemView);

            startTime = itemView.findViewById(R.id.txtStartTime);
            endTime = itemView.findViewById(R.id.txtEndTime);
            vehicleType = itemView.findViewById(R.id.txtVehicleType);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.txtEndDate);
            parkingSlot = itemView.findViewById(R.id.txtParkingSlot);
            carPlate = itemView.findViewById(R.id.txtCarPlate);
            penalty = itemView.findViewById(R.id.txtPenalty);
            pay = itemView.findViewById(R.id.pay);
            imageView2 = itemView.findViewById(R.id.imageView2);



        }
    }


}
