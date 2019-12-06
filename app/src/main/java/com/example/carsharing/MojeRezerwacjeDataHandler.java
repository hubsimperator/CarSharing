package com.example.carsharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MojeRezerwacjeDataHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CSRezerwacje.db";
    public static final String TABLE_NAME = "REZERWACJETABLE";
    public static final String ID = "ID";
    public static final String SUBJECT = "SUBJECT";
    public static final String START_DATA = "START_DATA";
    public static final String CAR = "CAR";
    public static final String CT = "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+SUBJECT+" text not null,"+START_DATA+" text not null,"+CAR+" text not null)";

    public MojeRezerwacjeDataHandler(Context context) {
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
    /*
    public boolean inputDataTime(Boolean Renember, String Login, String Password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBJECT,Login);
        contentValues.put(START_DATA,Password);
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

     */
}
