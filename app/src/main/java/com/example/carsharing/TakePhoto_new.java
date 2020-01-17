package com.example.carsharing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.example.carsharing.Activity.OcenaAuta;
import com.example.carsharing.Other.Update;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
@RequiresApi(api = Build.VERSION_CODES.N)
public class TakePhoto_new extends AppCompatActivity {

    ArrayList<String> path_list;
    private static final int CAMERA_PHOTO = 111;
    public  Uri imageToUploadUri;
    public static String path=null;
    Integer param;
    public static String[] PATH={"OcenaPrzed0","OcenaPrzed1","OcenaPrzed2"};
    Bitmap reducedSizeBitmap;

    public static Context con1;
    public static Integer parametr;
    public static String podparametr;
    public static String mslink;

    public void TakePhoto(Context con,Integer param,String popdaram, String _mslink){
        con1=con;
        parametr=param;
        podparametr=popdaram;
        mslink=_mslink;
        znajdzKamere();
        captureCameraImage();
    }

    private int znajdzKamere(){
        int cameraID= -1;
        for(int i = 0; i< Camera.getNumberOfCameras(); i++){
            Camera.CameraInfo info=new Camera.CameraInfo();
            Camera.getCameraInfo(i,info);

            if(info.facing== Camera.CameraInfo.CAMERA_FACING_BACK){
                Log.d("TAG","*********************");
                Log.d("TAG","Jest kamera tylna");
                Log.d("TAG","*********************");
                cameraID=i;
                break;
            }else if(info.facing== Camera.CameraInfo.CAMERA_FACING_FRONT){
                Log.d("TAG","*********************");
                Log.d("TAG","Jest kamera przednia");
                Log.d("TAG","*********************");
                cameraID=i;
                break;
            }


        }
        return cameraID;
    }
    private void captureCameraImage() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // dla wersji ponizej N
            if (con1 instanceof Activity) {
                try {
                    Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), PATH[parametr]+".jpg");
                    path=f.toString();
                    chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    ((Activity) con1).startActivityForResult(chooserIntent, CAMERA_PHOTO);
                }
                catch (FileUriExposedException fileUriExposedException){
                    Log.d("Alarm","Nie mozna wlaczyc aparatu");
                }
            } else {

            }
        }
        else{ // dla wersji powyzej N android

            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put(MediaStore.Images.Media.DISPLAY_NAME,"hubsonkrol232.jpg");

            imageToUploadUri = con1.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);
            ((Activity) con1).startActivityForResult(captureIntent, CAMERA_PHOTO);
        }

    }

    public void activityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == CAMERA_PHOTO && resultCode == Activity.RESULT_OK) {
            if((imageToUploadUri != null) &&(Build.VERSION.SDK_INT < Build.VERSION_CODES.N)){
                con1.getContentResolver().notifyChange(imageToUploadUri, null);
            }
            OcenaAuta ocenaAuta=new OcenaAuta();
            ocenaAuta.setphoto(0,parametr);

        }else {
            Toast.makeText(con1, "Error while capturing Image", Toast.LENGTH_LONG).show();
        }
    }

    public void showPicOrTakeNew(Integer _param,final Context con){
        param=_param;
        con1=con;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // dla wersji ponizej N

            File f = new File(Environment.getExternalStorageDirectory(), PATH[param] + ".jpg");
            path = f.toString();
            imageToUploadUri = Uri.fromFile(f);
            reducedSizeBitmap = getBitmap(imageToUploadUri.getPath());
        }
        else {
            // dla wersji powyzej  N
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put(MediaStore.Images.Media.DISPLAY_NAME,"hubsonkrol232.jpg");

            imageToUploadUri = con1.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            try {
                reducedSizeBitmap = MediaStore.Images.Media.getBitmap(con1.getContentResolver(), imageToUploadUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(reducedSizeBitmap != null){
            AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(con1,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
            dialogBuilder1.setNegativeButton("Powrót",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("Usuń zdjęcie",null
                    );



            LayoutInflater inflater1 = (LayoutInflater) con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView1 = inflater1.inflate(R.layout.paint_layout, null);
            dialogBuilder1.setView(dialogView1);

            final ImageView img=(ImageView) dialogView1.findViewById(R.id.imageViewid);
            img.setImageBitmap(reducedSizeBitmap);
            final AlertDialog alertDialog = dialogBuilder1.create();
            alertDialog.show();

            Button removePhoto=alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            removePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("k","h");
                    OcenaAuta ocenaAuta=new OcenaAuta();
                    ocenaAuta.setphoto(1,param);
                    alertDialog.dismiss();

                }
            });


            //wyslij button
            Button positiveButton=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CodePhotoBase64 cpb=new CodePhotoBase64();
                    //   cpb.encode2(con1,path,parametr,podparametr,mslink,null,null);
                    alertDialog.dismiss();
                }
            });


        }
    }

    private static Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = con1.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = con1.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch ( IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }



}