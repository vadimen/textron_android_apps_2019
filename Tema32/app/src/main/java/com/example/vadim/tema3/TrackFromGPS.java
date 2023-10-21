package com.example.vadim.tema3;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.FileOutputStream;
import java.util.Random;

public class TrackFromGPS extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    LocationListener m_oLocationListener;
    LocationManager m_oLocationManager;
    private PolylineOptions m_oLineOptions;

    BazaDeDate db;

    public Accelerometru accService;
    public boolean isBound = false;
    public Intent intent;

    public float[] dateAccelerometru = new float[3];

    public FileOutputStream fos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        m_oLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        m_oLineOptions = new PolylineOptions().width(15).color(Color.RED);

        db = new BazaDeDate(this);
        db.dropTable();

        intent = new Intent(this, Accelerometru.class);

        if(!isBound){
            startService(intent);
            getApplicationContext().bindService(intent, servConn, Context.BIND_AUTO_CREATE);
            Log.d("test", "Service started");
        }else{
            Log.d("test", "Service could not be started");
        }

        try {
            fos = openFileOutput("date.csv", Context.MODE_PRIVATE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void updateHarta(LatLng locatieNoua){
        if(mMap == null) return;
        mMap.clear();

        mMap.addMarker(new MarkerOptions().position(locatieNoua).title("Locatia mea"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locatieNoua, 22.0f));

        m_oLineOptions.add(locatieNoua);
        mMap.addPolyline(m_oLineOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TrackFromGPS.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

        m_oLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());

                if(isBound){
                    dateAccelerometru = accService.getSensorData();
                    Log.d("test", ""+dateAccelerometru[0]+" "+dateAccelerometru[1]+" "+dateAccelerometru[2]);
                }else{
                    dateAccelerometru[0] = 0;dateAccelerometru[1] = 0;dateAccelerometru[2] = 0;
                }

                db.insertData(location.getLatitude(), location.getLongitude(), dateAccelerometru[0], dateAccelerometru[1], dateAccelerometru[2]);

                try{
                    String toWrite = (""+location.getLatitude()+";"+location.getLongitude()+";"+dateAccelerometru[0]+";"+dateAccelerometru[1]+";"+dateAccelerometru[2]);
                    fos.write(toWrite.getBytes());
                }catch(Exception e){
                    e.printStackTrace();
                }

                updateHarta(loc);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        m_oLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, m_oLocationListener);

        LatLng startPoint = new LatLng(44.8585, 24.8692);
        updateHarta(startPoint);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(isBound){
            getApplicationContext().unbindService(servConn);
            stopService(intent);
            isBound = false;
            Log.d("test", "Service stopped");
        }else{
            Log.d("test", "Service could not be started");
        }

        m_oLocationManager.removeUpdates(m_oLocationListener);

        try {
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private ServiceConnection servConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            accService = ((Accelerometru.LocalBinder) binder).getService();
            isBound = true;
            Log.d("test", "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            accService = null;
            isBound = false;
            Log.d("test", "Service disconnected");
        }
    };


}
