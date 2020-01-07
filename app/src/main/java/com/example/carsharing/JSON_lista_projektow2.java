package com.example.carsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JSON_lista_projektow2 {
AlertDialog alertDialog;
    String nazwa_projektu;
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
        //   er=error;
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
             Projekty_DataHandler myDB = new Projekty_DataHandler(con);
             myDB.dropdatabase();
             if (!result.equals("null")) {


             myDB.dropdatabase();
             try {
             JSONArray jsonArray = new JSONArray(result);
             JSONObject jsonobject;
             for (int i = 0; i < jsonArray.length(); ) {
             jsonobject = jsonArray.getJSONObject(i);

             insered = myDB.inputData(jsonobject.getString("GRUPA_PROJEKTU"),jsonobject.getString("NR_PROJEKTU"),"0");
             if (insered) {
             i++;
             } else {
             Toast.makeText(con, "błąd podczas wczytywania danych", Toast.LENGTH_LONG).show();
             }
             }
             } catch (JSONException e) {
             e.printStackTrace();
             }
             }
             String test = "";
             final Cursor cur = myDB.getGrup();

             while(cur.moveToNext()){
                 lista_grupa_projektowa.add(cur.getString(0));
             }
             cur.moveToFirst();
             selected_item=cur.getString(0);
             cur.close();


            ArrayAdapter<String> GROUPadapter = new ArrayAdapter<String>(con,
                    android.R.layout.simple_spinner_item,lista_grupa_projektowa);
            LayoutInflater inflater = (LayoutInflater)   con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.projekty,null);
            projekt_sp=(Spinner) view.findViewById(R.id.spinner2);
            projekt_sp.setAdapter(GROUPadapter);

/*****/
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                    (con, android.R.layout.select_dialog_item, lista_nazwa_proj);



            //Projekty_DataHandler myDB = new Projekty_DataHandler(con);



/***/
myDB.close();
            projekt_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Projekty_DataHandler myDB = new Projekty_DataHandler(con);
                    Cursor ccc = myDB.getGrup();
                    ccc.moveToPosition(projekt);
                    selected_item=ccc.getString(0);
                        lista_nazwa_proj = new ArrayList<>();
                    Cursor getproj = myDB.getNumber(selected_item);
                        while (getproj.moveToNext()) {
                            lista_nazwa_proj.add(getproj.getString(0));

                    }
                    myDB.close();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
            ImageView dropdown=(ImageView) view.findViewById(R.id.dropdown);
            dropdown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Projekty_DataHandler myDB = new Projekty_DataHandler(con);
                    Cursor cur1 = myDB.getNumber(projekt_sp.getSelectedItem().toString());

                    lista_nazwa_proj = new ArrayList<>();
                    while (cur1.moveToNext()){
                        lista_nazwa_proj.add(cur1.getString(0));
                    }
                    cur1.close();
                    mList = retrievePeople(lista_nazwa_proj);



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
                    actv.showDropDown();
                    myDB.close();
                }
            });

            alertDialog=new AlertDialog.Builder(con)
                    .setTitle("Proszę czekać ")
                    .setMessage("Pobieranie danych ...")
                    .setIcon(android.R.drawable.ic_input_add)
                    .setCancelable(false)
                    .show();

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



            dialogBuilder.setView(view);

            alertDialog =dialogBuilder.create();
            alertDialog.show();


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
