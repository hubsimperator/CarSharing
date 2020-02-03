package com.example.carsharing.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.JSON.JSON_moje_rezerwacje_new;
import com.example.carsharing.JSON.JSON_ocen_auto;
import com.example.carsharing.R;
import com.example.carsharing.TakePhoto_new;

import java.util.ArrayList;

public class OcenaAuta extends AppCompatActivity {

    public static String BookingId;
    public static String GrupaProjektu;
    public static String NazwaProjektu;
    public static String NrProjektu;

    public static Switch switch1;
    public static Switch switch2;
    public static Switch switch3;

    public static TextView note0;
    public static TextView note1;
    public static TextView note2;


    public static TextView photo0;
    public static TextView photo1;
    public static TextView photo2;

    ImageView rozpocznij_jazde;

    public String notatka="";
    public String notatka1="";
    public String notatka2="";

    public static Context con;

    AlertDialog alertDialog;

    EditText note01;

    ArrayList<Integer> switch_on;
   public static ArrayList<Integer> switch_off;
    public static ArrayList<String> note_on;

    ArrayList<Integer> phote_on;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            TakePhoto_new tp = new TakePhoto_new();
            tp.activityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog("OcenaAuta.class 001: " + e.toString());
            log.close();
        }
    }
    public void setBlobImage(ArrayList<String> _bloblist,ArrayList<Integer> _bloblist_size,ArrayList<String> _bloblist_name){
        try {
            JSON_ocen_auto json_ocen_auto = new JSON_ocen_auto();
            json_ocen_auto.StartUpdate(BookingId, switch_off, note_on, _bloblist, _bloblist_size, _bloblist_name, con);
        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog("OcenaAuta.class 002: " + e.toString());
            log.close();
        }
    }

    public void setphoto(Integer _param,Integer _podparam){
        switch (_podparam){
            case 0:
            {
                if(_param==0) {
                    photo0.setText("Zdjęcie");
                }
                else{
                    photo0.setText("+ Dodaj zdjęcie");

                }
                break;
            }
            case 1:
            {
                if(_param==0) {
                    photo1.setText("Zdjęcie");
                }
                else{
                    photo1.setText("+ Dodaj zdjęcie");

                }
                break;
            }
            case 2:
            {
                if(_param==0) {
                    photo2.setText("Zdjęcie");
                }
                else{
                    photo2.setText("+ Dodaj zdjęcie");

                }
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocen_auto_przed);

        Bundle extras= getIntent().getExtras();
        BookingId=extras.getString("BookingId");
        GrupaProjektu=extras.getString("GrupaProjektu");
        NazwaProjektu=extras.getString("NrProjektu");
        NrProjektu=extras.getString("NrProjektu");

        switch_off=new ArrayList<>();
        phote_on=new ArrayList<>();
        switch1=(Switch) findViewById(R.id.switch1);
        switch2=(Switch) findViewById(R.id.switch2);
        switch3=(Switch) findViewById(R.id.switch3);
        note0=(TextView) findViewById(R.id.addnote0);
        photo0=(TextView) findViewById(R.id.addphoto0);
        note1=(TextView) findViewById(R.id.addnote1);
        photo1=(TextView) findViewById(R.id.addphoto1);
        note2=(TextView) findViewById(R.id.addnote2);
        photo2=(TextView) findViewById(R.id.addphoto2);


        photo0.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                TakePhoto_new tp = new TakePhoto_new();
                if(photo0.getText().equals("Zdjęcie")){
                    tp.showPicOrTakeNew(0,OcenaAuta.this);
                }else {
                    tp.TakePhoto(OcenaAuta.this, 0, "", "");
                }
            }
        });
        photo1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                TakePhoto_new tp = new TakePhoto_new();

                if(photo1.getText().equals("Zdjęcie")){
                    tp.showPicOrTakeNew(1,OcenaAuta.this);
                }else {
                    tp.TakePhoto(OcenaAuta.this, 1, "", "");
                }
            }
        });
        photo2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                TakePhoto_new tp = new TakePhoto_new();

                if(photo2.getText().equals("Zdjęcie")){
                    tp.showPicOrTakeNew(2,OcenaAuta.this);
                }else {
                    tp.TakePhoto(OcenaAuta.this, 2, "", "");
                }
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


        note1.setOnClickListener(new View.OnClickListener() {
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
                                notatka1=note01.getText().toString();
                                note1.setText("Notatka");
                            }
                        });

                LayoutInflater inflater = (LayoutInflater)   OcenaAuta.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.notatka,null);

                note01=view.findViewById(R.id.notatka);
                if(notatka1!=null) {
                    note01.setText(notatka1);
                }
                dialogBuilder.setView(view);

                alertDialog =dialogBuilder.create();
                alertDialog.show();
            }
        });

        note2.setOnClickListener(new View.OnClickListener() {
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
                                notatka2=note01.getText().toString();
                                note2.setText("Notatka");
                            }
                        });

                LayoutInflater inflater = (LayoutInflater)   OcenaAuta.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.notatka,null);


                note01=view.findViewById(R.id.notatka);
                if(notatka2!=null) {
                    note01.setText(notatka2);
                }
                dialogBuilder.setView(view);

                alertDialog =dialogBuilder.create();
                alertDialog.show();
            }
        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.opcjesw1);

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    linearLayout.setVisibility(View.INVISIBLE);

                }else{
                    linearLayout.setVisibility(View.VISIBLE);

                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.opcjesw2);

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    linearLayout.setVisibility(View.INVISIBLE);

                }else{
                    linearLayout.setVisibility(View.VISIBLE);

                }
            }
        });

        rozpocznij_jazde=(ImageView) findViewById(R.id.rezerwuj_bt);
        rozpocznij_jazde.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                switch_on=new ArrayList<>();
                rozpocznij_jazde.setClickable(false);
                note_on=new ArrayList<>();
                if(switch1.isChecked()) switch_on.add(0);
                else switch_off.add(3);
                if(switch2.isChecked()) switch_on.add(1);
                else switch_off.add(4);
                note_on.add(notatka);
                note_on.add(notatka1);
                //note_on.add(notatka2);

                if(photo0.getText().equals("Zdjęcie")) phote_on.add(0);
                if(photo1.getText().equals("Zdjęcie")) phote_on.add(1);
                if(photo2.getText().equals("Zdjęcie")) phote_on.add(2);

                //czy wysyłać
                if((switch_on.contains(0)) && (switch_on.contains(1))){
                    //w przypadku braku zastrzezen
                    alertDialog = new AlertDialog.Builder(OcenaAuta.this)
                            .setTitle("Potwierdzenie ")
                            .setMessage("Rozpoczęto jazdę, brak zastrzeżeń do stanu pojazdu")
                            .setIcon(R.drawable.confirm)
                            .setCancelable(true)
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    finish();
                                    JSON_moje_rezerwacje_new json_moje_rezerwacje_new=new JSON_moje_rezerwacje_new();
                                    json_moje_rezerwacje_new.StartUpdate(OcenaAuta.this);
                                }
                            })
                            .show();
                }else{//są zastrzezenia

                    boolean a= (!(switch_on.contains(0))) && (((notatka.length()==0) && (!(photo0.getText().equals("Zdjęcie")))));
                    boolean b=(!(switch_on.contains(1))) &&(((notatka1.length()==0) && (!(photo1.getText().equals("Zdjęcie")))));
                    boolean c=(a || b);
                    Log.d("Tag","Przejdz dalej");

                    if(c){
                        rozpocznij_jazde.setClickable(true);
                    alertDialog = new AlertDialog.Builder(OcenaAuta.this)
                                .setTitle("Błąd ")
                                .setMessage("Stan samochodu został oceniony negatywnie. Opisz usterkę/stan pojazdu lub zrób zdjęcie")
                                .setIcon(R.drawable.cancel)
                                .setCancelable(true)
                                .show();
                    rozpocznij_jazde.setClickable(true);
                    }else {//pola wypelnione

                        Log.d("Tag","Przejdz dalej");
                        con=OcenaAuta.this;
                        TakePhoto_new tp = new TakePhoto_new();
                        tp.sendToEncode(phote_on);
                    }
                }
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

    public void startTrip(String result){
        Toast.makeText(con, "Jazda rozpoczęta", Toast.LENGTH_LONG).show();
        finish();
        JSON_moje_rezerwacje_new json_moje_rezerwacje_new=new JSON_moje_rezerwacje_new();
        json_moje_rezerwacje_new.StartUpdate(con);
    }
}
