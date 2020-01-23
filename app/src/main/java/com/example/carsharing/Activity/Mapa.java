package com.example.carsharing.Activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carsharing.JSON.JSON_lokalizacja_samochodu;
import com.example.carsharing.Obiekt_LokalizacjaSamochodu;
import com.example.carsharing.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;
String BookingId;

    public static TextView samochod_tv;
    public static TextView bateria_tv;

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

         samochod_tv=(TextView) view.findViewById(R.id.samochod_tv);
         bateria_tv=(TextView) view.findViewById(R.id.bateria_tv);



        ImageView back_bt = (ImageView) findViewById(R.id.back_bt);

    }

    public void setCarPosition(Obiekt_LokalizacjaSamochodu obiekt_lokalizacjaSamochodu) {


       samochod_tv.setText(obiekt_lokalizacjaSamochodu.getResourceName());
       bateria_tv.setText(obiekt_lokalizacjaSamochodu.getBatery());

       double Lat=Double.valueOf(obiekt_lokalizacjaSamochodu.getLatitude());
       double Lon=Double.valueOf(obiekt_lokalizacjaSamochodu.getLatitude());

        LatLng auto = new LatLng(Lat, Lon);

        mMap.addMarker(new MarkerOptions().position(auto).title("auto"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(auto,18.0f));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        JSON_lokalizacja_samochodu json_lokalizacja_samochodu=new JSON_lokalizacja_samochodu();
        json_lokalizacja_samochodu.StartUpdate(BookingId, Mapa.this);
    }
}

