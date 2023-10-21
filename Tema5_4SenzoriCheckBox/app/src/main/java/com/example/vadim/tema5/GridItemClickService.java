package com.example.vadim.tema5;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GridItemClickService extends Service implements SensorEventListener {

    BazaDeDate bazaDeDate;

    private SensorManager managerAccelerometru;
    private SensorManager managerGiroscop;
    private SensorManager managerLumina;
    private Sensor accelerometru;
    private Sensor giroscop;
    private Sensor lumina;
    private float[] accelerometruData = {0.0f, 0.0f, 0.0f };
    private float[] giroscopData = {0.0f, 0.0f, 0.0f };
    private float luminaData;

    boolean but0=false, but1=false, but2=false, but3=false;

    @Override
    public void onCreate() {
        bazaDeDate = new BazaDeDate(this);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String sensorName = intent.getStringExtra("sensDenum");

        switch (sensorName){
            case "Accelerometru":
                if(!but0) {
                    but0 = true;
                    startAccelerometru();
                }
                else {
                    but0 = false;
                    stopAccelerometru();
                }
                break;
            case "Giroscop":
                if(!but1) {
                    but1 = true;
                    startGiroscop();
                }
                else {
                    but1 = false;
                    stopGiroscop();
                }
                break;
            case "00":
                if(!but2) {

                }
                else {

                }
                break;
            case "Lumina":
                if(!but3) {
                    but3 = true;
                    startLumina();
                }
                else {
                    but3 = false;
                    stopLumina();
                }
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void startAccelerometru(){
        managerAccelerometru = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometru = managerAccelerometru.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        managerAccelerometru.registerListener(this, accelerometru, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startGiroscop(){
        managerGiroscop = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        giroscop = managerGiroscop.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        managerGiroscop.registerListener(this, giroscop, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startLumina(){
        managerLumina = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        lumina = managerLumina.getDefaultSensor(Sensor.TYPE_LIGHT);
        managerLumina.registerListener(this, lumina, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopLumina(){
        try {
            managerLumina.unregisterListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopAccelerometru(){
        try {
            managerAccelerometru.unregisterListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopGiroscop(){
        try {
            managerGiroscop.unregisterListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            accelerometruData[0] = Float.parseFloat(String.format("%.2f", event.values[0]));
            accelerometruData[1] = Float.parseFloat(String.format("%.2f", event.values[1]));
            accelerometruData[2] = Float.parseFloat(String.format("%.2f", event.values[2]));
            Log.d("testare", "acc= " + accelerometruData[0] + " "+ accelerometruData[1]+ " "+ accelerometruData[2]);
            bazaDeDate.insertData("Accelerometru", accelerometruData[0], accelerometruData[1], accelerometruData[2]);
        }

        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            giroscopData[0] = Float.parseFloat(String.format("%.2f", event.values[0]));
            giroscopData[1] = Float.parseFloat(String.format("%.2f", event.values[1]));
            giroscopData[2] = Float.parseFloat(String.format("%.2f", event.values[2]));
            Log.d("testare", "gir= " + giroscopData[0] + " "+ giroscopData[1]+ " "+ giroscopData[2]);
        }

        if(event.sensor.getType()==Sensor.TYPE_LIGHT){
            luminaData = Float.parseFloat(String.format("%.2f", event.values[0]));
            Log.d("testare", "lum= " + luminaData);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
