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

public class GravityListenerService extends Service implements SensorEventListener {

    BazaDeDate bazaDeDate;

    private SensorManager managerGravity;
    private Sensor gravity;
    private float[] gravityData = {0.0f, 0.0f, 0.0f };

    boolean grv = false;

    @Override
    public void onCreate() {
        bazaDeDate = new BazaDeDate(this);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!grv) {
            grv = true;
            startGyroscope();
        }
        else {
            grv = false;
            stopGyroscope();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void startGyroscope(){
        managerGravity = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        gravity = managerGravity.getDefaultSensor(Sensor.TYPE_GRAVITY);
        managerGravity.registerListener( this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopGyroscope(){
        managerGravity.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==Sensor.TYPE_GRAVITY){
            gravityData[0] = Float.parseFloat(String.format("%.2f", event.values[0]));
            gravityData[1] = Float.parseFloat(String.format("%.2f", event.values[1]));
            gravityData[2] = Float.parseFloat(String.format("%.2f", event.values[2]));
            Log.d("testare", "grv= " + gravityData[0] + " "+ gravityData[1]+ " "+ gravityData[2]);
            bazaDeDate.insertData("Gravity", gravityData[0], gravityData[1], gravityData[2]);
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
