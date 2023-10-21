package com.example.vadim.bazadedate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "FeedReader.db";
    public static final String TABLE_NAME = "info";

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + TABLE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NUME TEXT, PRENUME TEXT, VARSTA INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String nume, String prenume, Integer varsta){
        SQLiteDatabase sqlDb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NUME", nume);
        cv.put("PRENUME", prenume);
        cv.put("VARSTA", varsta);
        long res = sqlDb.insert(TABLE_NAME,null,cv);

        return (false ? res==-1 : true);
    }

    public Cursor getDataSortedByName(){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor res = sqlDB.rawQuery("select * from "+TABLE_NAME+" order by NUME ASC", null);

        return res;
    }

    public Cursor getDataSortedByPreName(){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor res = sqlDB.rawQuery("select * from "+TABLE_NAME+" order by PRENUME ASC", null);

        return res;
    }

    public Cursor getDataSortedByVarsta(){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor res = sqlDB.rawQuery("select * from "+TABLE_NAME+" order by VARSTA ASC", null);

        return res;
    }
}