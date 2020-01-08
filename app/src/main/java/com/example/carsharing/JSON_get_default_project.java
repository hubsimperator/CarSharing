package com.example.carsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.List;

public class JSON_get_default_project {
AlertDialog alertDialog;
            Context con = null;
            String USER,grupa,numer;
public void StartUpdate(Context context) {
        LoginDataHandler lo = new LoginDataHandler(context);
        Cursor login = lo.getData();
        while (login.moveToNext()){
        USER = login.getString(1);
        }
        lo.close();
        con = context;
        new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppGetUserProject");
        }


public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        String json = "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("User",USER);
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
        log.inputLog( "JSON_Login.class 001: "+e.toString());
        log.close();
        }
        return result;
        }
private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        return POST(urls[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        boolean insered ;
        if (!result.equals("null")) {
            Rezerwacja rez = new Rezerwacja();
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonobject;
                for (int i = 0; i < jsonArray.length(); ) {
                    jsonobject = jsonArray.getJSONObject(i);
                    grupa = jsonobject.getString("GRUPA_PROJEKTU");
                    numer = jsonobject.getString("NR_PROJEKTU");
                        i++;
                }
                rez.wyswietl_projekt(con,numer,grupa);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
}
