package com.example.vadim.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class MyService extends Service implements SensorEventListener {

    static final String TABLE_NAME = "GpsData";
    static final String ROW_ID = "_id";
    static final String ROW_LAT = "lat";
    static final String ROW_LON = "lon";
    static final String ROW_ACC_X = "x";
    static final String ROW_ACC_Y = "y";
    static final String ROW_ACC_Z = "z";
    static final String ROW_TIMESTAMP = "timeStamp";
    static final String ROW_SPEED = "speed";

    private static final String TAG = "TESTGPS";

    private LocationManager mLocationManager;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 1.0f;

    private SQLiteDatabase db;

    private SensorManager sensorManager;
    private Sensor sensorAccelerometru;
    private float[] sensorData = {0.0f, 0.0f, 0.0f};

    BufferedReader bufferedReader;
    InputStream inputStreamFile;

    private boolean sendCurrentLocation = false;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra("getDataFrom");

            PackageManager pm = context.getPackageManager();

            if (value.equals("dateDinTabel")) {
                sendCurrentLocation = false;
                Cursor cursor = db.query(DataBaseHelper.TABLE_NAME, null, null,
                        null, null, null, null);

                while (cursor.moveToNext()) {
                    Log.d("TESTGPS", "s-a transmis din sql" + cursor.getDouble(2));
                    sendBroadcast(new Intent("dateTransmise").putExtra("linieDinTabel",
                            cursor.getDouble(1) + ";" + cursor.getDouble(2) + ";" +
                                    cursor.getDouble(3) + ";" + cursor.getDouble(4) + ";" +
                                    cursor.getDouble(5) + ";" + cursor.getString(6) + ";" +
                                    cursor.getDouble(7)));
                }
            }

            if(value.equals("dateDinFisier")){
                sendCurrentLocation = false;
                try{
                    inputStreamFile = getAssets().open("date.txt");
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStreamFile));

                    String line;
                    while((line = bufferedReader.readLine())!=null){
                        Log.d("TESTGPS", "s-a transmis din fisie " + line);
                        sendBroadcast(new Intent("dateTransmise").putExtra("linieDinFisier",line));
                    }

                    inputStreamFile.close();
                    bufferedReader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


            if(value.equals("locatiaCurenta")){
                Log.d(TAG, "sendCurrentLocation=true");
                sendCurrentLocation = true;
            }

            if(value.equals("buttonMaxSpeed")){
                Cursor crs = db.rawQuery("SELECT MAX("+ROW_SPEED+") FROM "+TABLE_NAME, null);
                if(crs != null){
                    crs.moveToNext();
                    double test = crs.getDouble(0);
                    //test = crs.getDouble(1);
                    try {
                        sendBroadcast(new Intent("dateTransmise").putExtra("vitezaMaxima", test + ""));
                    }catch(Exception e){
                        Log.d("TESTGPS", e.toString());
                    }
                }
            }

        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            sensorData[0] = Float.parseFloat(String.format("%.2f", event.values[0]));
            sensorData[1] = Float.parseFloat(String.format("%.2f", event.values[1]));
            sensorData[2] = Float.parseFloat(String.format("%.2f", event.values[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    Location prevLocation;
    long     prevTimeSeconds;

    private class LocationListener implements android.location.LocationListener{

        Location mLastLocation;

        public LocationListener(String provider){
            mLastLocation = new Location(provider);
        }

        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());
            long timeSeconds = System.currentTimeMillis()/1000;

            double currentSpeed;

            if(prevLocation == null){
                currentSpeed = 0.0f;
                prevLocation = location;
                prevTimeSeconds = timeSeconds;
            }else{
                double distanceKms = location.distanceTo(prevLocation)/1000.0f;
                currentSpeed = distanceKms/((timeSeconds-prevTimeSeconds)/3600.0f);
                prevLocation = location;
                prevTimeSeconds = timeSeconds;
            }

            currentSpeed = Double.parseDouble(String.format("%.2f", currentSpeed));

            if(currentSpeed==Double.POSITIVE_INFINITY) currentSpeed=0.0f;

            ContentValues cv = new ContentValues();

            cv.put(ROW_LAT, location.getLatitude());
            cv.put(ROW_LON, location.getLongitude());
            cv.put(ROW_ACC_X, sensorData[0]);
            cv.put(ROW_ACC_Y, sensorData[1]);
            cv.put(ROW_ACC_Z, sensorData[2]);
            cv.put(ROW_TIMESTAMP, timeStamp);
            cv.put(ROW_SPEED, currentSpeed);

            db.insert(TABLE_NAME, null, cv);
            Log.d(TAG, "s-au introdus date in bd " + location.getLatitude() + " " + location.getLongitude());

            if(sendCurrentLocation){
                sendBroadcast(new Intent("dateTransmise").putExtra("locatieCurenta", location.getLatitude()+";"+location.getLongitude()+";"+currentSpeed));
            }
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
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "s-a activat serviciu pentru citire gps");

        if(sendCurrentLocation==false)
            Log.d(TAG, "sendCurrentLocation=false");

        //resetam pozitia daca s-a restartat serviciul
        Location prevLocation = null;

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometru = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometru, SensorManager.SENSOR_DELAY_NORMAL);

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void initializeLocationManager(){
        if(mLocationManager == null)
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onCreate() {

        registerReceiver(broadcastReceiver, new IntentFilter("requestLaService"));

        db = (new DataBaseHelper(getApplicationContext())).getWritableDatabase();

        initializeLocationManager();

        try{
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        }catch(SecurityException ex){
            Log.i(TAG, "fail to request location update", ex);
        }catch(IllegalArgumentException ex){
            Log.d(TAG, "gps provider does not exist" + ex.getMessage());
        }

        try{
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    LOCATION_INTERVAL,LOCATION_DISTANCE,
                    mLocationListeners[1]
            );
        }catch(SecurityException ex){
            Log.i(TAG, "fail to request location update", ex);
        }catch(IllegalArgumentException ex){
            Log.d(TAG, "network provider does not exist" + ex.getMessage());
        }

        new MessageSender().execute("parametru");

    }

    @Override
    public void onDestroy() {

        unregisterReceiver(broadcastReceiver);

        Log.d(TAG, "s-a dezactivat service");

        super.onDestroy();
        if(mLocationManager!=null){
            try {
                mLocationManager.removeUpdates(mLocationListeners[0]);
                mLocationManager.removeUpdates(mLocationListeners[1]);
            }catch(Exception e){
                Log.i(TAG, "fail to remove location listeners");
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public class MessageSender extends AsyncTask<String,Void,Void> {
        Socket sock;
        DataOutputStream dos;
        PrintWriter pw;
        private String message;
        private String[] voids;

        @Override
        protected Void doInBackground(String... voids) {
            this.voids = voids;
            String[] message = this.voids;

            try {
                sock = new Socket("192.168.94.5", 9999);
                pw = new PrintWriter(sock.getOutputStream());
                pw.write(message[0]);
                pw.flush();
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
