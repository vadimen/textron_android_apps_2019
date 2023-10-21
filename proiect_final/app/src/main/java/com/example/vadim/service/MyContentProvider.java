package com.example.vadim.service;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class MyContentProvider extends ContentProvider {

    private static final String TAG = "TESTGPS";

    static final String PROVIDER_NAME = "com.example.vadim.service.MyContentProvider";
    static final String URL = "content://" + PROVIDER_NAME;
    static final Uri CONTENT_URI = Uri.parse(URL);


    static final String ROW_ID = "_id";
    static final String ROW_LAT = "lat";
    static final String ROW_LON = "lon";
    static final String ROW_TIMESTAMP = "timeStamp";
    static final String ROW_ACC_X = "x";
    static final String ROW_ACC_Y = "y";
    static final String ROW_ACC_Z = "z";

    static final int ALL_DATA = 1;
    static final int ONE_ROW = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "data", ALL_DATA);
        uriMatcher.addURI(PROVIDER_NAME, "data/#", ONE_ROW);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DataBaseHelper dbHelper = new DataBaseHelper(context);

        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query "+uri.toString());

        switch(uriMatcher.match(uri)){
            case ALL_DATA:
                Log.d(TAG, "ALL DATA");
                break;
            case ONE_ROW:
                Log.d(TAG, "ONE ROW");
                String id = uri.getLastPathSegment();
                Log.d(TAG, "URI id "  + id);
                if(TextUtils.isEmpty(selection)){
                    selection = ROW_ID + " = " + id;
                }else{
                    selection = selection + " AND " + ROW_ID + " = " + id;
                }
                break;
                default:
                    throw new IllegalArgumentException("Wrong URI " + uri);
        }

        Cursor cursor = db.query(DataBaseHelper.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);

        MatrixCursor cursor2 = new MatrixCursor(new String[] {"hello"}, 1);
        return cursor2;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri,String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,  String[] selectionArgs) {
        return 0;
    }
}
