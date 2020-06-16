package com.example.carsharing.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharing.JSON.JSON_zmien_czas_rezerwacji;
import com.example.carsharing.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
                data_poczatkowa=poczatek_et.getText().toString();
                data_koncowa=koniec_et.getText().toString();

                ZmienCzasRezerwacji zmienCzasRezerwacji=new ZmienCzasRezerwacji();

                if(zmienCzasRezerwacji.sprawdz_czy_data_poprawna(ZmienCzasRezerwacji.this)) {
                    JSON_zmien_czas_rezerwacji json_zmien_czas_rezerwacji = new JSON_zmien_czas_rezerwacji();
                    json_zmien_czas_rezerwacji.StartUpdate(data_poczatkowa, data_koncowa, BookingId, ZmienCzasRezerwacji.this);
                }
            }
        });

        String s[]=PoczatekRezerwacji.split(" ");
        final String ss[]=s[0].split("-");

        poczatek_et=(EditText) findViewById(R.id.poczatek_et);
        poczatek_et.setText(PoczatekRezerwacji);
        poczatek_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date=true;
                end_date=false;
                Calendar c= Calendar.getInstance();
                c.set(Calendar.YEAR,Integer.valueOf(ss[0]));
                c.set(Calendar.MONTH,(Integer.valueOf(ss[1])-1));
                c.set(Calendar.DAY_OF_MONTH,Integer.valueOf(ss[2]));
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ZmienCzasRezerwacji.this, ZmienCzasRezerwacji.this,year,month,day);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        String sk[]=KoniecRezerwacji.split(" ");
        final String ssk[]=sk[0].split("-");

        koniec_et=(EditText) findViewById(R.id.koniec_et);
        koniec_et.setText(KoniecRezerwacji);
        koniec_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date=false;
                end_date=true;
                Calendar c= Calendar.getInstance();
                c.set(Calendar.YEAR,Integer.valueOf(ssk[0]));
                c.set(Calendar.MONTH,(Integer.valueOf(ssk[1])-1));
                c.set(Calendar.DAY_OF_MONTH,Integer.valueOf(ssk[2]));
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ZmienCzasRezerwacji.this, ZmienCzasRezerwacji.this,year,month,day);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });
}

    public boolean sprawdz_czy_data_poprawna(Context con){
        String s1= poczatek_et.getText().toString();
        String s2= koniec_et.getText().toString();
        try {
            Date date1=new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(s1);
            Date date2=new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(s2);
            if(date1.compareTo(date2)>0){
                alertDialog = new AlertDialog.Builder(con)
                        .setTitle("Uwaga")
                        .setMessage("Data końcowa musi być większa niż data początkowa !")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }else return true;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
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
        if(start_date){
            String s[]=PoczatekRezerwacji.split(" ");
            final String ss[]=s[1].split(":");
            c.set(Calendar.HOUR_OF_DAY,Integer.valueOf(ss[0]));
            c.set(Calendar.MINUTE,Integer.valueOf(ss[1]));
        }else if(end_date){
            String s[]=KoniecRezerwacji.split(" ");
            final String ss[]=s[1].split(":");
            c.set(Calendar.HOUR_OF_DAY,Integer.valueOf(ss[0]));
            c.set(Calendar.MINUTE,Integer.valueOf(ss[1]));
        }
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
           data_poczatkowa=yearFinal+"-"+monthFinal+"-"+dayFinal+" "+(hourFinal)+":"+(minuteFinal);
           poczatek_et.setText(data_poczatkowa);
       }else{
           data_koncowa=yearFinal+"-"+monthFinal+"-"+dayFinal+" "+(hourFinal)+":"+(minuteFinal);
           koniec_et.setText(data_koncowa);
       }


    }
}


