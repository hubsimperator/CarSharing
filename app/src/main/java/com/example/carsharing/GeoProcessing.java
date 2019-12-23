package com.example.carsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeoProcessing{

Context con;
        public void CalculateGeoDistance(Context _con){
            con=_con;
        LatLng straszyn=ReverseGeoCoding("Straszyn");

        String[] latlong =  "54.357851,18.656592".split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng location = new LatLng(latitude, longitude);

        double _distance=distance(straszyn.latitude,straszyn.longitude,location.latitude,location.longitude);
        Log.d("Dystans",Double.toString(_distance));


            straszyn=ReverseGeoCoding("Wałowa");
            _distance=distance(straszyn.latitude,straszyn.longitude,location.latitude,location.longitude);
            Log.d("Dystans",Double.toString(_distance));

    }

    ArrayList<Double> distance;
    ArrayList<String> parkingi;

    public void setNearestParking(Context _con){
        con=_con;

        String[] latlong =  "54.357851,18.656592".split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng location = new LatLng(latitude, longitude);

        parkingi=new ArrayList<>();
        distance=new ArrayList<>();
        try {
            Lista_parking_DataHandler LDH = new Lista_parking_DataHandler(con);
            Cursor getdata = LDH.getData();
            while (getdata.moveToNext()) {
                parkingi.add(getdata.getString(1));
            }
            LDH.close();
        } catch (Exception e){
            e.printStackTrace();
        }


        for(int i=0;i<parkingi.size();i++){
            LatLng coordinates=ReverseGeoCoding(parkingi.get(i));
            distance.add(distance(coordinates.latitude,coordinates.longitude,location.latitude,location.longitude));
        }

        Log.d("dystans",distance.toString());

        int minIndex = distance.indexOf(Collections.min(distance));
        Toast.makeText(con,"Najbliżej masz do parkingu: "+parkingi.get(minIndex),Toast.LENGTH_LONG);

       AlertDialog alertDialog=new AlertDialog.Builder(con)
                .setTitle("Dystans ")
                .setMessage("Najbliżej masz do parkingu: "+parkingi.get(minIndex))
                .setIcon(android.R.drawable.ic_input_add)
                .setCancelable(true)
                .show();

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515*1000;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public LatLng ReverseGeoCoding(String strAddress){
        Geocoder coder = new Geocoder(con);
        List<Address> address;
        LatLng _location = null;
        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
            double latitude= (double) (location.getLatitude());
            double longitude=(double) (location.getLongitude());
            _location = new LatLng(latitude, longitude);

            return _location;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _location;

    }

}
