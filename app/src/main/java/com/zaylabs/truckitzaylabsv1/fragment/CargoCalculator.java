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
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.zaylabs.truckitzaylabsv1.MainActivity;
import com.zaylabs.truckitzaylabsv1.R;

import com.zaylabs.truckitzaylabsv1.SignInActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CargoCalculator extends Fragment {


    private FirebaseFirestore db;
    //Firebase Start
    private StorageReference mImageRef;
    private DatabaseReference mDBRef;
    private FirebaseAuth mAuth;
    private String userID;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    //Firebase End


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
    //GeoFire
    private DatabaseReference mSuzukiDriverLocation;
    private DatabaseReference mRikshaDriverLocation;
    private GeoFire mgeoFire;
    private int mRadius = 1;
    private Boolean mdriverFound = false;
    private String mdriverFoundID;
    private LatLng mPickupLocation;
    private GeoQuery mgeoQuery;
    private Boolean requestBol = false;
    private String requestService;
    private String mDropPlaceName;
    private LatLng mDropLatLng;
    private LinearLayout mDriverInfo;
    private ImageView mDriverProfileImage;
    private TextView mDriverName, mDriverPhone, mDriverCar;
    private Marker mDriverMarker;
    private DatabaseReference SuzukiDriverRef;
    private ValueEventListener SuzukiDriverLocationRefListener;
    private RatingBar mRatingBar;
    private DatabaseReference mCRRef;

    //Suzuki Driver Found
    private int suzukiRadius = 1;
    private boolean suzukiDriverFound = false;
    private String suzukiDriverFoundID;


    private GeoFire geoSuzukiFire;
    private GeoQuery geoSuzukiQuery;
    private GeoFire geoRikshaFire;
    private GeoQuery geoRikshaQuery;


    //Riksha Driver Found
    private int rikshaRadius = 1;
    private boolean riskshaDriverFound = false;
    private String rikshaDriverFoundID;


    public CargoCalculator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_cargo_calculator, container, false);

        //Firebase Start
        mStorageRef = FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userID = mAuth.getCurrentUser().getUid();
        mDBRef = mDatabase.child("CurrentRide");
        /*mSuzukiDriverLocation = mDatabase.child("driversAvailable").child("VT1");
        mRikshaDriverLocation = mDatabase.child("driversAvailable").child("VT2");
        mgeoFire = new GeoFire(mSuzukiDriverLocation);
        mCRRef = FirebaseDatabase.getInstance().getReference("customerRequest").child(userID);
        geoSuzukiFire = new GeoFire(mSuzukiDriverLocation);
//        geoSuzukiQuery = geoSuzukiFire.queryAtLocation(new GeoLocation(mPickupLocation.latitude, mPickupLocation.longitude), suzukiRadius);
        geoRikshaFire = new GeoFire(mRikshaDriverLocation);
*/  //      geoRikshaQuery = geoSuzukiFire.queryAtLocation(new GeoLocation(mPickupLocation.latitude, mPickupLocation.longitude), suzukiRadius);

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
        mPickupLocation = new LatLng(((MainActivity) getActivity()).mPickUpLatLng.latitude, ((MainActivity) getActivity()).mPickUpLatLng.longitude);

        mDropLatLng = ((MainActivity) getActivity()).mDropLatLng;
        mDropPlaceName = ((MainActivity) getActivity()).mDropName;
        mDriverInfo = view.findViewById(R.id.driverInfo);

        mDriverProfileImage = view.findViewById(R.id.driverProfileImage);

        mDriverName = view.findViewById(R.id.driverName);
        mDriverPhone = view.findViewById(R.id.driverPhone);
        mDriverCar = view.findViewById(R.id.driverCar);

        mRatingBar = view.findViewById(R.id.ratingBar);


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
                        fareCarType1Calculator();
                        break;
                    case 1: // secondbutton
                        requestService = mCarType2.getText().toString();
                        fareCarType2Calculator();
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*geoSuzukiQuery.removeAllListeners();
                geoRikshaQuery.removeAllListeners();*/
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
                Map<String, Object> requestmap = new HashMap<>();
                GeoPoint pickup = new GeoPoint(mPickupLocation.latitude,mPickupLocation.longitude);
                requestmap.put("pickup", pickup);
                GeoPoint drop=new GeoPoint(mDropLatLng.latitude,mDropLatLng.longitude);
                requestmap.put("drop", drop);
                db.collection("customerRequest").document(userID).set(requestmap);


                /*GeoFire geoFireCR = new GeoFire(mCRRef);
                geoFireCR.setLocation("PickUpLocation", new GeoLocation(mPickupLocation.latitude, mPickupLocation.longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            Toast.makeText(view.getContext(), "There was an error saving the location to GeoFire: " + error, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(view.getContext(), "Location saved on server successfully!", Toast.LENGTH_LONG).show();

                        }
                    }

                });
                geoFireCR.setLocation("DropLocation", new GeoLocation(mDropLatLng.latitude, mDropLatLng.longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            Toast.makeText(view.getContext(), "There was an error saving the location to GeoFire: " + error, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(view.getContext(), "Location saved on server successfully!", Toast.LENGTH_LONG).show();

                        }
                    }

                });
*/
                if (!(mCarType2.isChecked())) {
                    mConfirm.setText("Getting your Suzuki Driver....");
                   // getSuzukiDriver();
                } else if (!(mCarType1.isChecked())) {
                    mConfirm.setText("Getting your Riksha Driver....");
                    //getRikshaDriver();
                }
                ;

            }
        });
        return view;
    }


    public void fareCarType1Calculator() {
        Double result = 0.0;
        Double b;
        Double a = Double.parseDouble(mdistance.getText().toString().trim());
        if (!(mCarType2.isChecked())) {
            b = (a * 200) + 600;
            if ((mDriverLoading.isChecked())) {
                result = b + 150;
            } else {
                result = b;
            }
        }
            String results = result.toString();
        mfare.setText(results);
    }

    public void fareCarType2Calculator() {
        Double result = 0.0;
        Double b;
        Double a = Double.parseDouble(mdistance.getText().toString().trim());

        if (!(mCarType1.isChecked())) {
            b = (a * 90) + 270;
            if ((mDriverLoading.isChecked())) {
                result = b + 150;
            } else {
                result = b;
            }
        }
        String results = result.toString();
        mfare.setText(results);
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
}




