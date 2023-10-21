package com.example.vadim.bazadedate;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    DbHelper sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlDB = new DbHelper(this);
    }

    public void addData(View view){
        String textInput1 = ((EditText)findViewById(R.id.input1)).getText().toString();
        String textInput2 = ((EditText)findViewById(R.id.input2)).getText().toString();
        String textInput3 = ((EditText)findViewById(R.id.input3)).getText().toString();

        if(textInput1.length()>0 && textInput2.length()>0 && textInput3.length()>0 && (textInput3.charAt(0) >= '0' && textInput3.charAt(0) <='9') ){
            boolean reusit = sqlDB.insertData(textInput1, textInput2, Integer.parseInt(textInput3));
            Toast.makeText(MainActivity.this, "Adaugare cu succes", Toast.LENGTH_LONG).show();

            if(!reusit){
                Toast.makeText(MainActivity.this, "Eroare :(", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(MainActivity.this, "Camp gol sau varsta nu e numar :(", Toast.LENGTH_LONG).show();
        }
    }

    public void showSortedByName(View view){

        Intent intent = new Intent(this, SortateDupaNume.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void showSortedByPreName(View view){

        Intent intent = new Intent(this, SortPrenume.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void showSortedByVarsta(View view){

        Intent intent = new Intent(this, SortVarsta.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
