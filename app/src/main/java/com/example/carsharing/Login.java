package com.example.carsharing;
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
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class Login extends AppCompatActivity implements LocationListener {
    LocationManager mlocation;
    Criteria criteria;
    LocationManager mLocationManager;


    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //setContentView(R.layout.activity_login);
            final TextView error = (TextView)findViewById(R.id.errortxt);
            if(isConnected()){
                error.setTextColor(0xFF00CC00);
                getNetworkType gnt = new getNetworkType();
                error.setGravity(Gravity.RIGHT);
                error.setText("sieć: "+ gnt.getNetworkType(getApplicationContext()).toString());
            }
            else{
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
        try{
            JSON_lista_czas_powiadomien lis = new JSON_lista_czas_powiadomien();
            lis.StartUpdate(this);
        }catch(Exception e){

        }

        try{
            JSON_lista_parkingow lis = new JSON_lista_parkingow();
            lis.StartUpdate(this);
        }catch(Exception e){

        }


     try {
         Bundle b = getIntent().getExtras();
         String someData = b.getString("title");
         String someData2 = b.getString("body");
         AlertDialog alertDialog = new AlertDialog.Builder(this)
                 .setTitle(someData)
                 .setMessage(someData2)
                 .setIcon(R.drawable.confirm)
                 .setCancelable(true)
                 .show();


     }catch (NullPointerException ne){
         Log.d("Firebejz", "Nie ma nic");

     }


        if(!checkPermissions()){
            setPermissions();
        }





        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }


        LatLng myCoord;

        if(location==null){
            myCoord=new LatLng(54.357912, 18.656533);
        }else{
             myCoord= new LatLng(location.getLatitude(),location.getLongitude());
        }
        GeoProcessing geoProcessing=new GeoProcessing();
        geoProcessing.setNearestParking(myCoord,Login.this);



        final TextView error = (TextView)findViewById(R.id.errortxt);
        final Context context = this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        String tit = null;
        String bod = null;
        if(getIntent().getExtras()!=null)
        {
            for(String key : getIntent().getExtras().keySet())
            {
                if(key.equals("t1")){
                   tit = getIntent().getExtras().getString(key);
                }
                else if(key.equals("b1")){
                    bod = getIntent().getExtras().getString(key);
                }
            }
        }

        if(isConnected()){
            error.setTextColor(0xFF00CC00);
            getNetworkType gnt = new getNetworkType();
            error.setGravity(Gravity.RIGHT);
            error.setText("sieć: "+ gnt.getNetworkType(this).toString());
        }
        else{
            error.setTextColor(0xFFFF0000);
            error.setText("Błąd połączenia, sprawdź połączenie z internetem");
        }

        try {
            LoginDataHandler LDH = new LoginDataHandler(this);
            Cursor getdata = LDH.getData();
            while (getdata.moveToNext()) {
                if(getdata.getString(3).matches("true"))
                {
                    ((EditText) findViewById(R.id.Logintxt)).setText(getdata.getString(1));
                    ((EditText) findViewById(R.id.Passwordtxt)).setText(getdata.getString(2));
                    ((CheckBox) findViewById(R.id.zapamietajchbox)).setChecked(true);
                }
            }
            LDH.close();
        } catch (Exception e){
            e.printStackTrace();
        }


        ImageView zaloguj = (ImageView) findViewById(R.id.Loginbtn);

        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ProgressDialog pg = new ProgressDialog(context);
                pg.setMessage("Wczytywanie...");
                pg.show();
                final String Logi = ((EditText) findViewById(R.id.Logintxt)).getText().toString();
                final String Haslo = ((EditText) findViewById(R.id.Passwordtxt)).getText().toString();
                if(Logi.equals("") || Haslo.equals(""))
                {
                    error.setTextColor(0xFFCC0000);
                    error.setGravity(Gravity.CENTER);
                    error.setText("Login i Hasło nie mogą być puste");
                    pg.hide();
                }
                else
                {
                        LoginDataHandler LDH = new LoginDataHandler(getApplicationContext());
                        LDH.dropdatabase();
                        LDH.inputDataTime(((CheckBox) findViewById(R.id.zapamietajchbox)).isChecked(), Logi, Haslo);
                        LDH.close();

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if(task.isSuccessful()){
                                        String token = task.getResult().getToken();
                                        JSON_Login logowanie = new JSON_Login();
                                        Log.d("token",token + " 2");
                                        logowanie.StartUpdate(Logi,Haslo,getApplicationContext(),error,token);
                                    }else
                                    {
                                        JSON_Login logowanie = new JSON_Login();
                                    }
                                }
                            });
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


        return true;
    }

    private void setPermissions() {
        ActivityCompat.requestPermissions((Activity) this, new String[]{
                Manifest.permission.INTERNET , Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.VIBRATE ,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CALL_PHONE}, 1);
    }
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}


