package com.example.carsharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.ImageView;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView im = (ImageView) findViewById(R.id.Loginbtn);
        final TextView error = (TextView)findViewById(R.id.errortxt);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Logi = ((EditText) findViewById(R.id.Logintxt)).getText().toString();
                String Haslo = ((EditText) findViewById(R.id.Passwordtxt)).getText().toString();
                if(Logi.equals("") || Haslo.equals(""))
                {

                    error.setText("Login i Hasło nie mogą być puste");
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else
                {
                    LoginJson logowanie = new LoginJson();
                    logowanie.StartUpdate(Logi,Haslo,getApplicationContext(),error);
                }
            }
        });


        }
}
