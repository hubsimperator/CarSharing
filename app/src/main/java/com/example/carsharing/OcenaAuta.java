package com.example.carsharing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class OcenaAuta extends AppCompatActivity {

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
        setContentView(R.layout.ocen_auto_przed);


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





        ImageView rozpocznij_jazde=(ImageView) findViewById(R.id.rezerwuj_bt);
        rozpocznij_jazde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(switch1.isChecked()) switch_on.add(0);

                if(switch2.isChecked()) switch_on.add(1);

                if(switch3.isChecked()) switch_on.add(2);


                Intent intent = new Intent(getApplicationContext(), RozpoczecieJazdy.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("StartDate", PoczatekRezerwacji);
                intent.putExtra("EndDate", KoniecRezerwacji);
                intent.putExtra("BookingId", BookingId);
                intent.putExtra("GrupaProjektu", GrupaProjektu);
                intent.putExtra("NrProjektu", NrProjektu);
                startActivity(intent);
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

