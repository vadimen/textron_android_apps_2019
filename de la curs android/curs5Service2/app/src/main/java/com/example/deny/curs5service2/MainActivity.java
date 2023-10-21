package com.example.deny.curs5service2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    Handler handler = new Handler();
    ProviderClient providerClient;
    Runnable rTime = new Runnable() {
        @Override
        public void run() {
            //un runnable e tot un thread
            //il pornim el va rula si daca vrem sa ruleze inca odata
            //trebuie programat inca odata ca sa se reapeleze pt 5 sec
            //fscem asta cu ajutotul lui handler
            textView2.setText(providerClient.getValue("Time"));
            handler.postDelayed(rTime, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        providerClient = new ProviderClient(this);

        textView1 = findViewById(R.id.tvRaceivar);
        textView2 = findViewById(R.id.textView2);
        handler.postDelayed(rTime, 1000);
    }
    
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int valueReceived = intent.getIntExtra("seconds", 1);
            //intent contine acele date pe care noi le-am pus pe send
            //seconds este o cheie
            Log.d("Test", "" + valueReceived);
            textView1.setText(valueReceived + "");
        }
    };

    @Override
    protected void onStart() {
        //inregistra doar cu un anumit filtru, ca sa primim doar ce ne intereseaza cu un filtru
        //doar actiunea de MyBroadcast
        registerReceiver(broadcastReceiver, new IntentFilter("myBroadcast"));
        super.onStart();
    }

    @Override
    protected void onStop() {

        //ne asiguram ca inchidem Broadcastca sa nu mai foloseasca bateria
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}
