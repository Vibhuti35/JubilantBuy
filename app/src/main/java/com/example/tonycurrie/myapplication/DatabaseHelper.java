package com.example.tonycurrie.myapplication;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

//This is the main database in which dummy product details,barcode details and payment details are stored
public class DatabaseHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "maindatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}