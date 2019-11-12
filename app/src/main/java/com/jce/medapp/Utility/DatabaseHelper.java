package com.jce.medapp.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MedApp.db";
    public static final String TABLE_NAME_1 = "Emergency_Contacts";
    public static final String TABLE_NAME_2 = "Friends";
    public static final String TABLE_NAME_3 = "Medical_History";


    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+TABLE_NAME_2+" (F_ID INTEGER PRIMARY KEY AUTOINCREMENT,F_NAME TEXT,F_PHONE TEXT)");
        db.execSQL("create table "+TABLE_NAME_1+" (EC_ID INTEGER PRIMARY KEY AUTOINCREMENT,EC_NAME TEXT,EC_PHONE TEXT)");
        db.execSQL("create table "+TABLE_NAME_3+" (MH_ID INTEGER PRIMARY KEY AUTOINCREMENT,MH_NAME TEXT,MH_TEXT TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_3);
        onCreate(db);

    }

    public boolean insertEmergencyContacts(String name,String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EC_NAME",name);
        contentValues.put("EC_PHONE",phone);
        long result = db.insert(TABLE_NAME_1,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getEmergencyContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_1,null);
        return res;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_1, "EC_ID = ?",new String[] {id});
    }


    public boolean insertFriends(String name,String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("F_NAME",name);
        contentValues.put("F_PHONE",phone);
        long result = db.insert(TABLE_NAME_2,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getFriends() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_2,null);
        return res;
    }

    public boolean insertMedicalHistory(String name,String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MH_NAME",name);
        contentValues.put("MH_TEXT",text);
        long result = db.insert(TABLE_NAME_3,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getMedicalHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_3,null);
        return res;
    }
}
