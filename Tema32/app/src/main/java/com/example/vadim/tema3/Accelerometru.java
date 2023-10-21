package com.example.vadim.tema3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class Accelerometru extends Service implements SensorEventListener {

    private SensorManager sensManager;
    private Sensor sensorAcc;
    private float[] sensorData = {0.0f, 0.0f, 0.0f};

    private final IBinder binder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("test", "On start command");
        sensManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorAcc = sensManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);
    }

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

    public class LocalBinder extends Binder{
        Accelerometru getService(){
            return Accelerometru.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("test", "Service binded");
        return binder;
    }

    public float[] getSensorData(){
        Log.d("test", "Request Service Data");
        return sensorData;
    }
}
