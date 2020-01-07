package com.example.carsharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Projekty_DataHandler  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CSProj.db";
    public static final String TABLE_NAME = "Projekty";
    public static final String ID = "ID";
    public static final String GR = "GR";
    public static final String NR = "NR";
    public static final String DEF = "DEF";
    public static final String CT = "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+GR+" text not null,"+NR+" text not null)";

    public Projekty_DataHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
        getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " +TABLE_NAME + CT);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getGrup(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select distinct GR from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getNumber(String Group){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select NR from "+TABLE_NAME+" where GR = '"+Group+"'",null);
        return res;
    }
    public void dropdatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }

    public boolean inputData(String Grupa, String Numer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GR,Grupa);
        contentValues.put(NR,Numer);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }


}

