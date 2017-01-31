package com.example.android.er123office.firebase;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Abshafi on 1/26/2017.
 */

public class FirebaseHelper {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

}
