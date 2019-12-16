package com.example.carsharing;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        ImageView koszty_bt=(ImageView) findViewById(R.id.koszty_bt);
        koszty_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                try {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                    startActivityForResult(intent, 0);

                } catch (Exception e) {

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);

                }

                 */

                startActivity(new Intent(getApplicationContext(),QRScanner.class));
            }
        });

        ImageView mojerezerwacje_bt = (ImageView) findViewById(R.id.zmien_czas_bt);
        mojerezerwacje_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   // Intent intent = new Intent(getApplicationContext(), ListaRezerwacji.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //startActivity(intent);
                JSON_moje_rezerwacje json_moje_rezerwacje=new JSON_moje_rezerwacje();
                json_moje_rezerwacje.StartUpdate("","",Menu.this);
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

}

    public static int backButtonCount;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed()
    {
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("Zegar","seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                backButtonCount=1;
            }

            public void onFinish() {
                Log.d("Zegar","done");
                backButtonCount=0;
            }

        }.start();
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "W celu zamknięcia aplikacji naćiśnij POWRÓT jeszcze raz", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }

}





