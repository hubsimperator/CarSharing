package com.example.carsharing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProjektWybor {


    public static ArrayList<String> grupa_proj=new ArrayList<>();
    public static ArrayList<String> numer_proj=new ArrayList<>();

    View view;
    AlertDialog alertDialog;

public void a(Context con2){
    try {
        Projekty_DataHandler PDH = new Projekty_DataHandler(con2);
        Cursor getdata = PDH.getGrup();
        getdata.moveToFirst();
        String test = getdata.getString(0);
        while (getdata.moveToNext()) {
            grupa_proj.add(getdata.getString(0));
        }
        PDH.close();
    } catch (Exception e) {
        Logs_DataHandler log = new Logs_DataHandler(con2);
        log.inputLog("Login.class 006: " + e.toString());
        log.close();
    }
}


public void s(Context con1){
    ProjektWybor pw = new ProjektWybor();
    pw.a(con1);
    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con1)
            .setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();

                }
            })
            .setNegativeButton("Wybierz", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

    LayoutInflater inflater = (LayoutInflater)   con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    View view = inflater.inflate(R.layout.projekty,null);

    dialogBuilder.setView(view);

    alertDialog =dialogBuilder.create();
    alertDialog.show();
}


}