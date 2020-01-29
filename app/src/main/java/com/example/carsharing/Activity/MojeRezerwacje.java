package com.example.carsharing.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharing.JSON.JSON_anuluj_rezerwacje;
import com.example.carsharing.JSON.JSON_end_trip;
import com.example.carsharing.JSON.JSON_moje_rezerwacje;
import com.example.carsharing.JSON.JSON_moje_rezerwacje_new;
import com.example.carsharing.R;

public class MojeRezerwacje extends AppCompatActivity {
    public static String BookingId;
    public static String PoczatekRezerwacji;
    public static String KoniecRezerwacji;
    public static String Status;
    public static String GrupaProjektu;
    public static String NrProjektu;

    ImageView anuluj_rezerwacje_bt;
    ImageView zakoncz_rezerwacje_bt;

    ImageView lokalizacja_bt;

    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mojarezerwacja);

       Bundle extras= getIntent().getExtras();
       BookingId=extras.getString("BookingId");
       PoczatekRezerwacji=extras.getString("StartDate");
       KoniecRezerwacji=extras.getString("EndDate");
       Status=extras.getString("Status");
       GrupaProjektu=extras.getString("GrupaProjektu");
       NrProjektu=extras.getString("NrProjektu");


       lokalizacja_bt=(ImageView) findViewById(R.id.lokalizuj_bt);
       lokalizacja_bt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent mapIntent = new Intent(getApplicationContext(),Mapa.class);
               mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               mapIntent.putExtra("BookingId", BookingId);
               startActivity(mapIntent);
           }
       });


       zakoncz_rezerwacje_bt=(ImageView) findViewById(R.id.zakoncz_bt);


       ImageView rozpocznij_jazde_bt=(ImageView) findViewById(R.id.potwierdz_bt);

        if(Status.equals("0")|| Status.equals("2")){
            rozpocznij_jazde_bt.setImageResource(R.drawable.rozpocznijjazde);


        }else
        {
            rozpocznij_jazde_bt.setImageResource(R.drawable.zatrzymajjazd);

        }


       rozpocznij_jazde_bt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
/*
               Intent intent = new Intent(getApplicationContext(), RozpoczecieJazdy.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.putExtra("StartDate", PoczatekRezerwacji);
               intent.putExtra("EndDate", KoniecRezerwacji);
               intent.putExtra("Status",Status);
               intent.putExtra("BookingId", BookingId);
               intent.putExtra("GrupaProjektu", GrupaProjektu);
               intent.putExtra("NrProjektu", NrProjektu);
               startActivity(intent);/*


 */
               if(Status.equals("0") ){
                   Intent intent = new Intent(getApplicationContext(), OcenaAuta.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   intent.putExtra("StartDate", PoczatekRezerwacji);
                   intent.putExtra("EndDate", KoniecRezerwacji);
                   intent.putExtra("BookingId", BookingId);
                   intent.putExtra("GrupaProjektu", GrupaProjektu);
                   intent.putExtra("NrProjektu", NrProjektu);
                   startActivity(intent);
               }
               else if ( Status.equals("2")) {
                       Intent intent = new Intent(getApplicationContext(), RozpoczecieJazdy.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.putExtra("StartDate", PoczatekRezerwacji);
                       intent.putExtra("EndDate", KoniecRezerwacji);
                       intent.putExtra("BookingId", BookingId);
                       intent.putExtra("GrupaProjektu", GrupaProjektu);
                       intent.putExtra("NrProjektu", NrProjektu);
                       startActivity(intent);
                   } else if (Status.equals("1")) {
                       JSON_end_trip json_end_trip = new JSON_end_trip();
                       json_end_trip.StartUpdate(BookingId, MojeRezerwacje.this);
                   }


               }

       });


       ImageView zmien_czas_bt=(ImageView) findViewById(R.id.zmiana_czasu_bt);
       zmien_czas_bt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(getApplicationContext(), ZmienCzasRezerwacji.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.putExtra("StartDate",PoczatekRezerwacji);
               intent.putExtra("EndDate",KoniecRezerwacji);
               intent.putExtra("BookingId",BookingId);
               startActivity(intent);

           }


       });


        ImageView back_bt = (ImageView) findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                JSON_moje_rezerwacje_new json_moje_rezerwacje_new=new JSON_moje_rezerwacje_new();
                json_moje_rezerwacje_new.StartUpdate(MojeRezerwacje.this);
                //JSON_moje_rezerwacje json_moje_rezerwacje=new JSON_moje_rezerwacje();
                //json_moje_rezerwacje.StartUpdate("","",MojeRezerwacje.this);
              //  finish();
            }
        });

        anuluj_rezerwacje_bt=(ImageView) findViewById(R.id.anuluj_bt);
        anuluj_rezerwacje_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog=new AlertDialog.Builder(MojeRezerwacje.this)
                        .setTitle("Rezerwacja")
                        .setMessage("Czy napewno chcesz anulować rezerwację?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                JSON_anuluj_rezerwacje json_anuluj_rezerwacje=new JSON_anuluj_rezerwacje();
                                json_anuluj_rezerwacje.StartUpdate(BookingId,MojeRezerwacje.this);
                            }
                        })
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        zakoncz_rezerwacje_bt=(ImageView) findViewById(R.id.zakoncz_bt);
        zakoncz_rezerwacje_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog=new AlertDialog.Builder(MojeRezerwacje.this)
                        .setTitle("Rezerwacja")
                        .setMessage("Czy napewno chcesz zakończyć rezerwację?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), OcenaAuta_po.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("StartDate", PoczatekRezerwacji);
                                intent.putExtra("EndDate", KoniecRezerwacji);
                                intent.putExtra("BookingId", BookingId);
                                intent.putExtra("GrupaProjektu", GrupaProjektu);
                                intent.putExtra("NrProjektu", NrProjektu);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        if(!(Status.equals("0")))
        {
            anuluj_rezerwacje_bt.setVisibility(View.GONE);
            zakoncz_rezerwacje_bt.setVisibility(View.VISIBLE);
        }




    }

    public void powrot_do_rezerwacji(){
        //finish();
        //JSON_moje_rezerwacje json_moje_rezerwacje=new JSON_moje_rezerwacje();
        //json_moje_rezerwacje.StartUpdate("","",MojeRezerwacje.this);
        JSON_moje_rezerwacje_new json_moje_rezerwacje_new=new JSON_moje_rezerwacje_new();
        json_moje_rezerwacje_new.StartUpdate(MojeRezerwacje.this);
    }

}
