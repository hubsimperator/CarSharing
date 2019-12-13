package com.example.carsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

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

public class JSON_potwierdzenie_rezerwacji {

    Context con = null;

    public static ArrayList<String> lista_samochodow;
    String StartDate,EndDate,AllDay,Subject,Eit_Resource,Eit_Użytkownik,ReminderID,Location,Description,Status,UserName,GrupaProjektu,NazwaProjektu;

    public void StartUpdate(String _StartDate, String _EndDate,String _AllDay,String _Subject, String _Eit_Resource,String _Eit_Użytkownik,String _ReminderID,String _Location, String _Description,String _Status,String _UserName,String _grupa_projektu,String _nazwa_projektu, Context context) {
        con = context;
        lista_samochodow=new ArrayList<>();
        StartDate=_StartDate;
        EndDate=_EndDate;
        AllDay=_AllDay;
        Subject=_Subject;
        Eit_Resource=_Eit_Resource;
        Eit_Użytkownik=_Eit_Użytkownik;
        ReminderID=_ReminderID;
        Location=_Location;
        Description=_Description;
        Status=_Status;
        UserName=_UserName;
        GrupaProjektu=_grupa_projektu;
        NazwaProjektu=_nazwa_projektu;

        try {
            LoginDataHandler LDH = new LoginDataHandler(con);
            Cursor getdata = LDH.getData();
            while (getdata.moveToNext()) {
                UserName=(getdata.getString(1));
            }
            LDH.close();
        } catch (Exception e){
            e.printStackTrace();
        }


        new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppSendBooking");
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("StartDate",StartDate);
            jsonObject.accumulate("EndDate",EndDate);
            jsonObject.accumulate("AllDay",AllDay);
            jsonObject.accumulate("Subject",Subject);
            jsonObject.accumulate("Eit_Resource",Eit_Resource);
            jsonObject.accumulate("Eit_Użytkownik",Eit_Użytkownik);
            jsonObject.accumulate("ReminderID",ReminderID);
            jsonObject.accumulate("Location",Location);
            jsonObject.accumulate("Description",Description);
            jsonObject.accumulate("Status",Status);
            jsonObject.accumulate("UserName",UserName);
            jsonObject.accumulate("GrupaProjektu",GrupaProjektu);
            jsonObject.accumulate("NrProjektu",NazwaProjektu);

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
        if (result.contains("False")) {
            alertDialog = new AlertDialog.Builder(con)
                    .setTitle("Błąd")
                    .setMessage("Nie można dokonać rezerwacji")
                    .setIcon(R.drawable.cancel)
                    .setCancelable(true)
                    .show();
        }
        else if (result.contains("True")) {
            alertDialog = new AlertDialog.Builder(con)
                    .setTitle("Potwierdzenie ")
                    .setMessage("Rezerwacja przebiegła pomyślnie")
                    .setIcon(R.drawable.confirm)
                    .setCancelable(true)
                    .show();
        }
        else{
            alertDialog = new AlertDialog.Builder(con)
                    .setTitle("Błąd")
                    .setMessage("Nastąpił błąd podczas wysyłania danych.")
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
