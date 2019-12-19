package com.example.carsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

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

public class JSON_end_trip {

    Context con = null;

    String BookingID;


    public void StartUpdate(String _BookingID, Context context) {
        con = context;
        BookingID=_BookingID;
        new JSON_end_trip.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppInsertEndTrip");
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("BookingID",BookingID);

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
            return POST(urls[0]);
        }


        @Override
        protected void onPostExecute(String result) {
            alertDialog.dismiss();
            if (result.contains("false")) {
                alertDialog = new AlertDialog.Builder(con)
                        .setTitle("Błąd")
                        .setMessage("Nie można zakończyć jazdy")
                        .setIcon(R.drawable.cancel)
                        .setCancelable(true)
                        .show();
            }
            else if (result.contains("true")) {
                alertDialog = new AlertDialog.Builder(con)
                        .setTitle("Potwierdzenie ")
                        .setMessage("Zakończono jazdę")
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                //alertDialog.dismiss();
                                MojeRezerwacje mojeRezerwacje=new MojeRezerwacje();
                                mojeRezerwacje.finish();
                                JSON_moje_rezerwacje json_moje_rezerwacje=new JSON_moje_rezerwacje();
                                json_moje_rezerwacje.StartUpdate("","",con);
                            }
                        })
                        .setIcon(R.drawable.confirm)
                        .setCancelable(true)
                        .show();
            }
            else{
                alertDialog = new AlertDialog.Builder(con)
                        .setTitle("Błąd")
                        .setMessage(result)
                        .setIcon(R.drawable.cancel)
                        .setCancelable(true)
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

}