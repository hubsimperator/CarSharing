package com.example.carsharing;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MojeRezerwacje extends AppCompatActivity {
    public static String BookingId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mojarezerwacja);

       Bundle extras= getIntent().getExtras();
       BookingId=extras.getString("BookingId");

       String s1=extras.getString("BookingId");
        String s21= extras.getString("StartDate");
        String s31= extras.getString("EndDate");
        String s14=extras.getString("Status");


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

        ImageView anuluj_rezerwacje_bt=(ImageView) findViewById(R.id.anuluj_bt);
        anuluj_rezerwacje_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSON_anuluj_rezerwacje json_anuluj_rezerwacje=new JSON_anuluj_rezerwacje();
                json_anuluj_rezerwacje.StartUpdate(BookingId,MojeRezerwacje.this);
            }
        });

        ImageView potwierdz = (ImageView) findViewById(R.id.moje_rezerwacje_bt);
        potwierdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),ZmianaCzasu.class);


            startActivity(intent);
            }
        });

    }
}
