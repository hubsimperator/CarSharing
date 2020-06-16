package com.example.carsharing.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharing.JSON.JSON_anuluj_rezerwacje;
import com.example.carsharing.JSON.JSON_close_car;
import com.example.carsharing.JSON.JSON_end_trip;
import com.example.carsharing.JSON.JSON_moje_rezerwacje;
import com.example.carsharing.JSON.JSON_moje_rezerwacje_new;
import com.example.carsharing.JSON.JSON_open_car;
import com.example.carsharing.JSON.JSON_zakoncz_rezerwacje;
import com.example.carsharing.Other.QRScanner;
import com.example.carsharing.R;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.Duration;

public class MojeRezerwacje extends AppCompatActivity {
    public static String BookingId;
    public static String PoczatekRezerwacji;
    public static String KoniecRezerwacji;
    public static String Status;
    public static String GrupaProjektu;
    public static String NrProjektu;
    public static String EitResource;

    public static Integer CZAS_DO_ROZPOCZECIA=15;

    //test

    LatLng auto=new LatLng(54.358085, 18.656375);
    Location autoLocation=new Location(LocationManager.GPS_PROVIDER);
    //

    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;

    ImageView anuluj_rezerwacje_bt;
    ImageView zakoncz_rezerwacje_bt;

    ImageView lokalizacja_bt;

    ImageView openCar_bt;
    ImageView closeCar_bt;

    TextView infoAboutDistance_tv;
    TextView infoAboutGPS_tv;

    Button QRScanner_bt;

    AlertDialog alertDialog;

    @Override
    protected void onPause() {
        super.onPause();
        try {
            locationMangaer.removeUpdates(locationListener);
        }catch (NullPointerException ne){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            locationMangaer.removeUpdates(locationListener);
        }catch (NullPointerException ne){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   locationEnabled();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mojarezerwacja);

        Bundle extras = getIntent().getExtras();
        BookingId = extras.getString("BookingId");
        PoczatekRezerwacji = extras.getString("StartDate");
        KoniecRezerwacji = extras.getString("EndDate");
        Status = extras.getString("Status");
        GrupaProjektu = extras.getString("GrupaProjektu");
        NrProjektu = extras.getString("NrProjektu");
        EitResource = extras.getString("EitResource");

        long minutes=9999;


        autoLocation.setLatitude(54.357906);
        autoLocation.setLongitude(18.656125);

        lokalizacja_bt = (ImageView) findViewById(R.id.lokalizuj_bt);
        lokalizacja_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getApplicationContext(), Mapa.class);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mapIntent.putExtra("BookingId", BookingId);
                startActivity(mapIntent);
            }
        });

        closeCar_bt = (ImageView) findViewById(R.id.closeCar_bt);
        closeCar_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSON_close_car json_close_car = new JSON_close_car();
                json_close_car.StartUpdate(EitResource,BookingId, MojeRezerwacje.this);
            }
        });

        openCar_bt = (ImageView) findViewById(R.id.openCar_bt);
        openCar_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSON_open_car json_open_car = new JSON_open_car();
                json_open_car.StartUpdate(EitResource,BookingId, MojeRezerwacje.this);
            }
        });

        zakoncz_rezerwacje_bt = (ImageView) findViewById(R.id.zakoncz_bt);


        ImageView rozpocznij_jazde_bt = (ImageView) findViewById(R.id.potwierdz_bt);

        if (Status.equals("0") || Status.equals("2")) {
            rozpocznij_jazde_bt.setImageResource(R.drawable.rozpocznijjazde);


        } else {
            rozpocznij_jazde_bt.setImageResource(R.drawable.zatrzymajjazd);

        }


        rozpocznij_jazde_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Status.equals("1")) {
                    JSON_end_trip json_end_trip = new JSON_end_trip();
                    json_end_trip.StartUpdate(BookingId, MojeRezerwacje.this);
                } else {
                    Intent intent = new Intent(getApplicationContext(), RozpoczecieJazdy.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("StartDate", PoczatekRezerwacji);
                    intent.putExtra("EndDate", KoniecRezerwacji);
                    intent.putExtra("Status", Status);
                    intent.putExtra("BookingId", BookingId);
                    intent.putExtra("GrupaProjektu", GrupaProjektu);
                    intent.putExtra("NrProjektu", NrProjektu);
                    startActivity(intent);
                }

            }

        });


        ImageView zmien_czas_bt = (ImageView) findViewById(R.id.zmiana_czasu_bt);
        zmien_czas_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ZmienCzasRezerwacji.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("StartDate", PoczatekRezerwacji);
                intent.putExtra("EndDate", KoniecRezerwacji);
                intent.putExtra("BookingId", BookingId);
                startActivity(intent);

            }


        });


        ImageView back_bt = (ImageView) findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                JSON_moje_rezerwacje_new json_moje_rezerwacje_new = new JSON_moje_rezerwacje_new();
                json_moje_rezerwacje_new.StartUpdate(MojeRezerwacje.this);
                //JSON_moje_rezerwacje json_moje_rezerwacje=new JSON_moje_rezerwacje();
                //json_moje_rezerwacje.StartUpdate("","",MojeRezerwacje.this);
                //  finish();
            }
        });

        anuluj_rezerwacje_bt = (ImageView) findViewById(R.id.anuluj_bt);
        anuluj_rezerwacje_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog = new AlertDialog.Builder(MojeRezerwacje.this)
                        .setTitle("Rezerwacja")
                        .setMessage("Czy napewno chcesz anulować rezerwację?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                JSON_anuluj_rezerwacje json_anuluj_rezerwacje = new JSON_anuluj_rezerwacje();
                                json_anuluj_rezerwacje.StartUpdate(BookingId, MojeRezerwacje.this);
                            }
                        })
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        QRScanner_bt =(Button) findViewById(R.id.qrscanner_bt);
        QRScanner_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QRScanner.class));

            }
        });

        zakoncz_rezerwacje_bt = (ImageView) findViewById(R.id.zakoncz_bt);
        zakoncz_rezerwacje_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog = new AlertDialog.Builder(MojeRezerwacje.this)
                        .setTitle("Rezerwacja")
                        .setMessage("Czy napewno chcesz zakończyć rezerwację?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            /*
                                Intent intent = new Intent(getApplicationContext(), OcenaAuta_po.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("StartDate", PoczatekRezerwacji);
                                intent.putExtra("EndDate", KoniecRezerwacji);
                                intent.putExtra("BookingId", BookingId);
                                intent.putExtra("GrupaProjektu", GrupaProjektu);
                                intent.putExtra("NrProjektu", NrProjektu);
                                startActivity(intent);
                             */
                                JSON_zakoncz_rezerwacje json_zakoncz_rezerwacje = new JSON_zakoncz_rezerwacje();
                                json_zakoncz_rezerwacje.StartUpdate("test", BookingId, MojeRezerwacje.this);
                            }
                        })
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        if (!(Status.equals("0"))) {
            anuluj_rezerwacje_bt.setVisibility(View.GONE);
            zakoncz_rezerwacje_bt.setVisibility(View.VISIBLE);
        }

        infoAboutDistance_tv=(TextView) findViewById(R.id.infoAboutDistance_tv);

        infoAboutGPS_tv=(TextView) findViewById(R.id.infoAboutGPS_tv);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
        try {
            Date date_poczatek = format.parse(PoczatekRezerwacji);
            //    Date date_obecna= format(currentTime);
            long diff;
            diff = date_poczatek.getTime() - currentTime.getTime();
            long seconds = diff / 1000;
            minutes = seconds / 60;
            Log.d("Lokalizacja","aa");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        openCar_bt.setVisibility(View.VISIBLE);
        closeCar_bt.setVisibility(View.VISIBLE);
        //komentuje
        /*
        //Sprawdza warunek zdefiniowanego czasu do rozpoczecia  rezerwacji
        if(minutes > CZAS_DO_ROZPOCZECIA){
            Toast.makeText(MojeRezerwacje.this,"TEST",Toast.LENGTH_SHORT);
            infoAboutGPS_tv.setVisibility(View.VISIBLE);
            //openCar_bt.setVisibility(View.VISIBLE);
            //closeCar_bt.setVisibility(View.VISIBLE);
            locationEnabled();

        }else{
            infoAboutGPS_tv.setVisibility(View.GONE);
          //  openCar_bt.setVisibility(View.GONE);
            //closeCar_bt.setVisibility(View.GONE);
        }

         */

    }




    private void locationEnabled() {
        LocationManager lm = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled) {
            new AlertDialog.Builder(MojeRezerwacje.this)
                    .setMessage("GPS w telefonie jest nieaktywny. Czy chcesz go włączyć ?")
                    .setPositiveButton("Tak", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("Nie", null)
                    .show();

        } else {

            locationMangaer = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);

            locationListener = new MyLocationListener();

            locationMangaer.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 10000, 1, locationListener);

            try {

                Location l=  locationMangaer.getLastKnownLocation(LocationManager
                        .GPS_PROVIDER);

            //    CheckDistance(l,autoLocation);
            }catch (NullPointerException ne){
                Log.d("Lokalizacja","Błąd");
            }


        }
    }

    public boolean CheckDistance(Location userLocation,Location carLocation){
    final double DISTANCE_TO_CAR=25;

    double distance_between_objects=userLocation.distanceTo(carLocation);
        Toast.makeText(MojeRezerwacje.this,"Odleglosc do auta : "+distance_between_objects,Toast.LENGTH_LONG).show();
        Log.d("Lokalizacja","Odleglosc do auta : "+distance_between_objects);

        if(distance_between_objects<DISTANCE_TO_CAR){
            openCar_bt.setVisibility(View.VISIBLE);
            closeCar_bt.setVisibility(View.VISIBLE);
            infoAboutDistance_tv.setVisibility((View.GONE));
        }else{
            openCar_bt.setVisibility(View.GONE);
            closeCar_bt.setVisibility(View.GONE);
            infoAboutDistance_tv.setVisibility(View.VISIBLE);
        }
        return true;
    }

    public void powrot_do_rezerwacji() {
        //finish();
        //JSON_moje_rezerwacje json_moje_rezerwacje=new JSON_moje_rezerwacje();
        //json_moje_rezerwacje.StartUpdate("","",MojeRezerwacje.this);
        JSON_moje_rezerwacje_new json_moje_rezerwacje_new = new JSON_moje_rezerwacje_new();
        json_moje_rezerwacje_new.StartUpdate(MojeRezerwacje.this);
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            infoAboutGPS_tv.setVisibility(View.GONE);

            Log.d("Lokalizacja",location.getLatitude()+" "+location.getLongitude());
            CheckDistance(location,autoLocation);
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




}




