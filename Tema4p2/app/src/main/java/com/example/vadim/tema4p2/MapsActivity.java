package com.example.vadim.tema4p2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //int i = intent.getIntExtra("gpsdata", 0);

           /// Log.d("CristiBorcea", i+"");

            String valueReceived[] = intent.getStringExtra("gpsdata").split(";");
//
            Log.d("DanielaEriomenca", "" + valueReceived[0]+" "+valueReceived[1]);
//
            LatLng locatieNoua = new LatLng(Double.parseDouble(valueReceived[0]), Double.parseDouble(valueReceived[1]));

            mMap.addMarker(new MarkerOptions().position(locatieNoua).title("Locatia mea"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locatieNoua, 16.0f));


        }
    };

    @Override
    protected void onStart() {
        //inregistra doar cu un anumit filtru, ca sa primim doar ce ne intereseaza cu un filtru
        //doar actiunea de MyBroadcast
        registerReceiver(broadcastReceiver, new IntentFilter("myBroadcast"));
        super.onStart();
    }

    @Override
    protected void onStop() {

        //ne asiguram ca inchidem Broadcastca sa nu mai foloseasca bateria
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}
