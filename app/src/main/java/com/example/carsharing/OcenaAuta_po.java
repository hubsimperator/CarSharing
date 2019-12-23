package com.example.carsharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OcenaAuta_po extends AppCompatActivity {

    public static String PoczatekRezerwacji;
    public static String KoniecRezerwacji;
    public static String BookingId;
    public static String GrupaProjektu;
    public static String NazwaProjektu;
    public static String NrProjektu;


    public static Switch switch1;
    public static Switch switch2;
    public static Switch switch3;

ArrayList<Integer> switch_on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocen_auto_po);


        Bundle extras= getIntent().getExtras();
        PoczatekRezerwacji=extras.getString("StartDate");
        KoniecRezerwacji=extras.getString("EndDate");
        BookingId=extras.getString("BookingId");
        GrupaProjektu=extras.getString("GrupaProjektu");
        NazwaProjektu=extras.getString("NrProjektu");
        NrProjektu=extras.getString("NrProjektu");


        switch_on=new ArrayList<>();
        switch1=(Switch) findViewById(R.id.switch1);
        switch2=(Switch) findViewById(R.id.switch2);
        switch3=(Switch) findViewById(R.id.switch3);





        ImageView zakoncz=(ImageView) findViewById(R.id.rezerwuj_bt);
        zakoncz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(switch1.isChecked()) switch_on.add(0);

                if(switch2.isChecked()) switch_on.add(1);

                if(switch3.isChecked()) switch_on.add(2);

                JSON_zakoncz_rezerwacje json_zakoncz_rezerwacje=new JSON_zakoncz_rezerwacje();
                json_zakoncz_rezerwacje.StartUpdate("test",BookingId,OcenaAuta_po.this);
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
}

