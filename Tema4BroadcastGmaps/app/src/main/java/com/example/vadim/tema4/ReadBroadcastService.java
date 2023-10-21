package com.example.vadim.tema4;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadBroadcastService extends Service {

    private static final String ACTION_SERVICE = "myBroadcast";
    static Thread myThread;
    static boolean threadRunning = false;

    BufferedReader br;
    InputStream input;
    String line;

    public ReadBroadcastService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("Test", "Start Service");

        try{
            input = getAssets().open("gps_data.txt");
            br = new BufferedReader(new InputStreamReader(input));

        }catch (Exception e){
            e.printStackTrace();
        }

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
                    int i =0;

                    while (threadRunning)
                    {
                        try{
                            while((line = br.readLine())!=null){
                                //String gpsdata[] = line.split(";");
                                Log.d("gpsdata", line);
                                sendBroadcast(new Intent(ACTION_SERVICE).putExtra("gpsdata", line+""));

                                try
                                {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                    Log.d("Test", "InterruptedException");
                                }
                            }
                        }catch (IOException e){
                            e.printStackTrace();
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
        if(myThread.isAlive())
        {
            threadRunning = false;
            myThread.interrupt();
            //daca noi vrem sa dam stop sa se opreasca atunci cand vrem noi, nu dupa 5 secunde
            //in maxim 500 de msec
            //inca o metoda: thread with, cu notyfi
        }
    }
}
