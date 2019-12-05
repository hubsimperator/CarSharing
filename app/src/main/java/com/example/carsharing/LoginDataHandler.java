package com.example.carsharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDataHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CSUser.db";
    public static final String TABLE_NAME = "USERTABLE";
    public static final String ID = "ID";
    public static final String USER = "USER";
    public static final String PASS = "PASS";
    public static final String REN = "REN"; //Renember?/Zapamiętać?
    public static final String CT = "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+USER+" text not null,"+PASS+" text not null,"+REN+" text not null)";
    public LoginDataHandler(Context context) {
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
    public boolean inputDataTime(Boolean Renember, String Login, String Password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER,Login);
        contentValues.put(PASS,Password);
        if(Renember){
        contentValues.put(REN,"true");
        }
        else{
            contentValues.put(REN,"False");
        }
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
}
