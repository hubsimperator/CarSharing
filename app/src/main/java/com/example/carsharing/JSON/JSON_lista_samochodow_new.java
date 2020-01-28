package com.example.carsharing.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.example.carsharing.Activity.Rezerwacja;
import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.Obiekt_Samochód;
import com.example.carsharing.Other.Obiekt_Rezerwacja;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JSON_lista_samochodow_new {

    Context con = null;

    Obiekt_Samochód obiekt_samochód;
    ArrayList<Obiekt_Samochód> listaSamochodow;


    String StartDate,EndDate,Parking,Dystans;

    public void StartUpdate(String _StartDate, String _EndDate,String _Parking,String _Dystans, Context context) {
        con = context;
        Parking=_Parking;
        StartDate=_StartDate;
        EndDate=_EndDate;
        new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppGetAutos");
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("DateFrom",StartDate);
            jsonObject.accumulate("DateTo",EndDate);
            jsonObject.accumulate("Parking",Parking);
            jsonObject.accumulate("Dystans",Dystans);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json, "UTF-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) result = convertInputStreamToString(inputStream);
            else result = "Nie działa";
        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_lista_samochodow.class 001: "+e.toString());
            log.close();
        }
        return result;
    }
    private class HttpAsyncTask2 extends AsyncTask<String, Void,  ArrayList<Obiekt_Samochód>> {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            alertDialog=new AlertDialog.Builder(con)
                    .setTitle("Proszę czekać ")
                    .setMessage("Pobieranie danych ...")
                    .setIcon(android.R.drawable.ic_input_add)
                    .setCancelable(false)
                    .show();
        }

        @Override
        protected ArrayList<Obiekt_Samochód> doInBackground(String... urls) {
            String post_result=POST(urls[0]);
            ArrayList<Obiekt_Samochód> listaSamochodow=new ArrayList<>();
            listaSamochodow=deserialize_json(post_result);
            return listaSamochodow;
        }

        @Override
        protected void onPostExecute(ArrayList<Obiekt_Samochód> obiekt_samochód) {
            super.onPostExecute(obiekt_samochód);

            alertDialog.dismiss();
            if(listaSamochodow.size()>0) {
                Rezerwacja res = new Rezerwacja();
                res.wyswietl_liste2(con,listaSamochodow);
            }
            else{
                alertDialog=new AlertDialog.Builder(con)
                        .setTitle("Uwaga")
                        .setMessage("Brak dostępnych pojazdów w podanym terminie. Czy chcesz sprawdzić dostępność pojazdów w najbliższym czasie ?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                JSON_dostepnosc_aut json_dostepnosc_aut=new JSON_dostepnosc_aut();
                                json_dostepnosc_aut.StartUpdate(StartDate,Parking,con);
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


        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) result += line;
        inputStream.close();
        return result;
    }

    public  ArrayList<Obiekt_Samochód> deserialize_json(String input)
    {
        JSONArray array = null;
        try {
            array = new JSONArray(input);

        } catch (JSONException e) {
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_lista_samochodow_new.class 002: "+e.toString());
            log.close();
        }
        listaSamochodow=new ArrayList<>();

        try {
            for (int i = 0; i <array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                String ResourceId=row.getString("ResourceId");
                String ResourceName=row.getString("ResourceName");
                String Parking=row.getString("Parking");
                obiekt_samochód=new Obiekt_Samochód(ResourceId,ResourceName,Parking);
                listaSamochodow.add(obiekt_samochód);
            }
        }
        catch (JSONException e) {                Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_lista_samochodow_new.class 003: "+e.toString());
            log.close();
        }
        return listaSamochodow;
    }
}
