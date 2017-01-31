package com.example.android.er123office.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.android.er123office.callbacks.OnDriverSent;
import com.example.android.er123office.callbacks.OnPendingRequest;
import com.example.android.er123office.data.Driver;
import com.example.android.er123office.data.PendingRequests;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Abshafi on 1/26/2017.
 */

public class FirebaseHandler {

    public static void getAllPendingRequests(final Context context, final OnPendingRequest listener)
    {
        final ArrayList<PendingRequests> arrayList = new ArrayList<>();
        final ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setMessage("Please Wait");
        mProgress.setCancelable(false);
        mProgress.show();
        FirebaseHelper.getDatabase().getReference().child("PendingRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    PendingRequests requests = snapshot.getValue(PendingRequests.class);
                    arrayList.add(requests);
                }
                listener.getPendingRequests(arrayList);
                Log.e("TAG",arrayList.size()+"");
                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void getAllDrivers(final Context context, final OnDriverSent listener)
    {
        final ArrayList<Driver> arrayList = new ArrayList<>();
        final ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setMessage("Please Wait");
        mProgress.setCancelable(false);
        mProgress.show();
        FirebaseHelper.getDatabase().getReference("allDrivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Getting Driver Location","True");
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {

                        Driver driver = snapshot.getValue(Driver.class);
                        arrayList.add(driver);

                }
                Log.e("Location Size ",arrayList.size()+"");
                listener.getDriver(arrayList);
                mProgress.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
