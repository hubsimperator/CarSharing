package com.example.carsharing.JSON;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharing.Activity.ListaRezerwacji;
import com.example.carsharing.DataHandler.LoginDataHandler;
import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.Other.Obiekt_Rezerwacja;

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
import java.util.HashMap;


public class JSON_moje_rezerwacje extends AppCompatActivity {

    Obiekt_Rezerwacja rezerwacja;
    ArrayList<HashMap<String, String>> lista_rezerwacji;
    HashMap<String, String> lista_pola_rezerwacji;

    String user_name;

    Context con ;
    HttpAsyncTask2 mTask;
    public static ArrayList<String> lista_samochodow;

    public void StartUpdate(String Login, String Password, Context context) {
        con = context;
     //   er=error;
        lista_samochodow=new ArrayList<>();

        mTask=new HttpAsyncTask2();

        mTask.execute("https://notif2.sng.com.pl/api/CsAppGetMyBookings");
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
            log.inputLog( "JSON_moje_rezerwacje.class 001: "+e.toString());
            log.close();
        }


        return result;
    }
    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        public AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            alertDialog=new AlertDialog.Builder(con)
                    .setTitle("Proszę czekać ")
                    .setMessage("Pobieranie danych ...")
                    .setIcon(android.R.drawable.ic_input_add)
                    .setCancelable(false)
                    .show();

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
                log.inputLog( "Rezerwacja.class 003: "+e.toString());
                log.close();
            }
        }



        String post_result;


        @Override
        protected String doInBackground(String... urls) {
            post_result=POST(urls[0]);
            if(post_result==null){

                alertDialog.dismiss();

                finish();
                mTask.cancel(true);
            }else {
                deserialize_json(post_result);
                Log.d("aaa", "a");
            }
            return null;
        }


    @Override
    protected void onPostExecute(String result) {

            Log.d("tt","aa");
        if(post_result.equals("[]")){
                Toast.makeText(con,"Brak rezerwacji",Toast.LENGTH_LONG).show();
            }
        try {
            alertDialog.dismiss();
        }catch (IllegalArgumentException ie){
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_moje_rezerwacje.class 002: "+ie.toString());
            log.close();
        }

        ListaRezerwacji listaRezerwacji;
//        listaRezerwacji=new ListaRezerwacji(lista_rezerwacji);
        Intent intent= new Intent(con,ListaRezerwacji.class);
        intent.putExtra("lista_rezerwacji",lista_rezerwacji);
        con.startActivity(intent);
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


        JSONArray array = null;
        String dataname;

        try {
            array = new JSONArray(input);

        } catch (JSONException e) {
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_moje_rezerwacje.class 003: "+e.toString());
            log.close();
        }

        try {
            rezerwacja=new Obiekt_Rezerwacja();
            lista_rezerwacji=new ArrayList<>();

    try {
        for (int i = 0; i < array.length(); i++) {

            lista_pola_rezerwacji = new HashMap<>();


            JSONObject row = array.getJSONObject(i);
            rezerwacja.setBookingId(row.getString("BookingId"));
            rezerwacja.setStartDate(row.getString("StartDate"));
            rezerwacja.setEndDate(row.getString("EndDate"));
            rezerwacja.setAllDay(row.getString("AllDay"));
            rezerwacja.setSubject(row.getString("Subject"));
            rezerwacja.setEit_Resource(row.getString("Eit_Resource"));
            rezerwacja.setEit_ResourceName(row.getString("Eit_ResourceName"));
            rezerwacja.setEit_Uzytkownik(row.getString("Eit_Uzytkownik"));
            rezerwacja.setReminderId(row.getString("ReminderId"));
            rezerwacja.setLocation(row.getString("Location"));
            rezerwacja.setDescription(row.getString("Description"));
            rezerwacja.setStatus(row.getString("Status"));
            rezerwacja.setGrupaProjektu(row.getString("GRUPA_PROJEKTU"));
            rezerwacja.setNrProjektu(row.getString("NR_PROJEKTU"));

            lista_pola_rezerwacji.put("BookingId", rezerwacja.getBookingId());
            lista_pola_rezerwacji.put("StartDate", rezerwacja.getStartDate());
            lista_pola_rezerwacji.put("EndDate", rezerwacja.getEndDate());
            lista_pola_rezerwacji.put("AllDay", rezerwacja.getAllDay());
            lista_pola_rezerwacji.put("Subject", rezerwacja.getSubject());
            lista_pola_rezerwacji.put("Eit_Resource", rezerwacja.getEit_Resource());
            lista_pola_rezerwacji.put("Eit_ResourceName", rezerwacja.getEit_ResourceName());
            lista_pola_rezerwacji.put("Eit_Uzytkownik", rezerwacja.getEit_Uzytkownik());
            lista_pola_rezerwacji.put("ReminderId", rezerwacja.getReminderId());
            lista_pola_rezerwacji.put("Location", rezerwacja.getLocation());
            lista_pola_rezerwacji.put("Description", rezerwacja.getDescription());
            lista_pola_rezerwacji.put("Status", rezerwacja.getStatus());
            lista_pola_rezerwacji.put("GrupaProjektu", rezerwacja.getGrupaProjektu());
            lista_pola_rezerwacji.put("NrProjektu", rezerwacja.getNrProjektu());

            lista_rezerwacji.add(lista_pola_rezerwacji);

            Log.d("rezerwacja", rezerwacja.getStartDate());
        }

    }catch (NullPointerException ne){
        Toast.makeText(con,"Brak rezerwacji",Toast.LENGTH_LONG).show();

    }

        }
        catch (JSONException e) {                Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_moje_rezerwacje.class 004: "+e.toString());
            log.close();
        }

    }
}
