package com.example.s.aplicatiecubuton;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends Activity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    Sensor accelerometer;

    Button buton1,buton2;
    EditText editText1, editText2;
    public static String fileName = "file.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "initialisign sensor services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered accelerometer listener");

        buton1 = (Button) findViewById(R.id.button);
        buton2 = (Button) findViewById(R.id.button2);
        editText1 = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);

        buton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText2.setText(readFile(fileName));
            }
        });

        buton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile(fileName, editText1.getText().toString());
            }
        });
    }

    public String saveFile(String file, String text){
        try{
            FileOutputStream fos = openFileOutput(file,Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Eroare", Toast.LENGTH_SHORT).show();

        }
        return text;
    }

    public String readFile(String file){
        String text="";
        try{
            FileInputStream fis = openFileInput(file);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            text = new String(buffer);
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Eroare", Toast.LENGTH_SHORT).show();
        }
        return text;
    }

    public void write(View view){

    }

    public void read(View view){

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "onSensorChanged: X: "+sensorEvent.values[0]+" ,Y"+sensorEvent.values[1]+" ,Z: "+sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

class PrimeThread extends Thread{
    long minPrime;
    PrimeThread(long minPrime){
        this.minPrime = minPrime;
    }

    public void run(){
        if(minPrime%2 == 0){
            Log.e("tag","MinPrime e prim");
        }
    }
}


