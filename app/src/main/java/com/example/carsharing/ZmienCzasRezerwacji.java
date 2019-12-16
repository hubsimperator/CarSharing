package com.example.carsharing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class ZmienCzasRezerwacji extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

     int year,month,day,hour,minute;
     String dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    public static EditText poczatek_et;
    public static EditText koniec_et;

     String data_poczatkowa;
     String data_koncowa;
    public static ImageView rezerwuj_bt;

    public static String PoczatekRezerwacji;
    public static String KoniecRezerwacji;
    public static String BookingId;

    boolean start_date=false;
     boolean end_date=false;

    View view;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zmienczas);

        Bundle extras= getIntent().getExtras();
        PoczatekRezerwacji=extras.getString("StartDate");
        KoniecRezerwacji=extras.getString("EndDate");
        BookingId=extras.getString("BookingId");

        ImageView back_bt=(ImageView) findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView potwierdz=(ImageView) findViewById(R.id.potwierdz_bt);
        potwierdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSON_zmien_czas_rezerwacji json_zmien_czas_rezerwacji=new JSON_zmien_czas_rezerwacji();
                json_zmien_czas_rezerwacji.StartUpdate(data_poczatkowa,data_koncowa,BookingId,ZmienCzasRezerwacji.this);
            }
        });


        poczatek_et=(EditText) findViewById(R.id.poczatek_et);
        poczatek_et.setText(PoczatekRezerwacji);
        poczatek_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date=true;
                end_date=false;
                Calendar c= Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ZmienCzasRezerwacji.this, ZmienCzasRezerwacji.this,year,month,day);
                datePickerDialog.show();
            }
        });

        koniec_et=(EditText) findViewById(R.id.koniec_et);
        koniec_et.setText(KoniecRezerwacji);
        koniec_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date=false;
                end_date=true;
                Calendar c= Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ZmienCzasRezerwacji.this, ZmienCzasRezerwacji.this,year,month,day);
                datePickerDialog.show();
            }
        });

}

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month=month+1;
        if(year<10){
            yearFinal="0"+Integer.toString(year);
        }else{
            yearFinal=Integer.toString(year);
        }

        if(month<10){
            monthFinal="0"+Integer.toString(month);
        }else{
            monthFinal=Integer.toString(month);
        }

        if(dayOfMonth<10){
            dayFinal="0"+Integer.toString(dayOfMonth);
        }else{
            dayFinal=Integer.toString(dayOfMonth);
        }

     Calendar c= Calendar.getInstance();
     hour=c.get(Calendar.HOUR_OF_DAY);
     minute=c.get(Calendar.MINUTE);

     TimePickerDialog timePickerDialog = new TimePickerDialog(ZmienCzasRezerwacji.this, ZmienCzasRezerwacji.this,hour,minute,DateFormat.is24HourFormat(this));
     timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(hourOfDay<10){
            hourFinal="0"+Integer.toString(hourOfDay);
        }else{
            hourFinal=Integer.toString(hourOfDay);
        }
        if(minute<10){
            minuteFinal="0"+Integer.toString(minute);
        }else{
            minuteFinal=Integer.toString(minute);
        }

        String data=(dayFinal)+"-"+(monthFinal)+"-"+(yearFinal);
        String godzina=(hourFinal)+":"+(minuteFinal);
       if(start_date) {
           data_poczatkowa=yearFinal+"-"+monthFinal+"-"+dayFinal+" "+(hourFinal)+":"+(minuteFinal)+":00";
           poczatek_et.setText(data_poczatkowa);

       }else{

           data_koncowa=yearFinal+"-"+monthFinal+"-"+dayFinal+" "+(hourFinal)+":"+(minuteFinal)+":00";
           koniec_et.setText(data_koncowa);
       }


    }
}


