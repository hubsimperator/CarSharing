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
            RezerwacjeDH.inputData("Test 1","2019-12-06 12:00:00","Moja tojota");
            RezerwacjeDH.inputData("Test 2","2019-12-07 12:00:00","Daniela mazda");
            RezerwacjeDH.inputData("Test 3","2019-12-08 12:00:00","Jacka rower");
*/


            Cursor getdata = RezerwacjeDH.getData();
          Log.d("Dane z tabeli","Proba pobrania");
            while (getdata.moveToNext()) {
                Log.d("Dane z tabeli",getdata.getString(1)+" "+getdata.getString(2)+" "+getdata.getString(3));
                   // ((EditText) findViewById(R.id.Logintxt)).setText(getdata.getString(1));
                    //((EditText) findViewById(R.id.Passwordtxt)).setText(getdata.getString(2));
                    //((CheckBox) findViewById(R.id.zapamietajchbox)).setChecked(true);

            }
            RezerwacjeDH.close();


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
