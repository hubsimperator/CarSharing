package com.example.carsharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class lista_projektow_DataHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Projekty.db";
    public static final String TABLE_NAME = "ProjTABLE";
    public static final String ID = "ID";
    public static final String GRUPA_PROJ = "GRUPA_PROJ";
    public static final String NUMER_PROJ = "NUMER_PROJ";
    public static final String CT = "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ GRUPA_PROJ +" text not null,"+ NUMER_PROJ +" text not null)";
    public lista_projektow_DataHandler(Context context) {
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
    public boolean inputProjekt(String GrupaProjektu, String NumerProjektu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GRUPA_PROJ,GrupaProjektu);
        contentValues.put(NUMER_PROJ,NumerProjektu);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
}
