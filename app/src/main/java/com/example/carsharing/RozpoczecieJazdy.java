package com.example.carsharing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RozpoczecieJazdy extends AppCompatActivity {

     int year,month,day,hour,minute;
     String dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    public static EditText poczatek_et;
    public static EditText koniec_et;


   public static EditText proj_et;
     String data_poczatkowa;
     String data_koncowa;
    public static ImageView rozpocznij_bt;

    public static String PoczatekRezerwacji;
    public static String KoniecRezerwacji;
    public static String BookingId;

    public static String GrupaProjektu;
    public static String NazwaProjektu;


    boolean start_date=false;
     boolean end_date=false;

    View view;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rozpoczeciejazdy);

        Bundle extras= getIntent().getExtras();
        PoczatekRezerwacji=extras.getString("StartDate");
        KoniecRezerwacji=extras.getString("EndDate");
        BookingId=extras.getString("BookingId");

        rozpocznij_bt=(ImageView) findViewById(R.id.potwierdz_bt);
        rozpocznij_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSON_start_trip json_start_trip=new JSON_start_trip();
                json_start_trip.StartUpdate(BookingId,GrupaProjektu,NazwaProjektu,RozpoczecieJazdy.this);
            }
        });


        Button Skaner= (Button) findViewById(R.id.scanner_bt);
        Skaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),QRScanner.class));
            }
        });

        proj_et=(EditText) findViewById(R.id.projekt_et);
        proj_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSON_lista_projektow json_lista_projektow=new JSON_lista_projektow();
                json_lista_projektow.StartUpdate(1,RozpoczecieJazdy.this);
            }
        });


        ImageView back_bt=(ImageView) findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        JSON_lista_projektow json_lista_projektow=new JSON_lista_projektow();
        json_lista_projektow.StartUpdate(1,RozpoczecieJazdy.this);
}

    public void wyswietl_projekt(Context con, String _proj, String _grupa_projektu){
      proj_et.setText(_proj);
      NazwaProjektu=_proj;
      GrupaProjektu=_grupa_projektu;
        rozpocznij_bt.setVisibility(View.VISIBLE);


    }



}


