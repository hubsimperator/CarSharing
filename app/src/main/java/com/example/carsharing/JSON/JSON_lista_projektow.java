package com.example.carsharing.JSON;

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

import com.example.carsharing.DataHandler.LoginDataHandler;
import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.Other.Obiekt_Projekt;
import com.example.carsharing.Other.People;
import com.example.carsharing.Other.PeopleAdapter;
import com.example.carsharing.R;
import com.example.carsharing.Activity.Rezerwacja;
import com.example.carsharing.Activity.RozpoczecieJazdy;

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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JSON_lista_projektow {

    Context con = null;
    ArrayList<HashMap<String, String>> lista_projekt;
    HashMap<String, String> lista_pola_projekt;
    ArrayList<String> lista_grupa_projektowa;
    Integer projekt;
    String DEFAULT_GROUP_NAME="PO";
    String USER="";

    public static String selected_item;

    public static ArrayList<String> lista_samochodow;
    String currentDate;
    public void StartUpdate(Integer _projekt, Context context) {
        projekt=_projekt;
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
        lista_grupa_projektowa=new ArrayList<>();
        new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppGetProjectList");
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
            log.inputLog( "JSON_lista_projektow.class 001: "+e.toString());
            log.close();
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
        String post_result;

        @Override
        protected String doInBackground(String... urls) {
            post_result=POST(urls[0]);
            deserialize_json(post_result);
            return null;
        }

        String nazwa_projektu;

        private People selectedPerson;
        List<People> mList;


    @Override
    protected void onPostExecute(String result) {
        alertDialog.dismiss();
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con)
                .setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();

                    }
                })
                .setNegativeButton("Wybierz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if(projekt==0) {
                        Rezerwacja rez = new Rezerwacja();
                        rez.wyswietl_projekt(con, nazwa_projektu, selected_item);
                    }
                    else {
                        RozpoczecieJazdy rez = new RozpoczecieJazdy();
                        rez.wyswietl_projekt(con, nazwa_projektu, selected_item);
                    }
                    alertDialog.dismiss();
                }catch (Exception ne){
                    alertDialog.dismiss();
                    Logs_DataHandler log = new Logs_DataHandler(con);
                    log.inputLog( "JSON_lista_projektow.class 002: "+ne.toString());
                    log.close();

                }

            }
        });





        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                (con, android.R.layout.select_dialog_item, lista_nazwa_proj);
        LayoutInflater inflater = (LayoutInflater)   con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        View view = inflater.inflate(R.layout.projekty,null);
        Spinner projekt_sp=(Spinner) view.findViewById(R.id.spinner2);

        mList = retrievePeople();
        final AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        PeopleAdapter adapter;

        adapter = new PeopleAdapter(con, R.layout.activity_main, R.id.lbl_name, mList);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //this is the way to find selected object/item
                selectedPerson = (People) adapterView.getItemAtPosition(pos);
              //  nazwa_projektu=(String) adapterView.getItemAtPosition(pos);
                nazwa_projektu=selectedPerson.getName();
            }
        });


        ImageView dropdown=(ImageView) view.findViewById(R.id.dropdown);
        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actv.showDropDown();
            }
        });



        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(con,
                android.R.layout.simple_spinner_item,lista_grupa_projektowa);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projekt_sp.setAdapter(adapter4);
        projekt_sp.setSelection(lista_grupa_projektowa.indexOf(DEFAULT_GROUP_NAME));

        selected_item=projekt_sp.getSelectedItem().toString();

        dialogBuilder.setView(view);

        alertDialog =dialogBuilder.create();
        alertDialog.show();


    }

        private List<People> retrievePeople() {
            List<People> list = new ArrayList<People>();
            for(int i =0;i<lista_nazwa_proj.size();i++) {
                list.add(new People(lista_nazwa_proj.get(i)));
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


    Set set_grupa_projektowa;
    ArrayList<String> lista_nazwa_proj;

    public void deserialize_json(String input)
    {
        Log.d("output",input);


        JSONArray array = null;
        String dataname;

        try {
            array = new JSONArray(input);

        } catch (JSONException e) {
            Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_lista_projekctow.class 003: "+e.toString());
            log.close();
        }

        try {
            HashMap<String,ArrayList<Integer>> proj=new HashMap<>();
            Obiekt_Projekt obiekt_projekt=new Obiekt_Projekt();
            lista_projekt=new ArrayList<>();

            for (int i = 0; i <array.length(); i++) {

            lista_pola_projekt=new HashMap<>();
            JSONObject row = array.getJSONObject(i);
            obiekt_projekt.setGrupa_Projektu(row.getString("GRUPA_PROJEKTU"));
            obiekt_projekt.setNr_Projektu(row.getString("NR_PROJEKTU"));
            obiekt_projekt.setNazwa(row.getString("NAZWA"));
            obiekt_projekt.setStatus(row.getString("STATUS"));

            lista_grupa_projektowa.add(row.getString("GRUPA_PROJEKTU"));
            lista_pola_projekt.put("GRUPA_PROJEKTU",obiekt_projekt.getGrupa_Projektu());
            lista_pola_projekt.put("NR_PROJEKTU",obiekt_projekt.getNr_Projektu());
            lista_pola_projekt.put("NAZWA",obiekt_projekt.getNazwa());
            lista_pola_projekt.put("STATUS",obiekt_projekt.getStatus());

            lista_projekt.add(lista_pola_projekt);
            }
            set_grupa_projektowa = new HashSet(lista_grupa_projektowa);
            lista_grupa_projektowa=new ArrayList<>(set_grupa_projektowa);
            Collections.sort(lista_grupa_projektowa);
            lista_nazwa_proj=new ArrayList<>();

            for(int i =0;i<lista_projekt.size();i++){
               // if(lista_projekt.get(i).get("GRUPA_PROJEKTU").equals("PI")){
                    lista_nazwa_proj.add(lista_projekt.get(i).get("NR_PROJEKTU"));
                //}
            }



        }
        catch (JSONException e) {               Logs_DataHandler log = new Logs_DataHandler(con);
            log.inputLog( "JSON_lista_projektow.class 004: "+e.toString());
            log.close();
        }

    }
}
