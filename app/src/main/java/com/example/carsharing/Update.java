package com.example.carsharing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import androidx.core.content.FileProvider;


public class Update extends Activity {
public String ppath;
    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;


    // File url to download
    public static String file_url;//=extras.getString("BookingId"); //= "https://nc.sng.com.pl/index.php/s/JXfBTtXBciLK2AI/download?path=%2F&files=CarSharing.apk";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras= getIntent().getExtras();
        file_url=extras.getString("url");

        setContentView(R.layout.activity_main);

        new HttpAsyncTask2().execute(file_url);

    }

    private class HttpAsyncTask2 extends AsyncTask<String, String, String> {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }


        @Override
        protected String doInBackground(String... urls) {
            int count;
            try {
                URL url = new URL(urls[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                ppath=Environment
                        .getExternalStorageDirectory().getPath()+ "/aktualizacjaCarsharing.apk";

                Log.d("Path aktualizacji",ppath);
                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/aktualizacjaCarsharing.apk");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                   publishProgress("" + Integer.toString((int)((total * 100) / lenghtOfFile)));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String result) {

            File toInstall = new File(ppath,"/aktualizacjaCarsharing.apk");
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(Update.this, BuildConfig.APPLICATION_ID + ".fileprovider", toInstall);
                intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Uri apkUri = Uri.fromFile(toInstall);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            Update.this.startActivity(intent);
/*
            dismissDialog(progress_bar_type);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //Uri photoURI = FileProvider.getUriForFile(Update.this, Update.this.getApplicationContext().getPackageName() + ".provider",new File(ppath));

            //intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
            intent.setDataAndType()
            intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
        }
    }

    /**
     * Showing Dialog
     * */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Pobieranie aktualizacji. Proszę czekać...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

}