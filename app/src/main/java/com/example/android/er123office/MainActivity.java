package com.example.android.er123office;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnPendingRequest, OnDriverSent,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker patientMarker;
    private Marker ambulanceMarker;
    private CustomAdapter horizontalAdapter;
    private ArrayList<Driver> allDrivers;
    Button sendRequest;
    RecyclerView recyclerView;
    private PendingRequests targetLocation;
    private int c;
    BitmapDescriptor patient;
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendRequest = (Button) findViewById(R.id.send_request_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Driver> selectedDrivers= new ArrayList<>();
                c=0;
                for (Driver model : allDrivers) {
                    if (model.isSelected()) {
                        selectedDrivers.add(model);
                        c++;
                    }
                }
                FirebaseHandler.sentRequestsToDrivers(selectedDrivers,targetLocation);
                Toast.makeText(MainActivity.this, "number = "+c, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.7644653,35.2905022),5));
        FirebaseHandler.getAllPendingRequests(this,this);
        FirebaseHandler.getAllDrivers(this,this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng arg0){
                recyclerView.setVisibility(View.GONE);
                sendRequest.setVisibility(View.GONE);
                for(int i=0;i<allDrivers.size();i++){
                    allDrivers.get(i).setSelected(false);
                }
            }
        });
    }

    @Override
    public void getPendingRequests(ArrayList<PendingRequests> arrayList) {
        if(arrayList == null){
            return;
        }
        patient = BitmapDescriptorFactory.fromResource(R.drawable.patient);
        for(PendingRequests requests:arrayList){
            double lat, lng;
            lat = Double.parseDouble(requests.getLatPosition());
            lng = Double.parseDouble(requests.getLongPosition());
            patientMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Injuries: "+ requests.getNumberOfInjuries()).snippet(requests.getLatPosition()+"||"+requests.getLongPosition()).icon(patient));
            patientMarker.setTag(requests);
        }
    }

    @Override
    public void getDriver(ArrayList<Driver> arrayList) {
        allDrivers = arrayList;
        if(arrayList == null){
            return;
        }
        BitmapDescriptor patient = BitmapDescriptorFactory.fromResource(R.drawable.goodambulance);
        for(Driver driver:arrayList){
            double lat, lng;
            lat = Double.parseDouble(driver.getLatPosition());
            lng = Double.parseDouble(driver.getLongPosition());
            ambulanceMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Plate: "+ driver.getPlateChars() + " " + driver.getPlateNums())
                    .icon(patient));
        }
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        String markerType = marker.getTitle().substring(0, 8);
        if (markerType.equals("Injuries")) {
            double lat = Double.parseDouble(marker.getSnippet().substring(0, marker.getSnippet().indexOf("||")));
            double lon = Double.parseDouble(marker.getSnippet().substring(marker.getSnippet().lastIndexOf("||") + 2, marker.getSnippet().length()));
            assignAmbulance(lat, lon,(PendingRequests)marker.getTag());
        }
        return false;
    }
    public void assignAmbulance(double patiantLat, double patientLon,PendingRequests location){
        recyclerView.setVisibility(View.VISIBLE);
        sendRequest.setVisibility(View.VISIBLE);
        targetLocation =location;
        for(int i=0;i<allDrivers.size();i++){
            if(allDrivers.get(i).getDriverAvailable().equals("true")) {
                allDrivers.get(i).setDistance(calculateDistance(patiantLat, patientLon, Double.parseDouble(allDrivers.get(i).getLatPosition()), Double.parseDouble(allDrivers.get(i).getLongPosition())));
            }
        }
        Collections.sort(allDrivers, new Driver().DISTANCE);
        horizontalAdapter = new CustomAdapter(allDrivers,this);
        recyclerView.setAdapter(horizontalAdapter);

    }

    public float calculateDistance(double userLat, double userLng, double venueLat, double venueLng) {
        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);
        double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
                (Math.cos(Math.toRadians(userLat))) *
                        (Math.cos(Math.toRadians(venueLat))) *
                        (Math.sin(lngDistance / 2)) *
                        (Math.sin(lngDistance / 2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (float) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));
    }
}
