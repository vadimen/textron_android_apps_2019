package com.example.vadim.tema4;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            },1);
        }
    }

    public void startService(View view){
        Intent intent = new Intent(MainActivity.this, ReadBroadcastService.class);
        intent.putExtra("Test", "Start");
        startService(intent);
    }

    public void stopService(View view){
        Intent intent = new Intent(MainActivity.this, ReadBroadcastService.class);
        intent.putExtra("Test", "Stop");
        stopService(intent);
    }
}
