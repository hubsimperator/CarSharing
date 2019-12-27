package com.example.carsharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Logs_DataHandler  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Log.db";
    public static final String TABLE_NAME = "LogTABLE";
    public static final String ID = "ID";
    public static final String VAL = "VAL";
    public static final String CT = "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+VAL+" text not null)";
    public Logs_DataHandler(Context context) {
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
    public void deletewpis(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_NAME,ID+"="+id,null);
        db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(id) });
    }
    public void dropdatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }
    public boolean inputLog(String Log) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VAL,Log);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
}
