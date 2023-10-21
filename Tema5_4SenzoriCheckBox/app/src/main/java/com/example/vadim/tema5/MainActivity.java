package com.example.vadim.tema5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;


public class MainActivity extends Activity {

    GridView gridView;
    GridAdapter adapter;

    String senzori[] = {"Accelerometru", "Giroscop", "Gravity", "Lumina"};

    boolean but0=false, but1=false,but2=false,but3=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        adapter = new GridAdapter(getApplicationContext());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (senzori[position]){
                    case "Accelerometru":
                        if(!but0) {
                            view.setBackgroundColor(Color.GRAY);
                            but0 = true;
                            Intent intent = new Intent(getApplicationContext(), GridItemClickService.class);
                            intent.putExtra("sensDenum", senzori[position]);
                            startService(intent);
                        }
                        else { view.setBackgroundColor(Color.WHITE);but0 = false;
                            Intent intent = new Intent(getApplicationContext(), GridItemClickService.class);
                            intent.putExtra("sensDenum", senzori[position]);
                            startService(intent);
                        }
                        break;
                    case "Giroscop":
                        if(!but1) { view.setBackgroundColor(Color.GRAY);
                            but1 = true;
                            Intent intent = new Intent(getApplicationContext(), GyrListenerService.class);
                            startService(intent);
                        }
                        else { view.setBackgroundColor(Color.WHITE);
                            but1 = false;
                            Intent intent = new Intent(getApplicationContext(), GyrListenerService.class);
                            startService(intent);
                        }
                        break;
                    case "Gravity":
                        if(!but2) { view.setBackgroundColor(Color.GRAY);but2 = true;
                            Intent intent = new Intent(getApplicationContext(), GravityListenerService.class);
                            startService(intent);
                        }
                        else { view.setBackgroundColor(Color.WHITE);but2 = false;
                            Intent intent = new Intent(getApplicationContext(), GravityListenerService.class);
                            startService(intent);
                        }
                        break;
                    case "Lumina":
                        if(!but3) {
                            view.setBackgroundColor(Color.GRAY);
                            but3 = true;
                            Intent intent = new Intent(getApplicationContext(), LightListenerService.class);
                            startService(intent);
                        }
                        else { view.setBackgroundColor(Color.WHITE);but3 = false;
                            Intent intent = new Intent(getApplicationContext(), LightListenerService.class);
                            startService(intent);
                        }
                        break;
                }
            }
        });
    }

    private class GridAdapter extends BaseAdapter {
        private Context mContext;

        public GridAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return senzori.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View newView = getLayoutInflater().inflate(R.layout.griditem, null);

            //CheckBox checkBox = newView.findViewById(R.id.checkBox);
            TextView textView = newView.findViewById(R.id.textView);

            //checkBox.setId(position);
            textView.setId(position);

            textView.setText(senzori[position]);

            newView.setBackgroundColor(Color.WHITE);
            return newView;
        }
    }

    public void afisare(View view){

        CheckBox checkBox1 = (CheckBox)findViewById(R.id.checkBox1);
        CheckBox checkBox2 = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox checkBox3 = (CheckBox)findViewById(R.id.checkBox3);
        CheckBox checkBox4 = (CheckBox)findViewById(R.id.checkBox4);

        char checks[] = new char[4];

        if(checkBox1.isChecked()) checks[0] = '1'; else checks[0] = '0';
        if(checkBox2.isChecked()) checks[1] = '1'; else checks[1] = '0';
        if(checkBox3.isChecked()) checks[2] = '1'; else checks[2] = '0';
        if(checkBox4.isChecked()) checks[3] = '1'; else checks[3] = '0';

        Intent intent = new Intent(getApplicationContext(), Afisare.class);
        intent.putExtra("dateChecks", new String(checks));
        startActivity(intent);
    }
}


