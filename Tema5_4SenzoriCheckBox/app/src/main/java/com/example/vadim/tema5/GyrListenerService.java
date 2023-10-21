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

public class GyrListenerService extends Service implements SensorEventListener {

    BazaDeDate bazaDeDate;

    private SensorManager managerGyroscope;
    private Sensor gyroscope;
    private float[] gyroscopeData = {0.0f, 0.0f, 0.0f };

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
            startGyroscope();
        }
        else {
            light = false;
            stopGyroscope();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void startGyroscope(){
        managerGyroscope = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        gyroscope = managerGyroscope.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        managerGyroscope.registerListener( this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopGyroscope(){
        managerGyroscope.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            gyroscopeData[0] = Float.parseFloat(String.format("%.2f", event.values[0]));
            gyroscopeData[1] = Float.parseFloat(String.format("%.2f", event.values[1]));
            gyroscopeData[2] = Float.parseFloat(String.format("%.2f", event.values[2]));
            Log.d("testare", "gir= " + gyroscopeData[0] + " "+ gyroscopeData[1]+ " "+ gyroscopeData[2]);
            bazaDeDate.insertData("Giroscop", gyroscopeData[0], gyroscopeData[1], gyroscopeData[2]);
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
