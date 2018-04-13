package com.zaylabs.truckitzaylabsv1.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaylabs.truckitzaylabsv1.DTO.customerHistory;
import com.zaylabs.truckitzaylabsv1.R;
import com.zaylabs.truckitzaylabsv1.RecylerViewAdapter.driverHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private FirebaseFirestore firestoreDB;
    private RecyclerView mDhistory;

    private List<customerHistory> dHistory;
    private customerHistory driverHistory;
    private driverHistoryAdapter driverHistoryAdapter;
    private String driverID;
    private String TAG;
    private FirebaseAuth mAuth;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        firestoreDB = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        driverID = mAuth.getCurrentUser().getUid();



        dHistory = new ArrayList<>();
        driverHistoryAdapter = new driverHistoryAdapter(getActivity(),dHistory);
        mDhistory = (RecyclerView)view.findViewById(R.id.driverHistoryRV);
        mDhistory.setHasFixedSize(true);
        mDhistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDhistory.setAdapter(driverHistoryAdapter);

        // firestoreDB.collection("DriverHistory").whereEqualTo("driverid", driverID).
        firestoreDB.collection("DriverHistory").whereEqualTo("cid", driverID).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Error:" + e.getMessage());
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (doc.getType()) {
                        case ADDED:
                            driverHistory = doc.getDocument().toObject(customerHistory.class);
                            dHistory.add(driverHistory);
                            driverHistoryAdapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            driverHistory = doc.getDocument().toObject(customerHistory.class);
                            dHistory.add(driverHistory);
                            driverHistoryAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            dHistory.remove(driverHistory);
                            driverHistoryAdapter.notifyDataSetChanged();
                            break;
                    }

                }
            }
        });

        return view;
    }

}
