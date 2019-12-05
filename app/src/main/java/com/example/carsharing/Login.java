package com.example.carsharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
        if(!checkPermissions()){
            setPermissions();
        }
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
    private boolean checkPermissions() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
    private void setPermissions() {
        ActivityCompat.requestPermissions((Activity) this, new String[]{
                Manifest.permission.INTERNET , Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE  }, 1);
    }
}
