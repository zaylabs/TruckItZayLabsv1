package com.zaylabs.truckitzaylabsv1;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Places;
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
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import com.zaylabs.truckitzaylabsv1.Adapter.PlaceAutocompleteAdapter;
import com.zaylabs.truckitzaylabsv1.fragment.CargoCalculator;
import com.zaylabs.truckitzaylabsv1.fragment.HelpFragment;
import com.zaylabs.truckitzaylabsv1.fragment.HistoryFragment;
import com.zaylabs.truckitzaylabsv1.fragment.ProfileFragment;
import com.zaylabs.truckitzaylabsv1.fragment.SettingsFragment;
import com.zaylabs.truckitzaylabsv1.fragment.WalletFragment;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback {



    public android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
    public String mdistanceinKM;
    public TextView mDistancetoPass;
    private Place mPlacePickup, mPlaceDrop;
    public FrameLayout mHeader;
    public FrameLayout mFooter;
    private TextView mRideNow;
    private GoogleApiClient mGoogleApiClient;

    //AutoComplete Google Sample Start
    protected GeoDataClient mGeoDataClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mPickupText, mDropOffText;
    private TextView mPickUpDetailsText;
    private TextView mPickUpAttribution;
    private TextView mDropOffDetailsText;
    private TextView mDropOffAttribution;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    public LatLng mPickUpLatLng, mDropLatLng;
    private PlaceDetectionClient mPlaceDetectionClient;

    //Maps
    private Marker mPickupMarker, mDropMarker;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    public GoogleMap mMap;
    public SupportMapFragment sMapFragment;
    public String mDropName;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference mfirebaseDB;
    private DatabaseReference mAviableRikshaDrivers;
    private DatabaseReference mAviableSuzukiDrivers;
    //Layout
    private TextView mNameField, mEmail, mTextViewDP;
    private ImageView mDisplayPic, mMyLocation, mClear;
    ;
    private String userID;


    /*Location Update*/
    private static final String TAGLoc = MainActivity.class.getSimpleName();
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;
    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;
    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    // UI Widgets.
    /*private Button mStartUpdatesButton;*/
    /*private Button mStopUpdatesButton;*/
    private TextView mLastUpdateTimeTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    // Labels.
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private String mLastUpdateTimeLabel;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;
    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;

    //*******************************Location Update End********************************
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

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

        mFooter = findViewById(R.id.footerframe);
        mRideNow = findViewById(R.id.request_rides);
        mDistancetoPass=findViewById(R.id.distance_textview);

        mRideNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDistancetoPass.getText()!="") {
                    FragmentManager fm = getFragmentManager();
                    android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
                    sFm.beginTransaction().hide(sMapFragment).commit();
                    mHeader.setVisibility(GONE);
                    mFooter.setVisibility(GONE);
                    setDrawerState(false);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.cm, new CargoCalculator());
                    ft.commit();

                }else {
                    Toast.makeText(MainActivity.this, "Kindly add PickUp and Drop-Off Location", Toast.LENGTH_SHORT).show();
                };

                }

        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
       // toggle.syncState();
        setDrawerState(true);

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
        mAviableRikshaDrivers = FirebaseDatabase.getInstance().getReference().child("driversAvailable").child("VT2").child(userID);
        mAviableSuzukiDrivers = FirebaseDatabase.getInstance().getReference().child("driversAvailable").child("VT1").child(userID);
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

                if (mCurrentLocation != null) {
                    mPickUpLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    List<Address> addresses = null;
                    String currentAddress = "";
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(
                                mCurrentLocation.getLatitude(),
                                mCurrentLocation.getLongitude(),
                                // In this sample, get just a single address.
                                1);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        int max = address.getMaxAddressLineIndex();
                        if (max != -1) {
                            for (int i = 0; i < max; i++)

                                currentAddress += address.getAddressLine(i) + " ";
                            mPickupText.setText(currentAddress);
                        }
                        else
                            mPickupText.setText(mCurrentLocation.toString());
                    }


                    CameraUpdate mCameraCL = CameraUpdateFactory.newLatLngZoom(mPickUpLatLng, 18);
                    //mMap.moveCamera.(mCameraCL);

                    mMap.animateCamera(mCameraCL);
                    if (mPickupMarker != null) {
                        mPickupMarker.remove();
                    }

                    mPickupMarker = mMap.addMarker(new MarkerOptions().position(mPickUpLatLng)
                            .title(mPickUpLatLng.toString()).draggable(true));


                }
            }

            });

        mClear.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          mDropOffText.setText("");
                                          mDistancetoPass.setText("");

                                      }

        });


        mPickupText.setOnItemClickListener(mPickupTextClickListener);
        mDropOffText.setOnItemClickListener(mDropOffTextClickListener);

        mPickUpDetailsText = findViewById(R.id.pickup_details);
        mPickUpAttribution = findViewById(R.id.pickup_attribution);

        mDropOffDetailsText = findViewById(R.id.dropoff_details);
        mDropOffAttribution = findViewById(R.id.dropoff_attribution);


        // Set up the adapter that will retrieve suggestions from the Places Geo Data Client.
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("PK")
                .build();

        mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_GREATER_SYDNEY, typeFilter);
        mPickupText.setAdapter(mAdapter);
        mDropOffText.setAdapter(mAdapter);

        //Auto Complete Google End

        // *********************Location Update ************************************

        // Locate the UI widgets.
        /*mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);*/
        /*mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);*/
        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text);

        // Set labels.
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();


        //**************************************Location update End ****************************8
        mDistancetoPass.setText("");
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


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (sMapFragment.isAdded())
            sFm.beginTransaction().hide(sMapFragment).commit();

        if (id == R.id.Home) {
            mHeader.setVisibility(View.VISIBLE);
            mFooter.setVisibility(View.VISIBLE);
            if (id == R.id.Home) {
                if (!sMapFragment.isAdded())
                    sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
                else
                    sFm.beginTransaction().show(sMapFragment).commit();
            }
        } else if (id == R.id.Profile) {
            mHeader.setVisibility(GONE);
            mFooter.setVisibility(GONE);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new ProfileFragment());
            ft.commit();
        } else if (id == R.id.History) {
            mHeader.setVisibility(GONE);
            mFooter.setVisibility(GONE);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new HistoryFragment());
            ft.commit();
        } else if (id == R.id.wallet) {
            mHeader.setVisibility(GONE);
            mFooter.setVisibility(GONE);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new WalletFragment());
            ft.commit();
        } else if (id == R.id.cargo_calculator) {
            mHeader.setVisibility(GONE);
            mFooter.setVisibility(GONE);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new CargoCalculator());
            ft.commit();
        } else if (id == R.id.action_settings) {
            mHeader.setVisibility(GONE);
            mFooter.setVisibility(GONE);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new SettingsFragment());
            ft.commit();
        } else if (id == R.id.logout) {
            mAuth.signOut();
        } else if (id == R.id.get_help) {
            mHeader.setVisibility(GONE);
            mFooter.setVisibility(GONE);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
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
        startLocationUpdates();


    }

    @Override
    protected void onDestroy() {
        mAuth.removeAuthStateListener(firebaseAuthListener);
        stopLocationUpdates();
        super.onDestroy();

    }



    @Override
    protected void onStop() {
        mAuth.removeAuthStateListener(firebaseAuthListener);
        stopLocationUpdates();
        super.onStop();

    }

    private void getUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            mNameField.setText(name);
            String email = user.getEmail();
            mEmail.setText(email);
            if (user.getPhotoUrl() != null) {
                String photodp = user.getPhotoUrl().toString();
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View hView = navigationView.getHeaderView(0);
                navigationView.setNavigationItemSelectedListener(this);
                Picasso.with(hView.getContext()).load(photodp).resize(150, 150).centerCrop().into(mDisplayPic);
            }
        }

    }




    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        enableMyLocation();
        setOnMyLocationButtonClick();
        setOnMyLocationClick();
        mHeader.setVisibility(View.VISIBLE);
        mFooter.setVisibility(View.VISIBLE);

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

    /*@Override
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
*/
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
        @SuppressLint("RestrictedApi")
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();
                // Get the Place object from the buffer.
                @SuppressLint("RestrictedApi") final Place place = places.get(0);
                // Format details of the place for display and show it in a TextView.
                mPickUpDetailsText.setText(formatPickUpDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));

                mPickUpLatLng = place.getLatLng();
                CameraUpdate mCameraCL = CameraUpdateFactory.newLatLngZoom(mPickUpLatLng, 18);
                // mMap.moveCamera(mCameraCL);
                mMap.animateCamera(mCameraCL);
                if(mPickupMarker != null){
                    mPickupMarker.remove();
                }
                mPickupMarker = mMap.addMarker(new MarkerOptions().position(mPickUpLatLng)
                        .title(place.getName().toString()).draggable(true));
                // Display the third party attributions if set.
                final CharSequence thirdPartyAttribution = places.getAttributions();
                if (thirdPartyAttribution == null) {
                    mPickUpAttribution.setVisibility(GONE);
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
        @SuppressLint("RestrictedApi")
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();
                // Get the Place object from the buffer.
                @SuppressLint("RestrictedApi") final Place place = places.get(0);
                // Format details of the place for display and show it in a TextView.
                mDropOffDetailsText.setText(formatDropOffDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));
                mDropLatLng = place.getLatLng();
                mDropName= place.getName().toString();
                if(mDropMarker != null){
                    mDropMarker.remove();
                }
                mDropMarker = mMap.addMarker(new MarkerOptions().position(mDropLatLng)
                        .title(place.getName().toString()).draggable(true));
                mdistanceinKM=distanceInKM();
                mDistancetoPass.setText(mdistanceinKM);
                Toast.makeText(MainActivity.this, "Distance in KM " + mdistanceinKM, Toast.LENGTH_SHORT).show();
                // Display the third party attributions if set.
                final CharSequence thirdPartyAttribution = places.getAttributions();
                if (thirdPartyAttribution == null) {
                    mDropOffAttribution.setVisibility(GONE);
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

    // ************************Location Update Code ********************************8

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();

            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAGLoc, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAGLoc, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        updateUI();
                        break;
                }
                break;
        }
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
   /* public void startUpdatesButtonHandler(View view) {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            setButtonsEnabledState();
            startLocationUpdates();
        }
    }*/

    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */
   /* public void stopUpdatesButtonHandler(View view) {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        stopLocationUpdates();
    }*/

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAGLoc, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateUI();


                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAGLoc, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(com.zaylabs.truckitzaylabsv1.MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAGLoc, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAGLoc, errorMessage);
                                Toast.makeText(com.zaylabs.truckitzaylabsv1.MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        updateUI();
                    }
                });
    }


    /**
     * Updates all UI fields.
     */
    private void updateUI() {
        setButtonsEnabledState();
        updateLocationUI();
    }

    /**
     * Disables both buttons when functionality is disabled due to insuffucient location settings.
     * Otherwise ensures that only one button is enabled at any time. The Start Updates button is
     * enabled if the user is not requesting location updates. The Stop Updates button is enabled
     * if the user is requesting location updates.
     */
    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
           /* mStartUpdatesButton.setEnabled(false);*/
           /* mStopUpdatesButton.setEnabled(true);*/
        } else {
            /*mStartUpdatesButton.setEnabled(true);*/
            /*mStopUpdatesButton.setEnabled(false);*/
        }
    }

    /**
     * Sets the value of the UI fields for the location latitude, longitude and last update time.
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            mLatitudeTextView.setText(String.format(Locale.ENGLISH, "%f",
                    mCurrentLocation.getLatitude()));
            /*mLatitudeTextView.setText(String.format(Locale.ENGLISH, "%s: %f", mLatitudeLabel,
                    mCurrentLocation.getLatitude()));*/
            /*mLongitudeTextView.setText(String.format(Locale.ENGLISH, "%s: %f", mLongitudeLabel,
                    mCurrentLocation.getLongitude()));*/
            mLongitudeTextView.setText(String.format(Locale.ENGLISH, "%f",
                    mCurrentLocation.getLongitude()));
            mLastUpdateTimeTextView.setText(String.format(Locale.ENGLISH, "%s: %s",
                    mLastUpdateTimeLabel, mLastUpdateTime));

/*
            */
/*if(mNow != null){
                mNow.remove();
            }

            // Getting latitude of the current location
            double latitude = mCurrentLocation.getLatitude();

            // Getting longitude of the current location
            double longitude = mCurrentLocation.getLongitude();

            // Creating a LatLng object for the current location
            LatLng mlatLngCL = new LatLng(latitude, longitude);
            mNow = mMap.addMarker(new MarkerOptions().position(mlatLngCL));
            // Showing the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mlatLngCL));

            //Changing Marker Icon
            mNow.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.markercicon));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
*/


            saveLocation();

        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAGLoc, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                        setButtonsEnabledState();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
            updateUI();
        } else if (!checkPermissions()) {
            requestPermissions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove location updates to save battery.
        stopLocationUpdates();

    }


    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAGLoc, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(com.zaylabs.truckitzaylabsv1.MainActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAGLoc, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(com.zaylabs.truckitzaylabsv1.MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAGLoc, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAGLoc, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAGLoc, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void saveLocation() {


    }

    private void setCurrentLocation() {


    }


    private String distanceInKM() {


        Location loc1 = new Location("");
        loc1.setLatitude(mPickUpLatLng.latitude);
        loc1.setLongitude(mPickUpLatLng.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(mDropLatLng.latitude);
        loc2.setLongitude(mDropLatLng.longitude);

        float distance = loc1.distanceTo(loc2)/1000;
            /*Double mpickuplongitude = mPickUpLatLng.longitude;
            Double mpickuplatitude= mPickUpLatLng.latitude;
            Double mdroplongitude= mDropLatLng.longitude;
            Double mdroplatitude=mDropLatLng.latitude;
            Location mDistance = new Location("ServiceProvider");
            float[] results =  new float[1];
            mDistance.distanceBetween(mpickuplatitude,mdroplatitude,mpickuplongitude,mdroplongitude,results);

            return results[0];*/
        // }
        return String.valueOf(distance);
    }
    public void setDrawerState(boolean isEnabled) {
        if ( isEnabled ) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();

        }
        else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();
        }
    }



}

