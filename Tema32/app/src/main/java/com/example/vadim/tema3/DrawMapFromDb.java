package com.example.vadim.tema3;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
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

public class DrawMapFromDb extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public BazaDeDate db;
    private PolylineOptions polyline;
    public Cursor cursor;
    public Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_map_from_db);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = new BazaDeDate(this);
        polyline = new PolylineOptions();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.clear();

        cursor = db.getData();

        handler.postDelayed(drawWithPause, 0);

//        Thread thread = new Thread(){
//            public void run(){
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mMap.clear();
//
//                        Cursor cursor = db.getData();
//
//                        while(cursor.moveToNext()){
//                            LatLng position = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
//                            polyline.add(position);
//                            mMap.addPolyline(polyline);
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16.0f));
//                            try{
//                                Thread.sleep(2000);
//                            }catch(InterruptedException e){
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
//            }
//        };
//
//        thread.start();
    }

    Runnable drawWithPause = new Runnable() {
        @Override
        public void run() {
            if(cursor.moveToNext()){
                LatLng position = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
                polyline.add(position);
                mMap.addPolyline(polyline);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16.0f));
            }

            handler.postDelayed(this, 2000);
        }
    };

}
