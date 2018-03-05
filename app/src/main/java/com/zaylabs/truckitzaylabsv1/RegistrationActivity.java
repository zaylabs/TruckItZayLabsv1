package com.zaylabs.truckitzaylabsv1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "RegistrationActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDBRef;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignUpButton;
    private EditText mPhone;
    private EditText mName;
    private String userID;
    private ImageView mImageurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        // Views
        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);
        mSignUpButton = findViewById(R.id.button_register);
        mName = findViewById(R.id.field_name);
        mPhone = findViewById(R.id.field_phone);
        mImageurl = findViewById(R.id.displaypic);
        // Click listeners
        mSignUpButton.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            finish();
        }
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateSignUpForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        //Set Display name
        userdisplayname();
        //setphonenumber
        saveMap();
        // Go to MainActivity
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateSignUpForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }
        if (TextUtils.isEmpty(mName.getText().toString())) {
            mName.setError("Required");
            result = false;
        } else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(mPhone.getText().toString())) {
            mPhone.setError("Required");
            result = false;
        } else {
            mPhone.setError(null);
        }

        return result;
    }

    private void userdisplayname() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    public void saveMap() {

        userID = mAuth.getCurrentUser().getUid();
        mDBRef = mDatabase.child("users").child("customer").child(userID);
        Map<String, Object> userUpdates = new HashMap<>();
        final String phone = mPhone.getText().toString();
        userUpdates.put("phone", phone);

        mDBRef.updateChildren(userUpdates);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_register) {
            signUp();
        }
    }
}
