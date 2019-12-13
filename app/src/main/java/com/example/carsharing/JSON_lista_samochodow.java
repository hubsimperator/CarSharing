package com.example.carsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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

public class JSON_lista_samochodow {

    Context con = null;

    public static ArrayList<String> lista_samochodow;
    public static ArrayList<String> lista_samochodow_id;

    String StartDate,EndDate;

    public void StartUpdate(String _StartDate, String _EndDate, Context context) {
        con = context;
        StartDate=_StartDate;
        EndDate=_EndDate;
        lista_samochodow=new ArrayList<>();
        lista_samochodow_id=new ArrayList<>();
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
            Log.d("InputStream", e.getLocalizedMessage());
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

        @Override
        protected String doInBackground(String... urls) {
            String post_result=POST(urls[0]);
            deserialize_json(post_result);
            return null;
        }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.dismiss();
        Rezerwacja res=new Rezerwacja();
        res.wyswietl_liste(con,lista_samochodow,lista_samochodow_id);

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
        JSONArray array = null;
        String dataname;

        try {
            array = new JSONArray(input);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i <array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                dataname = row.getString("ResourceName");
                lista_samochodow.add(dataname);
                lista_samochodow_id.add(row.getString("ResourceId"));
            }
        }
        catch (JSONException e) {                e.printStackTrace();
        }

    }
}
