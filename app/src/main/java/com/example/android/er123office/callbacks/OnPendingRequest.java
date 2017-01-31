package com.example.android.er123office.callbacks;

import com.example.android.er123office.data.PendingRequests;

import java.util.ArrayList;

/**
 * Created by Abshafi on 1/26/2017.
 */

public interface OnPendingRequest {

    void getPendingRequests(ArrayList<PendingRequests> arrayList);
}
