package com.example.carsharing.Other;

import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.carsharing.Activity.Menu;
import com.example.carsharing.DataHandler.Lista_parking_DataHandler;
import com.example.carsharing.DataHandler.Logs_DataHandler;
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

    public void setNearestParking(LatLng _myCoordinate,Context _con){
        con=_con;

        String[] latlong =  "54.299752,18.591066".split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng location = new LatLng(latitude, longitude);

        location=_myCoordinate;

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
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "GeoProcessing.class 001: "+e.toString());
            log.close();
        }


        for(int i=0;i<parkingi.size();i++){
            LatLng coordinates=ReverseGeoCoding(parkingi.get(i));
            distance.add(distance(coordinates.latitude,coordinates.longitude,location.latitude,location.longitude));
        }

        Log.d("dystans",distance.toString());

        int minIndex = distance.indexOf(Collections.min(distance));

        Menu menu = new Menu();
        menu.setNearestParking(parkingi.get(minIndex));
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
           // strAddress="Port";
            address = coder.getFromLocationName(strAddress,5,54.248257,18.396202,54.499899,18.868130);
            if (address.size()<1) {

                double latitude= 54.402358;
                double longitude=18.730379;
                _location = new LatLng(latitude, longitude);
                return _location;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
            double latitude= (double) (location.getLatitude());
            double longitude=(double) (location.getLongitude());
            _location = new LatLng(latitude, longitude);

            return _location;
        } catch (IOException e) {
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "GeoProcessing.class 002: "+e.toString());
            log.close();
        }
        return _location;

    }

}
