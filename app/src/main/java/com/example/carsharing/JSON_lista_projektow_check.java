package com.example.carsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JSON_lista_projektow_check{
    AlertDialog alertDialog;
    String nazwa_projektu;
    int CProj=0;
    Context con = null;
    ArrayList<HashMap<String, String>> lista_projekt;
    HashMap<String, String> lista_pola_projekt;
    ArrayList<String> lista_grupa_projektowa, lista_nazwa_proj;
    Integer projekt;
    String DEFAULT_GROUP_NAME="PO";
    String USER="";
    public static String selected_item;
    List<People> mList;
    private People selectedPerson;
    public static ArrayList<String> lista_samochodow;
    String currentDate;
    Spinner projekt_sp;
    AutoCompleteTextView actv;
    PeopleAdapter adapter;
    public void StartUpdate( Context context) {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = formatter.format(todayDate);
        LoginDataHandler lo = new LoginDataHandler(context);
        Cursor login = lo.getData();
        while (login.moveToNext()){
            USER = login.getString(1);
        }
        Log.d("user",USER);
        lo.close();
        con = context;
        //   er=error;
        lista_grupa_projektowa=new ArrayList<>();

        Projekty_DataHandler pdh = new Projekty_DataHandler(con);
        CProj = pdh.getCount();
        new JSON_lista_projektow_check.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppGetProjectList2");
    }


    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Data",currentDate);
            jsonObject.accumulate("User",USER);
            jsonObject.accumulate("cproj",CProj);
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
            log.inputLog( "JSON_lista_projektów_check 001: "+e.toString());
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
            Projekty_DataHandler myDB = new Projekty_DataHandler(con);
            myDB.dropdatabase();
            if (!result.equals("null")) {



                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonobject;
                    if(jsonArray.length()>2) {
                        myDB.dropdatabase();
                        for (int i = 0; i < jsonArray.length(); ) {
                            jsonobject = jsonArray.getJSONObject(i);

                            insered = myDB.inputData(jsonobject.getString("GRUPA_PROJEKTU"), jsonobject.getString("NR_PROJEKTU"), jsonobject.getString("DEF"));
                            if (insered) {
                                i++;
                            } else {
                                Toast.makeText(con, "błąd podczas wczytywania danych", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Logs_DataHandler log = new Logs_DataHandler(con);
                    log.inputLog( "JSON_lista_projektów_check 002: "+e.toString());
                    log.close();
                }
            }

        }
        private List<People> retrievePeople(ArrayList<String> lista_nazwa_pro) {


            List<People> list = new ArrayList<People>();
            for(int i =0;i<lista_nazwa_proj.size();i++) {
                list.add(new People(lista_nazwa_pro.get(i)));
            }
            return list;
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
