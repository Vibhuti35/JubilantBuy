package com.example.tonycurrie.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Login details
public class DatabaseHelperLogin extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="registration.db";
    public static final String TABLE_NAME="Registration";
    public static final String COL_1="ID";
    public static final String COL_2="First_Name";
    public static final String COL_3="Last_Name";
    public static final String COL_4="Username";
    public static final String COL_5="Password";
    public static final String COL_6="Address_Line_1";
    public static final String COL_7="Address_Line_2";
    public static final String COL_8="City";
    public static final String COL_9="Postal_Code";
    public static final String COL_10="Phone";

    public DatabaseHelperLogin(Context context)
    {
        super(context,DATABASE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,First_Name TEXT,Last_Name TEXT,Username TEXT,Password TEXT,Address_Line_1 TEXT,Address_Line_2 TEXT,City TEXT,Postal_Code TEXT,Phone TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME); //Drop older table if exists
        onCreate(db);
    }
}