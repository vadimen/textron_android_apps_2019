package com.example.vadim.tema3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class BazaDeDate extends SQLiteOpenHelper {
    public static final String DB_NAME = "PositionData.db";
    public static final String TABLE_NAME = "positions";

    public BazaDeDate(Context context){
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" (id integer primary key autoincrement, " +
                "lat double, lon double, x double, y double, z double)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }

    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(double lat,double lon,double x,double y,double z){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("lat", lat);
        cv.put("lon", lon);
        cv.put("x", x);
        cv.put("y", y);
        cv.put("z", z);

        long res = sqlDB.insert(TABLE_NAME, null, cv);
        return res==-1 ? false : true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }
}
