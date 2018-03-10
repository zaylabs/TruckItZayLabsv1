package com.zaylabs.truckitzaylabsv1.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.zaylabs.truckitzaylabsv1.MainActivity;
import com.zaylabs.truckitzaylabsv1.R;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CargoCalculator extends Fragment {

    private Button mConfirm, mCancel;
    private RadioGroup mRadioGroup;
    private RadioButton mCarType1;
    private RadioButton mCarType2;
    private Map mSMap;
    private SwipeRefreshLayout mSwipeRefresh;
    private EditText mDesc, mBox_Number, mWeight;
    private TextView mfare, mdistance;
    private CheckBox mDriverLoading;
    private String mDistancePassed;

    public CargoCalculator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_cargo_calculator, container, false);


        mDesc = view.findViewById(R.id.txt_Desc);
        mfare = view.findViewById(R.id.txt_fare);
        mdistance = view.findViewById(R.id.distance);
        mBox_Number = view.findViewById(R.id.number_Boxes);
        mWeight = view.findViewById(R.id.Weight);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);
        mRadioGroup = view.findViewById(R.id.radiogroup_VType);
        mCarType1 = view.findViewById(R.id.radio_CarType1);
        mCarType2 = view.findViewById(R.id.radio_CarType2);
        mCancel = view.findViewById(R.id.btn_cancel);
        mConfirm = view.findViewById(R.id.btn_confirm);
        mDriverLoading = view.findViewById(R.id.dirver_loading);

        mdistance.setText(((MainActivity) getActivity()).mDistancetoPass.getText());

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = mRadioGroup.findViewById(checkedId);
                int index = mRadioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button

                        fareCarType1Calculator();
                        break;
                    case 1: // secondbutton

                        fareCarType2Calculator();
                }
            }
        });


        return view;
    }


    public void fareCarType1Calculator() {
        Double result = 0.0;
        Double b;
        Double a = Double.parseDouble(mdistance.getText().toString().trim());
        if (!(mCarType2.isChecked())) {
            b = a * 200;
            if ((mDriverLoading.isChecked())) {
                result = b + 150;
            } else {
                result = b;
            }
        }
        String results= result.toString();
        mfare.setText(results);
    }

    public void fareCarType2Calculator() {
        Double result = 0.0;
        Double b;
        Double a = Double.parseDouble(mdistance.getText().toString().trim());

        if (!(mCarType1.isChecked())) {
            b = a * 90;
            if ((mDriverLoading.isChecked())) {
                result = b + 150;
            } else {
                result = b;
            }
        }
        String results= result.toString();
        mfare.setText(results);
    }
}



