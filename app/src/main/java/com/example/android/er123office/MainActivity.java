package com.example.android.er123office;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.er123office.callbacks.OnDriverSent;
import com.example.android.er123office.callbacks.OnPendingRequest;
import com.example.android.er123office.data.Driver;
import com.example.android.er123office.data.PendingRequests;
import com.example.android.er123office.firebase.FirebaseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnPendingRequest, OnDriverSent{

    private GoogleMap mMap;
    private Marker patientMarker;
    private Marker ambulanceMarker;
    private HashMap<String,PendingRequests> patientsMap;
    private HashMap<String,String> ambulancesMap;
    private ArrayList<HashMap> ambulanceMapList;
    private ArrayList<HashMap> patientMapList;
    private Driver mDriver;
    private PendingRequests mPending;
    BitmapDescriptor patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ambulancesMap = new HashMap<>();
        ambulanceMapList = new ArrayList<>();
        FirebaseHandler.getAllPendingRequests(this,this);
        FirebaseHandler.getAllDrivers(this,this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.9556449,30.9134569),16.0f));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String markerType = marker.getTitle().substring(0, 5);
                Log.e("TAGKEY2", markerType);
                if (markerType.equals("Plate")) {

                    Toast.makeText(MainActivity.this, "Contains Plate", Toast.LENGTH_SHORT).show();
                } else if (markerType.equals("Injur"))
                {
                    Toast.makeText(MainActivity.this, "Contains Injuries", Toast.LENGTH_SHORT).show();
                    LatLng tempLatLan = marker.getPosition();
                    patientMarker.setVisible(false);
                    mMap.addMarker(new MarkerOptions().position(tempLatLan).icon(patient));

                }
//                LatLng markerLatLng = marker.getPosition();
//                Double markerlat = markerLatLng.latitude;
//                Double markerlng = markerLatLng.longitude;
//                for(HashMap<String,String> ambulance:ambulanceMapList)
//                {
//                    Double ambulancelat = Double.parseDouble(ambulance.get("latPosition"));
//                    Double ambulancelng = Double.parseDouble(ambulance.get("longPosition"));
//                }


//                if(markerlat.equals(ambulancelat) && markerlng.equals(ambulancelng))
//                {
//                    Toast.makeText(MainActivity.this,"True",Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(MainActivity.this,"False",Toast.LENGTH_SHORT).show();
//                }


                return true;
            }
        });


    }

    @Override
    public void getPendingRequests(ArrayList<PendingRequests> arrayList) {
        if(arrayList == null)
        {
            return;
        }
        patient = BitmapDescriptorFactory.fromResource(R.drawable.patient);
        for(PendingRequests requests:arrayList)
        {
            double lat, lng;
            lat = Double.parseDouble(requests.getLatPosition());
            lng = Double.parseDouble(requests.getLongPosition());
            patientMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Injuries: "+ requests.getNumberOfInjuries()).icon(patient));

        }

    }


    @Override
    public void getDriver(ArrayList<Driver> arrayList) {
        if(arrayList == null)
        {
            return;
        }

        BitmapDescriptor patient = BitmapDescriptorFactory.fromResource(R.drawable.goodambulance);
        for(Driver driver:arrayList)
        {
            double lat, lng;
            if(!driver.getLatPosition().equals("null") && !driver.getLongPosition().equals("null"))
            {
                lat = Double.parseDouble(driver.getLatPosition());
                lng = Double.parseDouble(driver.getLongPosition());
                ambulanceMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Plate: "+ driver.getPlateChars() + " " + driver.getPlateNums())
                        .icon(patient));
                ambulancesMap.put("driverEmail",driver.getDriverEmail());
                ambulancesMap.put("driverName",driver.getDriverName());
                ambulancesMap.put("driverPassword",driver.getDriverPassword());
                ambulancesMap.put("latPosition",driver.getLatPosition());
                ambulancesMap.put("longPosition",driver.getLongPosition());
                ambulancesMap.put("plateChars",driver.getPlateChars());
                ambulancesMap.put("plateNums",driver.getPlateNums());
                ambulancesMap.put("phoneNumber",driver.getPhoneNumber());
                ambulanceMapList.add(ambulancesMap);
            }

        }

    }

}
