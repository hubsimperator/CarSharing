package com.example.carsharing.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.carsharing.DataHandler.Lista_parking_DataHandler;
import com.example.carsharing.DataHandler.Logs_DataHandler;

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

public class JSON_lista_parkingow {

    Context con = null;
ArrayList<String> parking_nazwa;

    public void StartUpdate(Context context) {
        con = context;
        new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppVParkings");
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) result = convertInputStreamToString(inputStream);
            else result = "Nie działa";
        } catch (Exception e) {
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_lista_parkingow.class 001: "+e.toString());
            log.close();
        }
        return result;
    }
    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
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
        String post_result;



        @Override
        protected String doInBackground(String... urls) {
            post_result=POST(urls[0]);
            deserialize_json(post_result);
            return null;
        }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.dismiss();


            Lista_parking_DataHandler LD = new Lista_parking_DataHandler(con);
            LD.dropdatabase();
            boolean insert;
            for (int i = 0; i < parking_nazwa.size(); ) {
                insert = LD.inputData(parking_nazwa.get(i));
                if(insert)
                {i++;}
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


    public void deserialize_json(String input)
    {
        Log.d("output",input);

        parking_nazwa=new ArrayList<>();

        JSONArray array = null;
        String dataname;

        try {
            array = new JSONArray(input);

        } catch (JSONException e) {
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_lista_parkingow.class 002: "+e.toString());
            log.close();
        }

        try {

            for (int i = 0; i <array.length(); i++) {
            JSONObject row = array.getJSONObject(i);
            parking_nazwa.add(row.getString("parking"));
            }

        }
        catch (JSONException e) {
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_lista_parkingow.class 003: "+e.toString());
            log.close();
        }

    }
}
