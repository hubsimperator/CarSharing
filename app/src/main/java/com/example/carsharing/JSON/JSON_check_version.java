package com.example.carsharing.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.Other.Update;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSON_check_version {

    Context con = null;
    String Wersja ="";
    TextView er;
    public void StartUpdate(String version, TextView error, Context context) {
        con = context;
        Wersja = version;
        er=error;
        new JSON_check_version.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppWersja");
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Wersja",Wersja);
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
        protected void onPostExecute(final String result) {

            if(result.contains("True"))
            {

            }
            else
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(con);
                dlgAlert.setTitle("Dostępna jest nowa wersja aplikacji");
                dlgAlert.setMessage("Aktualna wersja może nie działać prawidłowo, zalecana jest aktualizacja.");
                dlgAlert.setPositiveButton("Aktualizuj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the dialog

                String URL =result.replace("\"","");
                Intent intent = new Intent(con, Update.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", URL);
                con.startActivity(intent);
                            }
                        });
                dlgAlert.show();
/*
                er.setTextColor(0xFFCC0000);
                er.setGravity(Gravity.CENTER);
                er.setText("Pobierz aktualną wersję: " + result);
                er.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uriUrl = Uri.parse(URL);
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        con.startActivity(launchBrowser);
                    }
                });

 */
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