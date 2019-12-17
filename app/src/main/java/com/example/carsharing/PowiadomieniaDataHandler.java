package com.example.carsharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PowiadomieniaDataHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CSNotif.db";
    public static final String TABLE_NAME = "NOTIFTABLE";
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String BODY = "BODY";
    public static final String CT = "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TITLE+" text not null,"+BODY+" text not null)";
    public PowiadomieniaDataHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
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
    public void dropdatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }
    public boolean inputDataTime( String Titles, String Bodys) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE,Titles);
        contentValues.put(BODY,Bodys);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
}