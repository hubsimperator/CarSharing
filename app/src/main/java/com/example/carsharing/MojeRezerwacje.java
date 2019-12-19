package com.example.carsharing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class MojeRezerwacje extends AppCompatActivity {
    public static String BookingId;
    public static String PoczatekRezerwacji;
    public static String KoniecRezerwacji;
    public static String Status;

    ImageView anuluj_rezerwacje_bt;
    ImageView zakoncz_rezerwacje_bt;

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
               if(Status.equals("0") || Status.equals("2")) {
                   Intent intent = new Intent(getApplicationContext(), RozpoczecieJazdy.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   intent.putExtra("StartDate", PoczatekRezerwacji);
                   intent.putExtra("EndDate", KoniecRezerwacji);
                   intent.putExtra("BookingId", BookingId);
                   startActivity(intent);
               }
               else if(Status.equals("1")) {
                   JSON_end_trip json_end_trip=new JSON_end_trip();
                   json_end_trip.StartUpdate(BookingId,MojeRezerwacje.this);
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
                JSON_moje_rezerwacje json_moje_rezerwacje=new JSON_moje_rezerwacje();
                json_moje_rezerwacje.StartUpdate("","",MojeRezerwacje.this);
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
                                JSON_zakoncz_rezerwacje json_zakoncz_rezerwacje=new JSON_zakoncz_rezerwacje();
                                json_zakoncz_rezerwacje.StartUpdate("test",BookingId,MojeRezerwacje.this);
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
        JSON_moje_rezerwacje json_moje_rezerwacje=new JSON_moje_rezerwacje();
        json_moje_rezerwacje.StartUpdate("","",MojeRezerwacje.this);
    }

}
