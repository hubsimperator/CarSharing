package com.example.carsharing.Activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carsharing.JSON.JSON_lokalizacja_samochodu;
import com.example.carsharing.Obiekt_LokalizacjaSamochodu;
import com.example.carsharing.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;
String BookingId;

    public static TextView samochod_tv;
    public static TextView bateria_tv;
    public static Button maptypes_bt;
    public static TextView czasodczytu_tv;

    int rodzaj_mapy=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras= getIntent().getExtras();
        BookingId=extras.getString("BookingId");

        LayoutInflater inflater = (LayoutInflater)   Mapa.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = findViewById(R.id.maap);

        View view_map=findViewById(R.id.map);

         samochod_tv=(TextView) view.findViewById(R.id.samochod_tv);
         bateria_tv=(TextView) view.findViewById(R.id.bateria_tv);
         maptypes_bt=(Button) view.findViewById(R.id.maptypes_bt);
         czasodczytu_tv=(TextView) view_map.findViewById(R.id.czas_odczytuTV);

         maptypes_bt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 rodzaj_mapy++;
                 if(rodzaj_mapy>1) rodzaj_mapy=0;
                 if(rodzaj_mapy==0) mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                 else if(rodzaj_mapy==1) mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
             }
         });

        ImageView back_bt = (ImageView) findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void setCarPosition(Obiekt_LokalizacjaSamochodu obiekt_lokalizacjaSamochodu) {

        try {
            samochod_tv.setText(obiekt_lokalizacjaSamochodu.getResourceName());
            bateria_tv.setText(obiekt_lokalizacjaSamochodu.getBatery());
            czasodczytu_tv.setText(obiekt_lokalizacjaSamochodu.getCzasOdczytu());

            double Lat = Double.valueOf(obiekt_lokalizacjaSamochodu.getLatitude());
            double Lon = Double.valueOf(obiekt_lokalizacjaSamochodu.getLongitude());

            LatLng auto = new LatLng(Lat, Lon);

            mMap.addMarker(new MarkerOptions().position(auto).title("auto").icon(BitmapDescriptorFactory.fromResource(R.drawable.znacznik)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(auto, 18.0f));
        }catch (NullPointerException ne){
            String s= ne.toString();
            Log.d("s","a00");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        JSON_lokalizacja_samochodu json_lokalizacja_samochodu=new JSON_lokalizacja_samochodu();
        json_lokalizacja_samochodu.StartUpdate(BookingId, Mapa.this);
    }
}

