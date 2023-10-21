package com.example.deny.curs5service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private static final String ACTION_SERVICE = "myBroadcast";
    static Thread myThread;
    static boolean threadRunning = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Test", "Start Service");

        start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("Test", "Stop Service");
        try {
            stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void start()
    {
        if(!threadRunning) threadRunning = true;
        if(myThread == null || !myThread.isAlive())
        {
            myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("Text", "Thread Start");
                    int i = 0;
                    while (threadRunning)
                    {
                        Log.d("Test", "5 Seconds");
                        sendBroadcast(new Intent(ACTION_SERVICE).putExtra("seconds", i));
                        //numarul de ciclari de cate ori apare
                        i++;
                        try
                        {
                            Thread.sleep(5000);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                            Log.d("Test", "InterruptedException");
                        }
                    }
                    Log.d("Test", "Thread stop");
                }
            });
            myThread.start();
        }
        else Log.d("Test", "Thread it's already started");
    }
    public void stop() throws InterruptedException
    {
        //verificam daca thread este activ
        if(myThread.isAlive())
        {
            //il facem falc
            threadRunning = false;
            myThread.interrupt();
            //daca noi vrem sa dam stop sa se opreasca atunci cand vrem noi, nu dupa 5 secunde
            //in maxim 500 de msec
            //inca o metoda: thread with, cu notyfi
        }
    }
}
