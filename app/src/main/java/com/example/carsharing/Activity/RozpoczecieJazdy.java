package com.example.carsharing.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharing.JSON.JSON_moje_rezerwacje;
import com.example.carsharing.JSON.JSON_moje_rezerwacje_new;
import com.example.carsharing.JSON.JSON_start_trip;
import com.example.carsharing.Other.ProjektWybor;
import com.example.carsharing.Other.QRScanner;
import com.example.carsharing.R;

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
        GrupaProjektu=extras.getString("GrupaProjektu");
        NazwaProjektu=extras.getString("NrProjektu");

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
                startActivity(new Intent(getApplicationContext(), QRScanner.class));
            }
        });

        proj_et=(EditText) findViewById(R.id.notatka);
        proj_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JSON_lista_projektow json_lista_projektow=new JSON_lista_projektow();
                //json_lista_projektow.StartUpdate(1,RozpoczecieJazdy.this);
                ProjektWybor projektWybor=new ProjektWybor();
                projektWybor.WyborProjektu(RozpoczecieJazdy.this,1);
            }
        });


        ImageView back_bt=(ImageView) findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //JSON_moje_rezerwacje json_moje_rezerwacje=new JSON_moje_rezerwacje();
              //  json_moje_rezerwacje.StartUpdate("","",RozpoczecieJazdy.this);
                JSON_moje_rezerwacje_new json_moje_rezerwacje_new=new JSON_moje_rezerwacje_new();
                json_moje_rezerwacje_new.StartUpdate(RozpoczecieJazdy.this);

            }
        });

        proj_et.setText(NazwaProjektu);
     //   JSON_lista_projektow json_lista_projektow=new JSON_lista_projektow();
      //  json_lista_projektow.StartUpdate(1,RozpoczecieJazdy.this);
}

    public void wyswietl_projekt(Context con, String _proj, String _grupa_projektu){
      proj_et.setText(_proj);
      NazwaProjektu=_proj;
      GrupaProjektu=_grupa_projektu;
       // rozpocznij_bt.setVisibility(View.VISIBLE);


    }



}


