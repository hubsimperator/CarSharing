package com.example.carsharing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import com.example.carsharing.Activity.OcenaAuta;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CodePhotoBase64 {
    public static Context con1;
    Bitmap selectedImage;
    public  ArrayList<String> path_list=new ArrayList<>();;
    public  ArrayList<String> photo_name=new ArrayList<>();
    ArrayList<String> blob_list=new ArrayList<>();
    public static String[] PATH={"OcenaPrzed0","OcenaPrzed1","OcenaPrzed2"};


    public  ArrayList<Integer> blob_size_list=new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    public void encode2(final Context con, ArrayList<String> fileName) {
        path_list=fileName;
        con1=con;
        String filePath ="";
        new AsyncTask<Void, Void, ArrayList>() {
            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }
            @Override
            protected ArrayList<String> doInBackground(Void... voids) {
                for(int i=0;i<path_list.size();i++) {
                    String [] s=path_list.get(i).split("/");
                    Log.d("aa",s[4]);
                    Log.d("aa",s[s.length-1]);
                     photo_name.add(s[s.length-1]);

                   if(selectedImage != null) {
                       selectedImage.recycle();
                   }
                    selectedImage = BitmapFactory.decodeFile(path_list.get(i));
                    int width = selectedImage.getWidth();
                    int height = selectedImage.getHeight();
//800 600
                    Bitmap resizedBitmap;
                    if (width > height) {
                        resizedBitmap = Bitmap.createScaledBitmap(
                                selectedImage, 800, 600, false);
                    } else {
                        resizedBitmap = Bitmap.createScaledBitmap(
                                selectedImage, 600, 800, false);
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String strBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    byte[] b = new byte[0];
                    try {
                        b = strBase64.getBytes("UTF-8");
                        Log.d("a",Integer.toString(b.length));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    blob_list.add(strBase64);
                    blob_size_list.add(b.length);
               }
                return blob_list;// strBase64;
            }
            @Override
            protected void onPostExecute(ArrayList arrayList) {
                OcenaAuta ocenaAuta=new OcenaAuta();
                ocenaAuta.setBlobImage(arrayList,blob_size_list,photo_name);
            }
        }.execute();
    }
    @SuppressLint("StaticFieldLeak")
    public void encode_morethanNandroid(final Context con, final ArrayList<Integer> file) {
        con1=con;
        new AsyncTask<Void, Void, ArrayList>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<String> doInBackground(Void... voids) {

                for(int i=0;i<file.size();i++) {

                    if(selectedImage != null) {
                        selectedImage.recycle();
                    }
                    Obiekt_Photo obiekt_photo=new Obiekt_Photo();
                    selectedImage =obiekt_photo.getPhoto(i); //BitmapFactory.decodeFile(path_list.get(i));
                    int width = selectedImage.getWidth();
                    int height = selectedImage.getHeight();
//800 600

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                    String format = simpleDateFormat.format(new Date());

                    photo_name.add(PATH[file.get(i)]+format);

                    Bitmap resizedBitmap;
                    if (width > height) {
                        resizedBitmap = Bitmap.createScaledBitmap(
                                selectedImage, 800, 600, false);
                    } else {
                        resizedBitmap = Bitmap.createScaledBitmap(
                                selectedImage, 600, 800, false);
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String strBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    byte[] b = new byte[0];
                    try {
                        b = strBase64.getBytes("UTF-8");
                        Log.d("a",Integer.toString(b.length));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    blob_list.add(strBase64);
                    blob_size_list.add(b.length);

                }
                return blob_list;// strBase64;
            }

            @Override
            protected void onPostExecute(ArrayList arrayList) {
                OcenaAuta ocenaAuta=new OcenaAuta();
                ocenaAuta.setBlobImage(arrayList,blob_size_list,photo_name);
            }
        }.execute();

    }
}
