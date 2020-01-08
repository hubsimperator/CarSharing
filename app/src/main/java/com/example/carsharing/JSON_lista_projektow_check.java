package com.example.carsharing;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
public class JSON_lista_projektow_check{
    AlertDialog alertDialog;
    int CProj;
    Context con = null;
    ArrayList<String> lista_grupa_projektowa;
    String USER="",Token="";
    String currentDate;
    ProgressDialog pg;
    public void StartUpdate( Context context,ProgressDialog progressDialog,String tok) {
        Token = tok;
        pg = progressDialog;
        Log.d("kroki","B1");
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
            Log.d("kroki","B2");
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
            Log.d("kroki","B3");
            return POST(urls[0]);
        }


        @Override
        protected void onPostExecute(String result) {
            boolean insered ;
            Log.d("kroki","B4");
            Projekty_DataHandler myDB = new Projekty_DataHandler(con);


            if (!result.equals("null")) {
                Log.d("kroki","B5");
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonobject;
                    if(jsonArray.length()>20) {
                        Log.d("kroki","B6");
                        myDB.dropdatabase();
                        myDB.close();
                        myDB = new Projekty_DataHandler(con);
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
            Cursor test = myDB.getGrup();
            int testowy = test.getCount();
            testowy = testowy;
            myDB.close();



            pg.hide();
            Log.d("kroki","B7");
            Log.d("kroki","3");
            if(Token.matches("")){
                Log.d("kroki","4");

                Intent intent = new Intent(con, Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                con.startActivity(intent);

            }else {
                Log.d("kroki","4a");
                JSON_SendToken st = new JSON_SendToken();
                st.StartUpdate(USER, Token, con);
                Log.d("kroki","4b");

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
