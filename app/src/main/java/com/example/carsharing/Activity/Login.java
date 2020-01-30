package com.example.carsharing.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.carsharing.DataHandler.LoginDataHandler;
import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.JSON.JSON_SendLog;
import com.example.carsharing.JSON.JSON_check_version;
import com.example.carsharing.JSON.JSON_lista_czas_powiadomien;
import com.example.carsharing.JSON.JSON_lista_parkingow;
import com.example.carsharing.JSON.JSON_Login;
import com.example.carsharing.Other.getNetworkType;
import com.example.carsharing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class Login extends AppCompatActivity {
    /** Poniżej wartość Wersji aplikacji - musi być zgodna z wartością dostępną w procedurze cs.pCheckVersion
     * w celu wypuszczenia nowej wersji oprogramowania zmieniamy poniższą wartosć na nową, aplikację generujemy i wystawiamy w nc.sng.com.pl
     * i w wyżej wspomnianej procedurze wprowadzamy nową wartosć w wskazane miejsce*/
String WersjaAplikacji = "Wersja Alfa6";


EditText Password;


    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //setContentView(R.layout.activity_login);
            final TextView error = (TextView) findViewById(R.id.errortxt);
            if (isConnected()) {
                error.setTextColor(0xFF00CC00);
                getNetworkType gnt = new getNetworkType();
                error.setGravity(Gravity.RIGHT);
                error.setText("sieć: " + gnt.getNetworkType(getApplicationContext()).toString());
            } else {
                error.setTextColor(0xFFFF0000);
                error.setText("Błąd połączenia, sprawdź połączenie z internetem");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(networkChangeReceiver);
    }

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!checkPermissions()) {
            setPermissions();
        }
//zmiana huberta

        Password=(EditText) findViewById(R.id.Passwordtxt);


//przypal na mnie
        if (isConnected()) {
try{
    TextView te = (TextView)findViewById(R.id.Versionerror);
JSON_check_version cv = new JSON_check_version();
cv.StartUpdate(WersjaAplikacji,te,this);
}catch (Exception e){
    Logs_DataHandler log = new Logs_DataHandler(this);
    log.inputLog("Login.class 000: " + e.toString());
    log.close();
}

        try {
            JSON_SendLog sendLog = new JSON_SendLog();
            sendLog.Send(this,WersjaAplikacji);
        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog("Login.class 001: " + e.toString());
            log.close();
        }
        try {
            JSON_lista_czas_powiadomien lis = new JSON_lista_czas_powiadomien();
            lis.StartUpdate(this);
        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog("Login.class 002: " + e.toString());
            log.close();
        }

        try {
            JSON_lista_parkingow lis = new JSON_lista_parkingow();
            lis.StartUpdate(this);
        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog("Login.class 003: " + e.toString());
            log.close();
        }


        try {
            if (getIntent().hasExtra("title")) {
                Bundle b = getIntent().getExtras();
                String someData = b.getString("title");
                String someData2 = b.getString("body");
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle(someData)
                        .setMessage(someData2)
                        .setIcon(R.drawable.confirm)
                        .setCancelable(true)
                        .show();
            }

        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog("Login.class 004: " + e.toString());
            log.close();

        }

        final TextView error = (TextView) findViewById(R.id.errortxt);
        final Context context = this;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        String tit = null;
        String bod = null;
        try {
            if (getIntent().getExtras() != null) {
                for (String key : getIntent().getExtras().keySet()) {
                    if (key.equals("t1")) {
                        tit = getIntent().getExtras().getString(key);
                    } else if (key.equals("b1")) {
                        bod = getIntent().getExtras().getString(key);
                    }
                }
            }
        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog("Login.class 005: " + e.toString());
            log.close();
        }

        if (isConnected()) {
            error.setTextColor(0xFF00CC00);
            getNetworkType gnt = new getNetworkType();
            error.setGravity(Gravity.RIGHT);
            error.setText("sieć: " + gnt.getNetworkType(this).toString());
        } else {
            error.setTextColor(0xFFFF0000);
            error.setText("Błąd połączenia, sprawdź połączenie z internetem");
        }

        try {
            LoginDataHandler LDH = new LoginDataHandler(this);
            Cursor getdata = LDH.getData();
            while (getdata.moveToNext()) {
                if (getdata.getString(3).matches("true")) {
                    ((EditText) findViewById(R.id.Logintxt)).setText(getdata.getString(1));
                    ((EditText) findViewById(R.id.Passwordtxt)).setText(getdata.getString(2));
                    ((CheckBox) findViewById(R.id.zapamietajchbox)).setChecked(true);


                }
            }
            LDH.close();
        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog("Login.class 006: " + e.toString());
            log.close();
        }
        final TextView poka = (TextView) findViewById(R.id.poka);
        poka.setVisibility(View.INVISIBLE);

            ((EditText) findViewById(R.id.Passwordtxt)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(((EditText) findViewById(R.id.Passwordtxt)).length()==0){
                        poka.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(((EditText) findViewById(R.id.Passwordtxt)).getText().length()==0){
                        poka.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(((EditText) findViewById(R.id.Passwordtxt)).getText().length()==0){
                        poka.setVisibility(View.VISIBLE);
                    }
                }
            });
        poka.setOnTouchListener(show_text);

        ImageView zaloguj = (ImageView) findViewById(R.id.Loginbtn);


        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog pg = new ProgressDialog(context);
                pg.setMessage("Wczytywanie...");
                pg.show();
                final String Logi = ((EditText) findViewById(R.id.Logintxt)).getText().toString();
                final String Haslo = ((EditText) findViewById(R.id.Passwordtxt)).getText().toString();
                if (Logi.equals("") || Haslo.equals("")) {
                    error.setTextColor(0xFFCC0000);
                    error.setGravity(Gravity.CENTER);
                    error.setText("Login i Hasło nie mogą być puste");
                    pg.hide();
                } else {
                    LoginDataHandler LDH = new LoginDataHandler(getApplicationContext());
                    LDH.dropdatabase();
                    LDH.inputDataTime(((CheckBox) findViewById(R.id.zapamietajchbox)).isChecked(), Logi, Haslo);
                    LDH.close();

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (task.isSuccessful()) {
                                        String token = task.getResult().getToken();
                                        JSON_Login logowanie = new JSON_Login();
                                        Log.d("token", token);
                                        logowanie.StartUpdate(Logi, Haslo, getApplicationContext(), error, token,pg,WersjaAplikacji);
                                    } else {
                                        JSON_Login logowanie = new JSON_Login();
                                    }
                                }
                            });
                }
            }
        });
    }else {TextView error = (TextView) findViewById(R.id.errortxt);
            w8(error);
        }

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
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
              if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SET_ALARM) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    private void setPermissions() {
        ActivityCompat.requestPermissions((Activity) this, new String[]{
                Manifest.permission.INTERNET , Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.VIBRATE ,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.SET_ALARM}, 1);
    }

    public void w8(final TextView error){
        try {
            error.setTextColor(0xFFFF0000);
            error.setText("Błąd połączenia, sprawdź połączenie z internetem");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isConnected()) {
                        finish();
                        startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                    else
                    {
                        w8(error);
                    }
                }
            }, 1000);

        }catch(Exception e){
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog("Login.class 007: " + e.toString());
            log.close();
        }
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private View.OnTouchListener show_text = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.poka) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return true;
            }
            return false;
        }
    };

}


