package com.example.vadim.tema5;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BazaDeDate extends SQLiteOpenHelper {
    public static final String DB_NAME = "DateSenzori.db";
    public static final String TABLE_NAME = "datePrimite";

    public BazaDeDate(Context context){
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" (id integer primary key autoincrement, " +
                "tip string, x double, y double, z double)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String tip, double x,double y,double z){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tip", tip);
        cv.put("x", x);
        cv.put("y", y);
        cv.put("z", z);

        long res = sqlDB.insert(TABLE_NAME, null, cv);
        return res==-1 ? false : true;
    }

    public Cursor getData(String tip){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME + " where tip = '"+ tip+"'", null);
        return res;
    }
}
