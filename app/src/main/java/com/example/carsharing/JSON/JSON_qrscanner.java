package com.example.carsharing.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.carsharing.Activity.RozpoczecieJazdy;
import com.example.carsharing.DataHandler.LoginDataHandler;
import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.Obiekt_QRgetstart;
import com.example.carsharing.Other.Model_comment;
import com.example.carsharing.Other.QRScanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JSON_qrscanner {
    Context con = null;
    String EitResource;
    String user_name;
    Obiekt_QRgetstart obiekt_qRgetstart;

public void StartUpdate(Context context,String _EitResource) {
        con = context;
        EitResource=_EitResource;

        new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppGetQRGetStart");

        }


public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        String json = "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("UserName",user_name);
        jsonObject.accumulate("Eit_Resource",EitResource);
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
private class HttpAsyncTask2 extends AsyncTask<String, Void, Obiekt_QRgetstart> {
    @Override
    protected Obiekt_QRgetstart doInBackground(String... urls) {
        String res=POST(urls[0]);
        if (!res.equals("null")) {
            try {
                JSONArray jsonArray = new JSONArray(res);
                JSONObject jsonobject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonobject = jsonArray.getJSONObject(i);

                    String BookingId= jsonobject.getString("BookingId");
                    String GrProjektu= jsonobject.getString("GrProjektu");
                    String NrProjektu=jsonobject.getString("NrProjektu");
                    String Status=jsonobject.getString("Status");
                    String Error=jsonobject.getString("Error");
                    obiekt_qRgetstart=new Obiekt_QRgetstart(BookingId,GrProjektu,NrProjektu,Error,Status);
                }

            } catch (Exception e) {
                Logs_DataHandler log = new Logs_DataHandler(con);
                log.inputLog( "JSON_qrscanner 002: "+e.toString());
                log.close();
            }
        }

        return obiekt_qRgetstart;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            LoginDataHandler LDH = new LoginDataHandler(con);
            Cursor getdata = LDH.getData();
            while (getdata.moveToNext()) {
                user_name=getdata.getString(1);
            }
            LDH.close();
        } catch (Exception e){
            e.printStackTrace();
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_qrscanner.class 003: "+e.toString());
            log.close();
        }
    }
    AlertDialog alertDialog;
    @Override
    protected void onPostExecute(Obiekt_QRgetstart result) {

        if(result.getError().length()>2) {
            alertDialog = new AlertDialog.Builder(con)
                    .setTitle("Błąd")
                    .setMessage(result.getError())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(true)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            alertDialog.dismiss();
                        }
                    })
                    .show();
        }else{
            /*
            Intent intent = new Intent(con, RozpoczecieJazdy.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("BookingId", result.getBookingId());
            intent.putExtra("GrupaProjektu", result.getGrProjektu());
            intent.putExtra("NrProjektu", result.getNrProjektu());
            intent.putExtra("Status",result.getStatus());
            con.startActivity(intent);
            */

            JSON_lista_rezerwacjiQR json_lista_rezerwacjiQR=new JSON_lista_rezerwacjiQR();
            json_lista_rezerwacjiQR.StartUpdate(con);


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