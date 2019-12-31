package com.example.carsharing;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import android.os.Build.VERSION;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSON_SendLog {
    Context context;
    String Exception="";
    String USER="";


    public void Send(Context con) {
        context = con;
        LoginDataHandler lo = new LoginDataHandler(con);
        Cursor login = lo.getData();
        while (login.moveToNext()){
            USER = login.getString(1);
        }
        Log.d("user",USER);
        lo.close();
        String erid =" ";
        Logs_DataHandler ErrorLog = new Logs_DataHandler(con);
        Cursor Err = ErrorLog.getData();
        while (Err.moveToNext()){
            erid = Err.getString(0);
            Exception=Err.getString(1);
        }
        if(!erid.equals(" ")){
            ErrorLog.deletewpis(erid);
            //tutaj zapodajesz adres json
            new JSON_SendLog.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppAddLogError");
            ErrorLog.close();
        }
    }



    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            //tutaj wrzucasz elementy json
            jsonObject.accumulate("UserId",USER);
            jsonObject.accumulate("Device", Build.DEVICE);
            jsonObject.accumulate("SysVersion", "SDK: "+String.valueOf( Build.VERSION.SDK_INT));
            jsonObject.accumulate("Logvalue",Exception);

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
            Logs_DataHandler log = new Logs_DataHandler(context);
            log.inputLog( "JSON_Send.class + 001: "+e.toString());
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
//tutaj piszesz obsługę odbioru json
            if(result.equals("\"true\"")) {
                Send(context);
                Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();

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


