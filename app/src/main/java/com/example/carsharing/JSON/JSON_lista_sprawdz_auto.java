package com.example.carsharing.JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.DataHandler.Projekty_DataHandler;
import com.example.carsharing.Other.Model_comment;

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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class JSON_lista_sprawdz_auto {
    Context con = null;

public void StartUpdate(Context context) {
        con = context;

        new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppGetCommentsList");

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

        ArrayList<Model_comment> modelComment = new ArrayList<>();

        if (!result.equals("null")) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonobject;
                if(jsonArray.length()>20) {

                    for (int i = 0; i < jsonArray.length(); ) {
                        jsonobject = jsonArray.getJSONObject(i);

                                jsonobject.getString("CommentID");
                                jsonobject.getString("Comment");
                                jsonobject.getString("Status");

                            i++;

                    }
                }
            } catch (Exception e) {
                Logs_DataHandler log = new Logs_DataHandler(con);
                log.inputLog( "JSON_lista_projektów_check 002: "+e.toString());
                log.close();
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