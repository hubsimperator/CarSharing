package com.example.carsharing.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.R;
import com.example.carsharing.TakePhoto;

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

    public static TextView note0;

    public static TextView photo0;

    public String notatka;

    AlertDialog alertDialog;

    EditText note01;

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

        note0=(TextView) findViewById(R.id.addnote0);
        photo0=(TextView) findViewById(R.id.addphoto0);


/*
        photo0.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                TakePhoto tp=new TakePhoto();
                tp.TakePhoto(OcenaAuta.this,"","","");
            }
        });


        note0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OcenaAuta.this)
                        .setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();

                            }
                        })
                        .setNegativeButton("Wybierz", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            notatka=note01.getText().toString();
                            note0.setText("Notatka");
                            }
                        });

                LayoutInflater inflater = (LayoutInflater)   OcenaAuta.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.notatka,null);


                note01=view.findViewById(R.id.notatka);
                if(notatka!=null) {
                    note01.setText(notatka);
                }
                dialogBuilder.setView(view);

                alertDialog =dialogBuilder.create();
                alertDialog.show();
            }
        });
*/



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

