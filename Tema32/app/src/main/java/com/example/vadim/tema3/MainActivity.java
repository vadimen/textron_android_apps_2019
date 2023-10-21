package com.example.vadim.tema3;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

//    public Accelerometru accService;
//    private boolean isBound = false;
//    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        intent = new Intent(this, Accelerometru.class);
    }

    public void goRandom(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void drawMap(View view){
         Intent intent = new Intent(this, DrawMapFromDb.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(intent);
    }

    public void trackFromGPS(View view){
        Intent intent = new Intent(this, TrackFromGPS.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

//    public void startService(View view) {
//        if(!isBound){
//            startService(intent);
//            getApplicationContext().bindService(intent, servConn, Context.BIND_AUTO_CREATE);
//            Log.d("test", "Service started");
//        }else{
//            Log.d("test", "Service could not be started");
//        }
//    }
//
//    public void stopService(View view){
//        if(isBound){
//            getApplicationContext().unbindService(servConn);
//            stopService(intent);
//            isBound = false;
//            Log.d("test", "Service stopped");
//        }else{
//            Log.d("test", "Service could not be started");
//        }
//    }
//
//    public void getData(View view){
//        if(isBound){
//            float[] data = accService.getSensorData();
//            Log.d("test", ""+data[0]+" "+data[1]+" "+data[2]);
//            Toast.makeText(this, ""+data[0]+" "+data[1]+" "+data[2], Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private ServiceConnection servConn = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder binder) {
//            accService = ((Accelerometru.LocalBinder) binder).getService();
//            isBound = true;
//            Log.d("test", "Service connected");
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            accService = null;
//            isBound = false;
//            Log.d("test", "Service disconnected");
//        }
//    };
}
