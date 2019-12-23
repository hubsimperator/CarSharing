package com.example.carsharing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class Menu extends AppCompatActivity implements LocationListener {
    AlertDialog alertDialog;
    Location mlocation;
    Criteria criteria;
    LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        final Looper looper = null;
        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mlocation = location;
                LatLng myCoord;
                if(mlocation==null){
                    myCoord=new LatLng(54.215, 18.627798);
                }else{
                    myCoord= new LatLng(mlocation.getLatitude(),mlocation.getLongitude());
                }


                GeoProcessing geoProcessing=new GeoProcessing();
                geoProcessing.setNearestParking(myCoord,Menu.this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Status Changed", String.valueOf(status));
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Provider Enabled", provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Provider Disabled", provider);
            }
        };

        final LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);




            locationManager.requestSingleUpdate(criteria, locationListener, looper);





        ImageView koszty_bt=(ImageView) findViewById(R.id.koszty_bt);
        koszty_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),QRScanner.class));
            }
        });

        ImageView mojerezerwacje_bt = (ImageView) findViewById(R.id.zmien_czas_bt);
        mojerezerwacje_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        ImageView telefon_bt=(ImageView) findViewById(R.id.telefon_bt);
        telefon_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(Menu.this)
                        .setTitle("Połączenie")
                        .setMessage("Czy napewno chcesz zadzwonić do Dyspozytora?")
                        .setPositiveButton("Zadzwoń", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:"+"692591846"));
                                startActivity(callIntent);
                            }
                        })
                        .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();




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





