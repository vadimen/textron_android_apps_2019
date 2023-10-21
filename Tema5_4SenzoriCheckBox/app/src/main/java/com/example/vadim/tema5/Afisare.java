package com.example.vadim.tema5;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Afisare extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afisare);

        Intent intent = getIntent();
        String stringDate = intent.getStringExtra("dateChecks");


        BazaDeDate bazaDeDate = new BazaDeDate(this);
        Cursor date;

        EditText textView = findViewById(R.id.editText);
        textView.clearComposingText();

        if(stringDate.charAt(0) == '1'){
            date = bazaDeDate.getData("Accelerometru");
            while(date.moveToNext()){
                textView.append("Acc" +" "+ date.getDouble(2) +" "+ date.getDouble(3) +" "+ date.getDouble(4) + "\n");
            }
        }

        if(stringDate.charAt(2) == '1'){
            date = bazaDeDate.getData("Giroscop");
            while(date.moveToNext()){
                textView.append("Gir" +" "+ date.getDouble(2) +" "+ date.getDouble(3) +" "+ date.getDouble(4) + "\n");
            }
        }

        if(stringDate.charAt(1) == '1'){
            date = bazaDeDate.getData("Gravity");
            while(date.moveToNext()){
                textView.append("Grv" +" "+ date.getDouble(2) +" "+ date.getDouble(3) +" "+ date.getDouble(4) + "\n");
            }
        }

        if(stringDate.charAt(3) == '1'){
            date = bazaDeDate.getData("Lumina");
            while(date.moveToNext()) {
                textView.append(date.getString(1) + " " + date.getDouble(2) + "\n");
            }
        }
    }
}
