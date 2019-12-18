package com.example.carsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JSON_lista_przypomnien {

    Context con = null;
ArrayList<String> przypomnienie_id;
ArrayList<String> przypomnienie_nazwa;

    public void StartUpdate(Context context) {
        con = context;
        new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppGetReminders");
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
        String post_result;



        @Override
        protected String doInBackground(String... urls) {
            post_result=POST(urls[0]);
            deserialize_json(post_result);
            return null;
        }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.dismiss();
        Rezerwacja rezerwacja=new Rezerwacja();
  //      rezerwacja.setSpinner(przypomnienie_id,przypomnienie_nazwa);

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

        przypomnienie_id=new ArrayList<>();
        przypomnienie_nazwa=new ArrayList<>();

        JSONArray array = null;
        String dataname;

        try {
            array = new JSONArray(input);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            for (int i = 0; i <array.length(); i++) {
            JSONObject row = array.getJSONObject(i);
            przypomnienie_id.add(row.getString("ReminderID"));
            przypomnienie_nazwa.add(row.getString("ReminderInfo"));
            }

        }
        catch (JSONException e) {                e.printStackTrace();
        }

    }
}
