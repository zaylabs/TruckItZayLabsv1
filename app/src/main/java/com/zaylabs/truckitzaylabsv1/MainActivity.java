package com.zaylabs.truckitzaylabsv1;


import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import com.zaylabs.truckitzaylabsv1.Adapter.PlaceAutocompleteAdapter;
import com.zaylabs.truckitzaylabsv1.fragment.CargoCalculator;
import com.zaylabs.truckitzaylabsv1.fragment.HelpFragment;
import com.zaylabs.truckitzaylabsv1.fragment.HistoryFragment;
import com.zaylabs.truckitzaylabsv1.fragment.ProfileFragment;
import com.zaylabs.truckitzaylabsv1.fragment.SettingsFragment;
import com.zaylabs.truckitzaylabsv1.fragment.WalletFragment;




public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback {

    private    Place mPlacePickup, mPlaceDrop;
    private FrameLayout mHeader;


        //AutoComplete Google Sample Start
    protected GeoDataClient mGeoDataClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mPickupText,mDropOffText;
    private TextView mPickUpDetailsText;
    private TextView mPickUpAttribution;
    private TextView mDropOffDetailsText;
    private TextView mDropOffAttribution;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    private LatLng mPickUpLatLng, mDropLatLng;
    private PlaceDetectionClient mPlaceDetectionClient;

    //Maps
    private Marker mNow;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    SupportMapFragment sMapFragment;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference mfirebaseDB;

    //Layout
    private TextView mNameField, mEmail, mTextViewDP;
    private ImageView mDisplayPic,mMyLocation, mClear;;
    private String userID;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sMapFragment = SupportMapFragment.newInstance();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHeader = findViewById(R.id.headerframe);
        mPickupText = findViewById(R.id.pickup_location);
        mDropOffText = findViewById(R.id.drop_location);
        mMyLocation = findViewById(R.id.current_location);
        mClear = findViewById(R.id.clear);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);
        mTextViewDP = hView.findViewById(R.id.TextViewDP);

        mEmail = hView.findViewById(R.id.TextViewUserEmail);
        mNameField = hView.findViewById(R.id.TextViewUser);
        mDisplayPic = hView.findViewById(R.id.displaypic);

        navigationView.getMenu().getItem(0).setCheckable(true);
        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        if (!sMapFragment.isAdded()) {
            sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
        } else {
            sFm.beginTransaction().show(sMapFragment).commit();
        }


        sMapFragment.getMapAsync(this);


//Firebase Start --------------------------------------------------------------

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mfirebaseDB = FirebaseDatabase.getInstance().getReference().child("Users").child("customer").child(userID);
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Toast.makeText(MainActivity.this, "User is Signed In", Toast.LENGTH_SHORT).show();
                    getUserInfo();
                } else {
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
//Firebase Auth End --------------------------------------------------------------------




        //AutoComplete Google Start

       // Construct a GeoDataClient for the Google Places API for Android.
        mGeoDataClient = Places.getGeoDataClient(this, null);
        //Auto Place Google End



        mMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mCurrentLocation!=null){
                 mPickUpLatLng = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
                  CameraUpdate mCameraCL= CameraUpdateFactory.newLatLngZoom(mPickUpLatLng,18);
                    mMap.moveCamera(mCameraCL);
                    mMap.animateCamera(mCameraCL);
                    mMap.addMarker(new MarkerOptions().position(mPickUpLatLng)
                            .title(mPickUpLatLng.toString()).draggable(true));

                }
            }
        });

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropOffText.setText("");


            }
        });

        mPickupText.setOnItemClickListener(mPickupTextClickListener);
        mDropOffText.setOnItemClickListener(mDropOffTextClickListener);

        mPickUpDetailsText = findViewById(R.id.pickup_details);
        mPickUpAttribution = findViewById(R.id.pickup_attribution);

        mDropOffDetailsText = findViewById(R.id.dropoff_details);
        mDropOffAttribution = findViewById(R.id.dropoff_attribution);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data Client.
        mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_GREATER_SYDNEY, null);
        mPickupText.setAdapter(mAdapter);
        mDropOffText.setAdapter(mAdapter);

        //Auto Complete Google End

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (sMapFragment.isAdded())
            sFm.beginTransaction().hide(sMapFragment).commit();

        if (id == R.id.Home){
            mHeader.setVisibility(View.VISIBLE);
            if (id == R.id.Home) {
                if (!sMapFragment.isAdded())
                    sFm.beginTransaction().add(R.id.map,sMapFragment).commit();
                else
                    sFm.beginTransaction().show(sMapFragment).commit();}
        } else if (id == R.id.Profile) {
            mHeader.setVisibility(View.GONE);
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new ProfileFragment());
            ft.commit();
        } else if (id == R.id.History) {
            mHeader.setVisibility(View.GONE);
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new HistoryFragment());
            ft.commit();
        } else if (id == R.id.wallet) {
            mHeader.setVisibility(View.GONE);
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new WalletFragment());
            ft.commit();
        } else if (id == R.id.cargo_calculator) {
            mHeader.setVisibility(View.GONE);
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new CargoCalculator());
            ft.commit();
        } else if (id == R.id.action_settings) {
            mHeader.setVisibility(View.GONE);
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new SettingsFragment());
            ft.commit();
        } else if (id == R.id.logout) {
            mAuth.signOut();
        } else if (id == R.id.get_help) {
            mHeader.setVisibility(View.GONE);
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new HelpFragment());
            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);

    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);

    }

    private void getUserInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            mNameField.setText(name);
            String email = user.getEmail();
            mEmail.setText(email);
            if (user.getPhotoUrl()!=null){
                String photodp = user.getPhotoUrl().toString();
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View hView =  navigationView.getHeaderView(0);
                navigationView.setNavigationItemSelectedListener(this);
                Picasso.with(hView.getContext()).load(photodp).resize(150,150).centerCrop().into(mDisplayPic);
            }}
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        enableMyLocation();
        setOnMyLocationButtonClick();
        setOnMyLocationClick();
        mHeader.setVisibility(View.VISIBLE);

    }

    private void setOnMyLocationButtonClick() {
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(com.zaylabs.truckitzaylabsv1.MainActivity.this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
                // Return false so that we don't consume the event and the default behavior still occurs
                // (the camera animates to the user's current position).
                return false;
            }

        });
    }


    private void setOnMyLocationClick() {
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                Toast.makeText(com.zaylabs.truckitzaylabsv1.MainActivity.this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
                mCurrentLocation=location;
            }
        });
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(com.zaylabs.truckitzaylabsv1.MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(com.zaylabs.truckitzaylabsv1.MainActivity.this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }

    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    //Auto Complete Google Start
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data Client
     * to retrieve more details about the place.
     *
     * @see GeoDataClient#getPlaceById(String...)
     */
        private AdapterView.OnItemClickListener mPickupTextClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

       //     Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
             placeResult.addOnCompleteListener(mUpdatePickUpDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
           // Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);

        }
    };
    private AdapterView.OnItemClickListener mDropOffTextClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);
            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdateDropOffDetailsCallback);
            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
        }
    };

     // Callback for results from a Places Geo Data Client query that shows the first place result in
     // the details view on screen.
    private OnCompleteListener<PlaceBufferResponse> mUpdatePickUpDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();
                // Get the Place object from the buffer.
                final Place place = places.get(0);
                // Format details of the place for display and show it in a TextView.
                mPickUpDetailsText.setText(formatPickUpDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));

                mPickUpLatLng = place.getLatLng();
                CameraUpdate mCameraCL= CameraUpdateFactory.newLatLngZoom(mPickUpLatLng,18);
                mMap.moveCamera(mCameraCL);
                mMap.animateCamera(mCameraCL);
                mNow=mMap.addMarker(new MarkerOptions().position(mPickUpLatLng)
                        .title(place.getName().toString()).draggable(true));
                // Display the third party attributions if set.
                final CharSequence thirdPartyAttribution = places.getAttributions();
                if (thirdPartyAttribution == null) {
                    mPickUpAttribution.setVisibility(View.GONE);
                } else {
                    mPickUpAttribution.setVisibility(View.VISIBLE);
                    mPickUpAttribution.setText(
                            Html.fromHtml(thirdPartyAttribution.toString()));
                }
                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
    //           Log.e(TAG, "Place query did not complete.", e);
      //          return;
            }
        }
    };

    private static Spanned formatPickUpDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
    }

    // Dropoff
    private OnCompleteListener<PlaceBufferResponse> mUpdateDropOffDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();
                // Get the Place object from the buffer.
                final Place place = places.get(0);
                // Format details of the place for display and show it in a TextView.
                mDropOffDetailsText.setText(formatDropOffDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));
                mDropLatLng = place.getLatLng();
                // Display the third party attributions if set.
                final CharSequence thirdPartyAttribution = places.getAttributions();
                if (thirdPartyAttribution == null) {
                    mDropOffAttribution.setVisibility(View.GONE);
                } else {
                    mDropOffAttribution.setVisibility(View.VISIBLE);
                    mDropOffAttribution.setText(
                            Html.fromHtml(thirdPartyAttribution.toString()));
                }
                places.release();
            } catch (RuntimeRemoteException e) {

            }
        }
    };

    private static Spanned formatDropOffDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

}



