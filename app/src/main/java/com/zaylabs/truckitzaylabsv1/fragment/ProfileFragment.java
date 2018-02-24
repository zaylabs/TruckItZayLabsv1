package com.zaylabs.truckitzaylabsv1.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zaylabs.truckitzaylabsv1.R;
import com.zaylabs.truckitzaylabsv1.models.User;
import com.zaylabs.truckitzaylabsv1.models.UserProfile;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.auth.FirebaseAuth.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ProfileFragment";
    private DatabaseReference mDatabase;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private EditText mName;
    private EditText mPhone;
    private Button mSave;
    private TextView mImageLink;
    private ImageView mUserDP;
    private String mPostKey;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mDBRef;
    private FirebaseAuth mAuth;
    private String userID;


    public ProfileFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        // Get post key from intent

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userID = mAuth.getCurrentUser().getUid();
        mDBRef = mDatabase.child("users").child("customer").child(userID);
        mName = view.findViewById(R.id.field_name);
        mPhone = view.findViewById(R.id.field_phone);
        mImageLink = view.findViewById(R.id.imagelink);
        mUserDP = view.findViewById(R.id.userDP);
        mSave = view.findViewById(R.id.button_update);

        mUserDP.setOnClickListener(this);
        mSave.setOnClickListener(this);
        getInfo();
        // Inflate the layout for this fragment
        return view;
    }

    private void chooseImage() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mUserDP.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }

    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        mDBRef.child("imageURL").setValue(imageEncoded);
    }

    private void getInfo(){
        mDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)  {
                String fullname = dataSnapshot.child("fullname").getValue().toString();
                mName.setText(fullname);
                String phone = dataSnapshot.child("phone").getValue().toString();
                mPhone.setText(phone);
            }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void saveMap(){
        Map<String, Object> userUpdates = new HashMap<>();
        final String fullname = mName.getText().toString();
        userUpdates.put("fullname", fullname);
        final String phone = mPhone.getText().toString();
        userUpdates.put("phone", phone);
        mDBRef.updateChildren(userUpdates);
    }
    private void saveInfo() {
            saveMap();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.userDP) {
            chooseImage();
        } else if (i == R.id.button_update) {
            saveInfo();
        }
    }


}
