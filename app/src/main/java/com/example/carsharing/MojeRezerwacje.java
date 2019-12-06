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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mojarezerwacja);

        try {
            MojeRezerwacjeDataHandler RezerwacjeDH = new MojeRezerwacjeDataHandler(MojeRezerwacje.this);
            Log.d("BAZA","UDALO SIE");
            /*
            Cursor getdata = RezerwacjeDH.getData();
            while (getdata.moveToNext()) {
                if(getdata.getString(3).matches("true"))
                {
                   // ((EditText) findViewById(R.id.Logintxt)).setText(getdata.getString(1));
                    //((EditText) findViewById(R.id.Passwordtxt)).setText(getdata.getString(2));
                    //((CheckBox) findViewById(R.id.zapamietajchbox)).setChecked(true);
                }
            }
            RezerwacjeDH.close();

            */
        } catch (Exception e){
            Log.d("BAZA","NIE UDALO SIE");

            e.printStackTrace();
        }

        ImageView back_bt = (ImageView) findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
