package com.zaylabs.truckitzaylabsv1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.FragmentTransitionSupport;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zaylabs.truckitzaylabsv1.fragment.CargoCalculator;
import com.zaylabs.truckitzaylabsv1.fragment.HelpFragment;
import com.zaylabs.truckitzaylabsv1.fragment.HistoryFragment;
import com.zaylabs.truckitzaylabsv1.fragment.ProfileFragment;
import com.zaylabs.truckitzaylabsv1.fragment.SettingsFragment;
import com.zaylabs.truckitzaylabsv1.fragment.WalletFragment;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;


    SupportMapFragment sMapFragment;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference mfirebaseDB;

    private TextView mNameField, mEmail, mTextViewDP;

    private ImageView mDisplayPic;

    private String userID;
    private Location mlat;
    private Location mlng;
    private Location mlocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sMapFragment = SupportMapFragment.newInstance();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();





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

        userID = mAuth.getCurrentUser().getUid();
        mfirebaseDB = FirebaseDatabase.getInstance().getReference().child("Users").child("customer").child(userID);


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
        View hView =  navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);
        mTextViewDP =hView.findViewById(R.id.TextViewDP);

        mEmail = hView.findViewById(R.id.TextViewUserEmail);
        mNameField=hView.findViewById(R.id.TextViewUser);
        mDisplayPic =hView.findViewById(R.id.displaypic);

    navigationView.getMenu().getItem(0).setCheckable(true);
        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        if (!sMapFragment.isAdded()){
            sFm.beginTransaction().add(R.id.map,sMapFragment).commit();}
        else{
            sFm.beginTransaction().show(sMapFragment).commit();}


        sMapFragment.getMapAsync(this);

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
            if (id == R.id.Home) {
                if (!sMapFragment.isAdded())
                    sFm.beginTransaction().add(R.id.map,sMapFragment).commit();
                else
                    sFm.beginTransaction().show(sMapFragment).commit();}
        } else if (id == R.id.Profile) {
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new ProfileFragment());
            ft.commit();
        } else if (id == R.id.History) {
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new HistoryFragment());
            ft.commit();
        } else if (id == R.id.wallet) {
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new WalletFragment());
            ft.commit();
        } else if (id == R.id.cargo_calculator) {
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new CargoCalculator());
            ft.commit();
        } else if (id == R.id.action_settings) {
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.cm, new SettingsFragment());
            ft.commit();
        } else if (id == R.id.logout) {
            mAuth.signOut();
        } else if (id == R.id.get_help) {
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

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        enableMyLocation();
        setOnMyLocationButtonClick();
        setOnMyLocationClick();
    }



    private void setOnMyLocationClick() {
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {

                Toast.makeText(com.zaylabs.truckitzaylabsv1.MainActivity.this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
                mlocation = location;

            }
        });
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
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
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


}
