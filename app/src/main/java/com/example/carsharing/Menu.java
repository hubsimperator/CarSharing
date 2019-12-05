package com.example.carsharing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        ImageView mojerezerwacje_bt = (ImageView) findViewById(R.id.moje_rezerwacje_bt);
        mojerezerwacje_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this,"Brak zaplanowanych rezerwacji",Toast.LENGTH_LONG).show();
                /*
                    Intent intent = new Intent(getApplicationContext(), MojeRezerwacje.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                 */
                }
    });

        ImageView rezerwacja_bt = (ImageView) findViewById(R.id.zarezerwuj_bt);
        rezerwacja_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Rezerwacja.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

}}


