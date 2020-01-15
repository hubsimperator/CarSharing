package com.example.carsharing.JSON;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharing.Activity.ListaRezerwacji;
import com.example.carsharing.Activity.Rezerwacja;
import com.example.carsharing.DataHandler.LoginDataHandler;
import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.DostepnoscListAdapter;
import com.example.carsharing.Obiekt_Dostepnosc;
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
import java.util.HashMap;


public class JSON_dostepnosc_aut extends AppCompatActivity {

    Context con ;
    HttpAsyncTask2 mTask;

    ArrayList<Obiekt_Dostepnosc> lista_obiektowdostepnosc;
    String Data;
    String Parking;


    public void StartUpdate(String _Data, String _Parking, Context context) {
        con = context;
        Data=_Data;
        Parking=_Parking;

        mTask=new HttpAsyncTask2();

        mTask.execute("https://notif2.sng.com.pl/api/CsAppCheckAutoTime");
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Data",Data);
            jsonObject.accumulate("parking",Parking);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"UTF-8");

            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) result = convertInputStreamToString(inputStream);
            else result = "Nie działa";
        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_moje_rezerwacje.class 001: "+e.toString());
            log.close();
        }


        return result;
    }
    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        String post_result;


        @Override
        protected String doInBackground(String... urls) {
            post_result=POST(urls[0]);
            if(post_result==null){
                finish();
                mTask.cancel(true);
            }else {
                deserialize_json(post_result);
                Log.d("aaa", "a");
            }
            return null;
        }


    @Override
    protected void onPostExecute(String result) {
        Rezerwacja rezerwacja=new Rezerwacja();
        rezerwacja.wyswietl_dostepnosc(con,lista_obiektowdostepnosc,Parking);
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


    public void deserialize_json(String input)
    {
        Log.d("output",input);


        JSONArray array = null;
        try {
            array = new JSONArray(input);

        } catch (JSONException e) {
        }



        lista_obiektowdostepnosc=new ArrayList<>();

    try {
        for (int i = 0; i < array.length(); i++) {
            JSONObject row = array.getJSONObject(i);
            String samochod=row.getString("ResourceName");
            String samochodID=row.getString("ResourceID");
            String startDate=row.getString("WolnyOd");
            String endDate=row.getString("WolnyDo");

            if((samochod != null) && (samochodID!= null) && (startDate!= null) &&(endDate!= null )) {
                lista_obiektowdostepnosc.add(new Obiekt_Dostepnosc(samochod, samochodID, startDate, endDate));
            }
        }

    }catch (NullPointerException ne){
        Toast.makeText(con,"Brak rezerwacji",Toast.LENGTH_LONG).show();

    }
    catch (JSONException e) {                Logs_DataHandler log = new Logs_DataHandler(con);
         //   log.inputLog( "JSON_moje_rezerwacje.class 004: "+e.toString());
           // log.close();
        }

    }
}
