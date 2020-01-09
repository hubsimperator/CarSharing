package com.example.carsharing.JSON;
import android.content.Context;
import android.os.AsyncTask;

import com.example.carsharing.DataHandler.Lista_czas_powiadomien_DataHandler;
import com.example.carsharing.DataHandler.Logs_DataHandler;

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


public class JSON_lista_czas_powiadomien {
    Context con = null;
    public void StartUpdate(Context context) {
        con = context;
        new JSON_lista_czas_powiadomien.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppGetReminders");
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
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
            log.inputLog( "JSON_lista_czas_powiadomien.class 001: "+e.toString());
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
            if(!result.equals("null"))
            {
                try {
                Lista_czas_powiadomien_DataHandler LD = new Lista_czas_powiadomien_DataHandler(con);
                LD.dropdatabase();
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonobject;
                    boolean insert;
                    for (int i = 0; i < jsonArray.length(); ) {
                        jsonobject=jsonArray.getJSONObject(i);
                        insert = LD.inputDataTime(jsonobject.getString("ReminderID").toString(),jsonobject.getString("ReminderInfo").toString());
                        if(insert)
                        {i++;}
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
