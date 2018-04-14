package com.zaylabs.truckitzaylabsv1.RecylerViewAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaylabs.truckitzaylabsv1.DTO.customerHistory;
import com.zaylabs.truckitzaylabsv1.R;

import java.util.List;

public class driverHistoryAdapter extends RecyclerView.Adapter<driverHistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<customerHistory> dHistory;

    public driverHistoryAdapter(Context context, List<customerHistory> dHistory){

        this.context = context;
        this.dHistory = dHistory;
    }

    @NonNull
    @Override
    public driverHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driverhistory_single_item,parent,false);
        return new driverHistoryAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull driverHistoryAdapter.ViewHolder holder, int position) {

        holder.mName.setText(dHistory.get(position).getDrivername());
        holder.mPickup.setText(dHistory.get(position).getPickupaddress());
        holder.mDrop.setText(dHistory.get(position).getDropaddress());
        holder.mPhone.setText(dHistory.get(position).getDriverphone());
        holder.mstatus.setText(dHistory.get(position).getStatus());
        holder.mRideDistance.setText(String.valueOf(dHistory.get(position).getRidedistance()));
        holder.mDiscription.setText(dHistory.get(position).getDescription());
        holder.mBoxes.setText(dHistory.get(position).getBoxes());
        holder.mWeight.setText(dHistory.get(position).getWeight());
        holder.mPaymentStatus.setText(dHistory.get(position).getPaymentstatus());
        holder.mPaidVia.setText(dHistory.get(position).getPaidvia());
        holder.mfare.setText(dHistory.get(position).getRidefare());




    }

    @Override
    public int getItemCount() {
        return dHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView mName,mPickup,mDrop,mPhone, mstatus, mDiscription, mBoxes, mRideDistance,mWeight, mPaymentStatus,mPaidVia, mfare;


        public ViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
            mName=(TextView)mView.findViewById(R.id.customername);
            mPickup=(TextView)mView.findViewById(R.id.pickupaddress);
            mDrop=(TextView)mView.findViewById(R.id.dropaddress);
            mPhone=(TextView)mView.findViewById(R.id.customerphone);
            mstatus=(TextView)mView.findViewById(R.id.dhstatus);
            mRideDistance=(TextView)mView.findViewById(R.id.dhridedistance);
            mDiscription=(TextView)mView.findViewById(R.id.dhdescription);
            mPaymentStatus=(TextView)mView.findViewById(R.id.dhpaymentstatus);
            mPaidVia=(TextView)mView.findViewById(R.id.dhpaidvia);
            mWeight=(TextView)mView.findViewById(R.id.dhweight);
            mBoxes=(TextView)mView.findViewById(R.id.dhBoxes);
            mfare=(TextView)mView.findViewById(R.id.dhfare);
        }
    }
    }
