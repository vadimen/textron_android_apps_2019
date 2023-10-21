package com.example.pc.client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MessageSender().execute("parametru");
    }

    public class MessageSender extends AsyncTask<String,Void,Void> {
        Socket sock;
        DataOutputStream dos;
        PrintWriter pw;
        private String message;
        private String[] voids;

//        @Override
//        protected Void doInBackground(String... voids){
//            this.voids = voids;
//            String[] message = this.voids;
//
//            try{
//                s = new Socket("192.168.94.5", 9999);
//                pw = new PrintWriter(s.getOutputStream());
//                pw.write(message[0]);
//                pw.flush();
//                pw.close();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//
//            return null;
//        }


        @Override
        protected Void doInBackground(String... strings) {
            URL url;

            HttpURLConnection urlConnection = null;

            try{
                url = new URL("http://www.google.com");
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader isr = new InputStreamReader(in);

                int data = isr.read();
                StringBuilder s = new StringBuilder();

                while(data!= -1){
                    char current = (char)data;
                    data = isr.read();
                    s.append(current);
                }

                sock = new Socket("192.168.94.5", 9999);
                pw = new PrintWriter(sock.getOutputStream());
                pw.write(new String(s));
                pw.flush();
                pw.close();

                Log.d("ttt", String.valueOf(s));
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(urlConnection!=null){urlConnection.disconnect();}
            }

            return null;
        }
    }
}
