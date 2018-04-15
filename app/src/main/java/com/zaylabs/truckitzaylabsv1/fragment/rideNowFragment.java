package com.zaylabs.truckitzaylabsv1.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.zaylabs.truckitzaylabsv1.DTO.customerHistory;
import com.zaylabs.truckitzaylabsv1.DTO.customerRequest;
import com.zaylabs.truckitzaylabsv1.DTO.userProfile;
import com.zaylabs.truckitzaylabsv1.ForgetPasswordActivity;
import com.zaylabs.truckitzaylabsv1.MainActivity;
import com.zaylabs.truckitzaylabsv1.R;

import com.zaylabs.truckitzaylabsv1.SignInActivity;
import com.zaylabs.truckitzaylabsv1.currentRide;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class rideNowFragment extends Fragment {



    //Firebase Start
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;
       //Firebase End


    private Button mConfirm, mCancel;
    private RadioGroup mRadioGroup;
    private RadioButton mCarType1;
    private RadioButton mCarType2;
    private Map mSMap;
    private SwipeRefreshLayout mSwipeRefresh;
    private EditText mDesc, mBox_Number;
    private Spinner mWeight;
    private TextView mfare, mdistance;
    private CheckBox mDriverLoading;
    private String mDistancePassed;
    private String estFare;
    private TextView mFareEstimate;
    private LatLng mPickupLocation;
    private LatLng mDropLatLng;
    private String requestService;
    private String mPhone;
    private String mPickupAddress;
    private String mDAddress;
    private String uniqueID;
    public rideNowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_cargo_calculator, container, false);

        //Firebase Start


        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getCurrentUser().getUid();

        mDesc = view.findViewById(R.id.txt_Desc);
        mfare = view.findViewById(R.id.txt_fare);
        mdistance = view.findViewById(R.id.distance);
        mBox_Number = view.findViewById(R.id.number_Boxes);
        mWeight = view.findViewById(R.id.weight);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);
        mRadioGroup = view.findViewById(R.id.radiogroup_VType);
        mCarType1 = view.findViewById(R.id.radio_CarType1);
        mCarType2 = view.findViewById(R.id.radio_CarType2);
        mCancel = view.findViewById(R.id.btn_cancel);
        mConfirm = view.findViewById(R.id.btn_confirm);
        mDriverLoading = view.findViewById(R.id.dirver_loading);
        mPickupLocation = new LatLng(((MainActivity) getActivity()).mPickUpLatLng.latitude, ((MainActivity) getActivity()).mPickUpLatLng.longitude);
        mFareEstimate=view.findViewById(R.id.textView_fare);
        mPickupAddress = ((MainActivity) getActivity()).mPickupAddress;
        mDAddress = ((MainActivity) getActivity()).mDropoffAddress;
        mDropLatLng = ((MainActivity) getActivity()).mDropLatLng;

        DocumentReference docRef = db.collection("customers").document(userID);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userProfile profile = documentSnapshot.toObject(userProfile.class);
                mPhone =profile.getPhone();

            }

        });



// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.wightinkg, android.R.layout.simple_spinner_dropdown_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mWeight.setAdapter(adapter);

        mdistance.setText(((MainActivity) getActivity()).mDistancetoPass.getText());
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = mRadioGroup.findViewById(checkedId);
                int index = mRadioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button
                        requestService = mCarType1.getText().toString();
                        break;
                    case 1: // secondbutton
                        requestService = mCarType2.getText().toString();
                        }
            }
        });


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).mHeader.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).mFooter.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).setDrawerState(true);
                if (!((MainActivity) getActivity()).sMapFragment.isAdded())
                    ((MainActivity) getActivity()).sFm.beginTransaction().add(R.id.map, ((MainActivity) getActivity()).sMapFragment).commit();
                else
                    ((MainActivity) getActivity()).sFm.beginTransaction().show(((MainActivity) getActivity()).sMapFragment).commit();
                
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mCancel.setVisibility(View.INVISIBLE);
                GeoPoint pickup = new GeoPoint(mPickupLocation.latitude,mPickupLocation.longitude);
                GeoPoint drop=new GeoPoint(mDropLatLng.latitude,mDropLatLng.longitude);
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String name = Objects.requireNonNull(user).getDisplayName();
//                String photodp = Objects.requireNonNull(user.getPhotoUrl()).toString();
                String phone = mPhone;
                Date date =  Calendar.getInstance().getTime();
                String CID= userID;
                String VT;
                if (!(mCarType2.isChecked())){
                    VT="Suzuki";
                }else
                {
                    VT="Riksha";
                }
                String weight= mWeight.getSelectedItem().toString();
                String boxes = mBox_Number.getText().toString();
                String description= mDesc.getText().toString();
                String driverloading;

                if (mDriverLoading.isChecked()){
                    driverloading ="Diver Loading Needed";
                }else  {
                    driverloading="DriverLoading Not Needed";
                }
                Float ridedistance =((MainActivity) getActivity()).distance ;
                String dropaddress = mDAddress;
                String pickupaddress = mPickupAddress;
                uniqueID= userID+date;
                customerRequest customerRequest=new customerRequest(name,pickup,drop,phone,date,CID,VT,weight,boxes,description,driverloading,ridedistance,pickupaddress,dropaddress, estFare, uniqueID);
                customerHistory customerHistory=new customerHistory(name,pickup,drop,null,null,phone,date,CID,VT,weight,boxes,description,driverloading,ridedistance,pickupaddress,dropaddress,estFare,null,null,null,null,null,null,null,"Pending",null,null,null,null, uniqueID);

                db.collection("customerRequest").document(uniqueID).set(customerRequest);
                db.collection("CustomerHistory").document(uniqueID).set(customerHistory);

                currentRide();


            }
        });

        mFareEstimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fareCarCalculator();
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fareCarCalculator();
            }
        });
        mDriverLoading.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fareCarCalculator();
            }
        });
        return view;
    }


    public void fareCarCalculator() {
        Double result = 0.0;
        Double b;
        Double a = Double.parseDouble(mdistance.getText().toString().trim());
        if (!(mCarType2.isChecked())) {
            b = (a * 200) + 600;
            if ((mDriverLoading.isChecked())) {
                result = b + 150;
            } else {
                result = b;
            }}
         else if (!(mCarType1.isChecked())) {
                b = (a * 90) + 270;
                if ((mDriverLoading.isChecked())) {
                    result = b + 150;
                } else {
                    result = b;
                }
            }
            String results = result.toString();
            estFare=results;
            mfare.setText(results);

        mConfirm.setVisibility(VISIBLE);
        mCancel.setVisibility(VISIBLE);
    }

    private void currentRide() {

        Intent intent = new Intent(getActivity(), currentRide.class);
        startActivity(intent);

    }

}





// **********************************Get CLosest Driver ******************************************************

/*
    private void getSuzukiDriver() {

        geoSuzukiQuery.removeAllListeners();

        geoSuzukiQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!suzukiDriverFound) {
                    suzukiDriverFound = true;
                    suzukiDriverFoundID = key;
                    mConfirm.setText(suzukiDriverFoundID);
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!suzukiDriverFound) {
                    suzukiRadius++;
                    getSuzukiDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


    private void getRikshaDriver() {

            geoRikshaQuery.removeAllListeners();

            geoRikshaQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    if (!riskshaDriverFound) {
                        riskshaDriverFound = true;
                        rikshaDriverFoundID = key;
                        mConfirm.setText(rikshaDriverFoundID);
                    }
                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }

                @Override
                public void onGeoQueryReady() {
                    if (!riskshaDriverFound) {
                        rikshaRadius++;
                        getRikshaDriver();
                        mConfirm.setText(suzukiDriverFoundID);
                    }
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });

        }*/


