package com.example.carsharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class Login extends AppCompatActivity implements View.OnClickListener{
    View view;
    Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        con = getApplicationContext();
        LayoutInflater inflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.activity_login,null);

        ImageView Zaloguj = (ImageView) view.findViewById(R.id.Loginbtn);
        Zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tag","klik");


            }
        });

    }

    @Override
    public void onClick(View v) {
        final TextView Login = (TextView) view.findViewById(R.id.Logintxt);
        final TextView Haslo = (TextView) view.findViewById(R.id.Logintxt);
        if(Login.getText().equals(null) || Haslo.getText().equals(null))
        {
            TextView error = (TextView)view.findViewById(R.id.errortxt);
            error.setText("Login i Hasło nie mogą być puste");
        }
        else
        {
            Intent intent = new Intent(con, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            con.startActivity(intent);
        }
    }
}
