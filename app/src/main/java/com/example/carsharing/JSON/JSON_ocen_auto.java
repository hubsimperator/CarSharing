package com.example.carsharing.JSON;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.example.carsharing.Activity.OcenaAuta;
import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.Activity.MojeRezerwacje;
import com.example.carsharing.R;
import com.example.carsharing.Activity.ZmienCzasRezerwacji;

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
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class JSON_ocen_auto {


    Context con ;

    public static String BookingID = null;
    public static ArrayList<Integer> CommentID=new ArrayList<>();
    public static ArrayList<String> Note=new ArrayList<>();
    public static ArrayList<String> Photo=new ArrayList<>();
    public static ArrayList<Integer> PhotoSize=new ArrayList<>();
    public static ArrayList<String> PhotoName=new ArrayList<>();
    String Results="";


    public void StartUpdate(String _BookingID, ArrayList<Integer> _CommentID,ArrayList<String> _Note,ArrayList<String> _Photo,ArrayList<Integer> _PhotoSize,ArrayList<String> _PhotoName, Context context) {
        con = context;
        BookingID=_BookingID;
        CommentID=_CommentID;;

        PhotoSize=_PhotoSize;
        PhotoName=_PhotoName;

        Note=_Note;
        Photo=_Photo;

        try{
            for(int i=0;i<CommentID.size();i++){
                if(PhotoName.get(i).length()>5) {
                    String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())+i);
                    PhotoName.set(i,PhotoName.get(i)+timeStamp);
                }
            }
        }catch (IndexOutOfBoundsException ne){

            for(int i=0;i<CommentID.size();i++){
                    Photo.add("");
                    PhotoSize.add(0);
                    PhotoName.add("");
            }
        }
        new JSON_ocen_auto.HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/CsAppInsertComment");
    }

    public String POST(String url,String _BookingID,Integer _CommentID,String _Note,String _Photo,String _PhotoName,Integer _ByteSize) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("BookingID",_BookingID);
            jsonObject.accumulate("CommentID",_CommentID);
            jsonObject.accumulate("Comment",_Note);
            jsonObject.accumulate("BlobImage",_Photo);
            jsonObject.accumulate("ByteSize",_ByteSize);

            jsonObject.accumulate("PhotoFileName",_PhotoName);


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
            log.inputLog( "JSON_ocen_auto.class 001: "+e.toString());
            log.close();
        }
        Results+=result;
        return result;
    }


    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(con);
            progressDialog.setMessage("Wysyłanie danych.Proszę czekać");
            progressDialog.show();
            /*
            alertDialog=new AlertDialog.Builder(con)
                    .setTitle("Proszę czekać ")
                    .setMessage("Wysyłanie danych ...")
                    .setIcon(android.R.drawable.ic_menu_upload)
                    .setCancelable(false)
                    .show();\

             */
        }

        @Override
        protected String doInBackground(String... urls) {
            for (int i =0; i<CommentID.size();i++){
                String s=POST(urls[0],BookingID,CommentID.get(i),Note.get(i),Photo.get(i),PhotoName.get(i),PhotoSize.get(i));
                Log.d("aa","a");
            }
    Log.d("aa","a");
            return null ;
        }


        @Override
        protected void onPostExecute(String result) {
           // alertDialog.dismiss();
            progressDialog.dismiss();
            OcenaAuta ocenaAuta=new OcenaAuta();
            ocenaAuta.startTrip(Results);

          //  alertDialog.dismiss();
          /*
            if (result.contains("False")) {
                alertDialog = new AlertDialog.Builder(con)
                        .setTitle("Błąd")
                        .setMessage("Nie można dokonać zmiany na rezerwacji")
                        .setIcon(R.drawable.cancel)
                        .setCancelable(true)
                        .show();
            }
            else if (result.contains("True")) {
                alertDialog = new AlertDialog.Builder(con)
                        .setTitle("Potwierdzenie ")
                        .setMessage("Dokonano zmiany na rezerwacji")
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                //alertDialog.dismiss();
                                MojeRezerwacje mojeRezerwacje=new MojeRezerwacje();
                                mojeRezerwacje.finish();
                                ZmienCzasRezerwacji zmienCzasRezerwacji=new ZmienCzasRezerwacji();
                                zmienCzasRezerwacji.finish();
                                JSON_moje_rezerwacje json_moje_rezerwacje=new JSON_moje_rezerwacje();
                                json_moje_rezerwacje.StartUpdate("","",con);
                            }
                        })
                        .setIcon(R.drawable.confirm)
                        .setCancelable(true)
                        .show();
            }
            else{
                alertDialog = new AlertDialog.Builder(con)
                        .setTitle("Błąd")
                        .setMessage(result)
                        .setIcon(R.drawable.cancel)
                        .setCancelable(true)
                        .show();
            }

           */
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
