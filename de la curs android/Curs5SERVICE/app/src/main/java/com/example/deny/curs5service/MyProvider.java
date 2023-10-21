package com.example.deny.curs5service;

import android.app.Service;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;

public class MyProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        String action = uri.getLastPathSegment();
        Log.d("Test","Query: " + action);
        String response = null;
        switch (action)
        {
            case "Time":
                Date dt = new Date();
                //facem un string in care returnam timpul
                response = dt.toString();
                break;
            default:
                response = "Necunoscut";
                break;
        }
        MatrixCursor cursor = new MatrixCursor(new String[] {response}, 1);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
