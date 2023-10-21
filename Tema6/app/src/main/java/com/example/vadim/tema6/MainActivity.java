package com.example.vadim.tema6;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    JSONObject json;
    EditText editText;

    StringBuilder vocale;
    StringBuilder consoane;
    StringBuilder caractSpec;

    boolean threadReady = false;
    Handler handler;

    Runnable r = new Runnable() {
        @Override
        public void run() {
            if(!threadReady){
                handler.postDelayed(r,100);
            }else{
                editText.clearComposingText();
                editText.append(json.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        json = new JSONObject();

        editText = (EditText) findViewById(R.id.editText);

        Thread thread = new AnotherThread();
        thread.start();

        handler.postDelayed(r, 100);

    }

    class AnotherThread extends Thread{
        public void run() {
            URL url;

            HttpURLConnection urlConnection = null;

            try{
                url = new URL("http://www.google.com");
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream pageInputStream = urlConnection.getInputStream();
                InputStreamReader pageStreamReader = new InputStreamReader(pageInputStream);

                int data = pageStreamReader.read();

                String vcl = "aeiou";
                String cns = "bcdfghjklmnpqrstvwxz";
                vocale = new StringBuilder();
                consoane = new StringBuilder();
                caractSpec = new StringBuilder();

                while(data!=-1){
                    String chr = (((char)data)+"").toLowerCase();
                    if(vcl.contains(chr)){
                        vocale.append((char)data);
                    }else if(cns.contains(chr)){
                        consoane.append((char)data);
                    }else{
                        caractSpec.append((char)data);
                    }

                    data = pageStreamReader.read();
                }

                json.put("vocale", new String(vocale));
                json.put("consoane", new String(consoane));
                json.put("caractSpec", new String(caractSpec));

                Log.d("jsnb", json.toString());

            }catch(Exception e){
                e.printStackTrace();
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
            }

            threadReady = true;
            this.interrupt();
        }
    }

}
