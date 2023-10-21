package com.example.vadim.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    static final String ROW_ID = "_id";
    static final String ROW_LAT = "lat";
    static final String ROW_LON = "lon";
    static final String ROW_ACC_X = "x";
    static final String ROW_ACC_Y = "y";
    static final String ROW_ACC_Z = "z";
    static final String ROW_TIMESTAMP = "timeStamp";
    static final String ROW_SPEED = "speed";

    static final String DATABASE_NAME = "MainDatabase";
    static final String TABLE_NAME = "GpsData";
    static final int DATABASE_VERSION = 2;
    static final String CREATE_DB_TABLE_SQL = "create table "+TABLE_NAME+" (_id integer primary key autoincrement, " +
            "lat double, lon double, x double, y double, z double,timeStamp text, speed double)";


    DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(double lat, double lon, double x, double y,double z){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ROW_LAT, lat);
        cv.put(ROW_LON, lon);
        cv.put(ROW_ACC_X, x);
        cv.put(ROW_ACC_Y, y);
        cv.put(ROW_ACC_Z, z);

        long res = db.insert(TABLE_NAME, null, cv);
        Log.d("DB insert", res+" ");
        return res==-1 ? false : true;

    }
}
