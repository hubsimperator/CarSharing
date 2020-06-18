package com.example.carsharing.JSON;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.R;

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

public class JSON_close_car {
    Context con ;
    public static String EitResource = null;
    public static String BookingId = null;
    String Results="";
    String Response;

    public void StartUpdate(String _EitResource,String _BookingId, Context context) {
        con = context;
        EitResource=_EitResource;
        BookingId=_BookingId;
        new JSON_close_car.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppOpenClose");
    }

    public String POST(String url,String _EitResource,String _BookingId) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("ID","1");
            jsonObject.accumulate("ResourceName",_EitResource);
            jsonObject.accumulate("BookingId",_BookingId);
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
            log.inputLog( "JSON_ocen_car.class 001: "+e.toString());
            log.close();
        }
        ;
        return deserialize_json(result);
    }

    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(con);
            progressDialog.setMessage("Wysyłanie danych.Proszę czekać");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0],EitResource,BookingId);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            alertDialog = new AlertDialog.Builder(con)
                    .setTitle("Informacja")
                    .setMessage(result)
                    .setIcon(R.drawable.confirm)
                    .setCancelable(true)
                    .show();
        }
    }

    public String deserialize_json(String input)
    {
        Response="test";
        JSONArray array = null;

        try {
            array = new JSONArray(input);

        } catch (JSONException e) {

        }

        try {

            for (int i = 0; i <array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                Response=row.getString("response");
            }
        }
        catch (JSONException e) {
        }
        if(Response.contains("$OK:OUTS")) Response="Samochód zamknięty";

        return Response;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) result += line;
        inputStream.close();
        return result;
    }

}
