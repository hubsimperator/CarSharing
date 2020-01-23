package com.example.carsharing.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.carsharing.Activity.Mapa;
import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.Obiekt_LokalizacjaSamochodu;

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

public class JSON_lokalizacja_samochodu {

    Context con = null;

    String BookingId;
    String StartDate,EndDate,Parking,Dystans;

    public void StartUpdate(String _BookingId, Context context) {
        BookingId=_BookingId;
        con = context;
        new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppGetAutoData");
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("BookingId",BookingId);
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
            log.inputLog( "JSON_lokalizacja_samochodu.class 001: "+e.toString());
            log.close();
        }
        return result;
    }
    private class HttpAsyncTask2 extends AsyncTask<String, String, Obiekt_LokalizacjaSamochodu> {
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
        protected Obiekt_LokalizacjaSamochodu doInBackground(String... urls) {
           Obiekt_LokalizacjaSamochodu obiekt_lokalizacjaSamochodu;
            String post_result=POST(urls[0]);
            obiekt_lokalizacjaSamochodu=deserialize_json(post_result);
            return obiekt_lokalizacjaSamochodu;
        }

    @Override
    protected void onPostExecute(Obiekt_LokalizacjaSamochodu result) {
        alertDialog.dismiss();
        Mapa mapa=new Mapa();
        mapa.setCarPosition(result);
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

    public Obiekt_LokalizacjaSamochodu deserialize_json(String input)
    {
        JSONArray array = null;
        Obiekt_LokalizacjaSamochodu obiekt_lokalizacjaSamochodu = null;
        try {
            array = new JSONArray(input);

        } catch (JSONException e) {
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_lokalizacja_samochodu.class 002: "+e.toString());
            log.close();
        }

        try {
            for (int i = 0; i <array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
               String ResourceName = row.getString("ResourceName");
               String Latitude=row.getString("Latitude");
               String Longitude=row.getString("Longitude");
               String Batery=row.getString("Batery");
               obiekt_lokalizacjaSamochodu=new Obiekt_LokalizacjaSamochodu(ResourceName,Latitude,Longitude,Batery);

            }
        }
        catch (JSONException e) {                Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_lista_samochodow.class 003: "+e.toString());
            log.close();
        }

        return obiekt_lokalizacjaSamochodu;

    }
}
