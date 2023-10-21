package com.example.vadim.bazadedate;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SortPrenume extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_prenume);

        DbHelper sqlDB = new DbHelper(this);
        Cursor c = sqlDB.getDataSortedByPreName();

        EditText longText = (EditText)findViewById(R.id.editText);
        longText.clearComposingText();

        while(c.moveToNext()){
            longText.append("ID: "+c.getString(0)+" "+c.getString(1)+" "+
                    c.getString(2)+" "+c.getString(3)+";\n");
        }
    }

    public void mergiInapoi(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
