package com.example.vadim.proiect_final_map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Cursor cursor;

    private SQLiteDatabase db;

    TextView latLongText, speedText;
    Location locatiaPredefinita = null;
    boolean apropiereSemnalizata = false;
    Marker markerLocPredef = null;

    Polyline line = null;
    PolylineOptions options;
    ArrayList<LatLng> storedLocationsGPS = new ArrayList<LatLng>();
    ArrayList<LatLng> storedLocationsSIM = new ArrayList<LatLng>();
    boolean trailEn = false;
    boolean simEn = false;

    LatLng ultimaLocatie;

    int ZOOM_CAMERA = 22;
    int opacity = 1;//1 => alpha 0.6

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        registerReceiver(broadcastReceiver, new IntentFilter("dateTransmise"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value, recData[];
            LatLng recLatLong = null;

            value = intent.getStringExtra("linieDinTabel");
            if(value!=null)
                Log.d("DatePrimiteDinSql", value);

            value = intent.getStringExtra("linieDinFisier");
            if(value!=null) {
                Log.d("DatePrimiteDinFisier", value);
                recData = value.split(";");
                recLatLong = new LatLng(Double.parseDouble(recData[0]), Double.parseDouble(recData[1]));

                ultimaLocatie = recLatLong;
                storedLocationsSIM.add(recLatLong);

                mMap.addMarker(new MarkerOptions().position(recLatLong));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(recLatLong));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_CAMERA));
            }

            value = intent.getStringExtra("vitezaMaxima");
            if(value!=null) {
                Toast.makeText(getApplicationContext(),
                        "viteza maxima e "+value+" km/h", Toast.LENGTH_LONG).show();

            }

            value = intent.getStringExtra("locatieCurenta");
            if(value!=null) {
                Log.d("Locatie curenta primita", value);
                recData = value.split(";");
                latLongText.setText(recData[0] + "\n"+recData[1]);
                speedText.setText(recData[2]+" km/h");

                recLatLong = new LatLng(Double.parseDouble(recData[0]), Double.parseDouble(recData[1]));
                mMap.addMarker(new MarkerOptions().position(recLatLong));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(recLatLong));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_CAMERA));

                ultimaLocatie = recLatLong;

                storedLocationsGPS.add(recLatLong);

                if(locatiaPredefinita!=null){
                    Location locActuala = new Location("");
                    locActuala.setLatitude(Double.parseDouble(recData[0]));
                    locActuala.setLongitude(Double.parseDouble(recData[1]));

                    if(locActuala.distanceTo(locatiaPredefinita)<=500 && !apropiereSemnalizata) {
                        apropiereSemnalizata = true;
                        Toast.makeText(getApplicationContext(),
                                "Ne apropiem de locatie predefinita.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            if(trailEn && recLatLong!=null) {
                options.add(recLatLong);
                line.remove();
                line = mMap.addPolyline(options);
            }
        }
    };


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

        latLongText = findViewById(R.id.latLong);
        speedText = findViewById(R.id.speedText);

        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                locatiaPredefinita = new Location("");
                locatiaPredefinita.setLatitude(latLng.latitude);
                locatiaPredefinita.setLongitude(latLng.longitude);

                if(markerLocPredef==null)
                    markerLocPredef = mMap.addMarker(new MarkerOptions().position(latLng).title("Locatia predefinita").icon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    ));
                else
                    markerLocPredef.setPosition(latLng);

                apropiereSemnalizata = false;
            }
        });

        // Add a marker in Chisinau and move the camera
        LatLng chisinau = new LatLng(47.0105, 28.8638);
        mMap.addMarker(new MarkerOptions().position(chisinau).title("Marker in Chisinau"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(chisinau));


        latLongText.setText(chisinau.latitude + "\n"+chisinau.longitude);

        //sendBroadcast(new Intent("requestLaService").putExtra("getDataFrom", "dateDinTabel"));
        //sendBroadcast(new Intent("requestLaService").putExtra("getDataFrom", "dateDinFisier"));
        sendBroadcast(new Intent("requestLaService").putExtra("getDataFrom", "locatiaCurenta"));
    }

    public void buttonAnimateCamera(View view){
        if(ultimaLocatie!=null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ultimaLocatie));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_CAMERA));
        }
    }

    public void buttonMaxSpeed(){
        sendBroadcast(new Intent("requestLaService").putExtra("getDataFrom", "buttonMaxSpeed"));
    }

    //============menu staff
    private PopupMenu popupMenu, popupMenuPref;
    View mainView;

    public void onClickMenu(View anchor) {
        mainView = anchor;
        // TODO Auto-generated method stub
        if(popupMenu==null) {
            popupMenu = new PopupMenu(getApplicationContext(), anchor);
            popupMenu.setOnDismissListener(new OnDismissListener());
            popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener());
            popupMenu.inflate(R.menu.popup_menu);
        }

        popupMenu.show();
    }

    public void onClickPrefMenu(View anchor) {
        // TODO Auto-generated method stub
        if(popupMenuPref==null) {
            popupMenuPref = new PopupMenu(getApplicationContext(), anchor);
            popupMenuPref.setOnDismissListener(new OnDismissListener());
            popupMenuPref.setOnMenuItemClickListener(new OnPrefMenuItemClickListener());
            popupMenuPref.inflate(R.menu.pref_popup_menu);
        }

        popupMenuPref.show();
    }

    private class OnDismissListener implements PopupMenu.OnDismissListener {

        @Override
        public void onDismiss(PopupMenu menu) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "Popup Menu is dismissed",
            //        Toast.LENGTH_SHORT).show();
        }

    }

    private class OnPrefMenuItemClickListener implements
            PopupMenu.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.opacitate:
                    changeOpacity();
                    return true;
            }
            return false;
        }
    }


    public void changeOpacity(){
        opacity++;
        if(opacity>2) opacity=0;

        double alpha = Double.POSITIVE_INFINITY;
        switch (opacity){
            case 0:alpha=0.3;break;
            case 1:alpha=0.6;break;
            case 2:alpha=1.0;break;
        }

        findViewById(R.id.leftTopCorner).setAlpha(Float.parseFloat(alpha+""));
        findViewById(R.id.rightBottomCorn).setAlpha(Float.parseFloat(alpha+""));
        findViewById(R.id.button1).setAlpha(Float.parseFloat(alpha+""));
        findViewById(R.id.button2).setAlpha(Float.parseFloat(alpha+""));
        findViewById(R.id.button3).setAlpha(Float.parseFloat(alpha+""));
    }

    private class OnMenuItemClickListener implements
            PopupMenu.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // TODO Auto-generated method stub
            switch (item.getItemId()) {
                case R.id.setTrail:
                    trailEn = !trailEn;
                    item.setTitle("Trail (" + (trailEn?"on":"off") +")");
                    drawUndrawPolyline();
                    return true;
                case R.id.setSimulation:
                    simEn = !simEn;
                    item.setTitle("Simulation (" + (simEn?"on":"off") +")");
                    if(simEn)
                        startSimulation();
                    else
                        stopSimulation();
                    return true;
                case R.id.viteza_maxima:
                    buttonMaxSpeed();
                    return true;
                case R.id.prefs:
                    onClickPrefMenu(mainView);
                    return true;
            }
            return false;
        }

        public void startSimulation(){
            mMap.clear();

            locatiaPredefPuneInapoi();

            storedLocationsSIM.clear();

            sendBroadcast(new Intent("requestLaService").putExtra("getDataFrom", "dateDinFisier"));
        }

        public void stopSimulation(){//and restart getting gps data
            mMap.clear();
            ultimaLocatie = null;

            locatiaPredefPuneInapoi();

            int i;
            if(storedLocationsGPS.size()>0) {
                for (i = 0; i <= storedLocationsGPS.size() - 1; i++)
                    mMap.addMarker(new MarkerOptions().position(storedLocationsGPS.get(i)));

                try {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(storedLocationsGPS.get(i)));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(22));
                }catch(Exception e){
                    Log.d("TESTGPS", e.getMessage());
                }
            }

            sendBroadcast(new Intent("requestLaService").putExtra("getDataFrom", "locatiaCurenta"));
        }

        public void drawUndrawPolyline(){
            if(trailEn){
                options = new PolylineOptions();

                if(!simEn) {
                    int a = 5;
                    for (int i = 0; i <= storedLocationsGPS.size()-1; i++)
                        options.add(storedLocationsGPS.get(i));
                }else{
                    int a = 5;
                    for (int i = 0; i <= storedLocationsSIM.size()-1; i++)
                        options.add(storedLocationsSIM.get(i));
                }

                options.width(5).color(Color.BLUE);

                line = mMap.addPolyline(options);
            }else{
                if(line!=null)
                  line.remove();
            }
        }
    }

    public void locatiaPredefPuneInapoi(){
        if(markerLocPredef!=null) {
            LatLng tmp = new LatLng(locatiaPredefinita.getLatitude(), locatiaPredefinita.getLongitude());
            markerLocPredef = mMap.addMarker(new MarkerOptions().position(tmp).title("Locatia predefinita").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            ));
        }
    }
}
