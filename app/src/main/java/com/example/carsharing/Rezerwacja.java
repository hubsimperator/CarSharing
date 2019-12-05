package com.example.carsharing;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;


import androidx.appcompat.app.AppCompatActivity;

import android.text.format.DateFormat;

import java.util.Calendar;

public class Rezerwacja extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public int year,month,day,hour,minute;
    public String dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    public static EditText poczatek_et;
    public static EditText koniec_et;

    public boolean start_date=false;
    public boolean end_date=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rezerwacja);

        poczatek_et=(EditText) findViewById(R.id.poczatek_et);
        poczatek_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date=true;
                end_date=false;
                Calendar c= Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Rezerwacja.this,Rezerwacja.this,year,month,day);
                datePickerDialog.show();
            }
        });

        koniec_et=(EditText) findViewById(R.id.koniec_et);
        koniec_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date=false;
                end_date=true;
                Calendar c= Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Rezerwacja.this,Rezerwacja.this,year,month,day);
                datePickerDialog.show();
            }
        });


        ImageView back_bt = (ImageView) findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
}

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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

     TimePickerDialog timePickerDialog = new TimePickerDialog(Rezerwacja.this,Rezerwacja.this,hour,minute,DateFormat.is24HourFormat(this));
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
        String godzina=(hourFinal)+":"+(minute);
       if(start_date) {
           poczatek_et.setText(data + "  " + godzina);
       }else{
           koniec_et.setText(data + "  " + godzina);

       }


    }
}


