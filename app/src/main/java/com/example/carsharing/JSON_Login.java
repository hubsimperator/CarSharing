package com.example.carsharing;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import androidx.core.app.ActivityCompat;

public class JSON_Login {
    Context con = null;
    String User ="",Pass="",Token="";
    TextView er=null;
    ProgressDialog progressDialog;
    public void StartUpdate(String Login, String Password, Context context, TextView error, String Tok, ProgressDialog pg) {
        con = context;
        User=Login;
        Pass=Password;
        er=error;
        Token=Tok;
        progressDialog=pg;
        new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/GetUsercs");
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("User",User);
            jsonObject.accumulate("Password",Pass);
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
        Log.d("kroki","1");
            if(result.contains("true"))
            {
                try {
                    JSON_lista_projektow_check lpc = new JSON_lista_projektow_check();
                    lpc.StartUpdate(con,progressDialog,Token);
                    Log.d("kroki","2");
                } catch(Exception e){Logs_DataHandler log = new Logs_DataHandler(con);
                    log.inputLog( "JSON_Login.class 002: "+e.toString());
                    log.close();
            }

            }
            else
            {
                er.setTextColor(0xFFCC0000);
                er.setGravity(Gravity.CENTER);
                er.setText("Błędny Login lub Hasło");
                progressDialog.hide();
            }
        Log.d("kroki","5");
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
