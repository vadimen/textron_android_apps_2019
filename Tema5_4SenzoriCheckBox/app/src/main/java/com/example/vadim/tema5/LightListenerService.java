package com.example.vadim.tema5;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class LightListenerService extends Service implements SensorEventListener{

    BazaDeDate bazaDeDate;

    private SensorManager managerLumina;
    private Sensor lumina;
    private float luminaData;

    boolean light = false;

    @Override
    public void onCreate() {
        bazaDeDate = new BazaDeDate(this);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!light) {
            light = true;
            startLumina();
        }
        else {
            light = false;
            stopLumina();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void startLumina(){
        managerLumina = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        lumina = managerLumina.getDefaultSensor(Sensor.TYPE_LIGHT);
        managerLumina.registerListener( this, lumina, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopLumina(){
        managerLumina.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==Sensor.TYPE_LIGHT){
            luminaData = Float.parseFloat(String.format("%.2f", event.values[0]));
            Log.d("testare", "lum= " + luminaData);
            bazaDeDate.insertData("Lumina", luminaData, 0, 0);
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
