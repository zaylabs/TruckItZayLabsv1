package com.zaylabs.truckitzaylabsv1.RecylerViewAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.zaylabs.truckitzaylabsv1.DTO.acceptRequest;
import com.zaylabs.truckitzaylabsv1.R;

import java.util.List;

public class currentRideAdapter extends RecyclerView.Adapter<currentRideAdapter.ViewHolder> {

private final Context context;
private final List<acceptRequest> dHistory;

public currentRideAdapter(Context context, List<acceptRequest> dHistory){

        this.context = context;
        this.dHistory = dHistory;
        }

@NonNull
@Override
public currentRideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_ride_siglet_item,parent,false);
        return new currentRideAdapter.ViewHolder(view);


        }

@Override
public void onBindViewHolder(@NonNull currentRideAdapter.ViewHolder holder, int position) {

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
    public Button mReached,mStart, mPayment, mFinish;

    public ViewHolder(View itemView) {
        super(itemView);
        mView= itemView;
        mName=(TextView)mView.findViewById(R.id.CRcustomername);
        mPickup=(TextView)mView.findViewById(R.id.CRpickupaddress);
        mDrop=(TextView)mView.findViewById(R.id.CRdropaddress);
        mPhone=(TextView)mView.findViewById(R.id.CRcustomerphone);
        mstatus=(TextView)mView.findViewById(R.id.CRstatus);
        mRideDistance=(TextView)mView.findViewById(R.id.CRridedistance);
        mDiscription=(TextView)mView.findViewById(R.id.CRdescription);
        mPaymentStatus=(TextView)mView.findViewById(R.id.CRpaymentstatus);
        mPaidVia=(TextView)mView.findViewById(R.id.CRpaidvia);
        mWeight=(TextView)mView.findViewById(R.id.CRweight);
        mBoxes=(TextView)mView.findViewById(R.id.CRBoxes);
        mfare=(TextView)mView.findViewById(R.id.CRfare);


    }
}
    }


