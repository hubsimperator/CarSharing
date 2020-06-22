package com.example.carsharing.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.carsharing.JSON.JSON_qrscanner;
import com.example.carsharing.R;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    String SelectedEit_Resource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        SelectedEit_Resource = extras.getString("SelectedEit_Resource");
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener(){
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

    }

    @Override
    public void handleResult(Result result) {
        Log.d("QRCODe",result.toString());
    //    Toast.makeText(QRScanner.this,result.toString(),Toast.LENGTH_LONG).show();
       if(result.toString().equals(SelectedEit_Resource)){
           JSON_qrscanner json_qrscanner=new JSON_qrscanner();
           json_qrscanner.StartUpdate(QRScanner.this,result.toString());
           //finish();

       }else{
           AlertDialog alertDialog;
           alertDialog= new AlertDialog.Builder(QRScanner.this)
                   .setTitle("Informacja")
                   .setMessage("Twoja rezerwacja nie dotyczy wskazanego auta")
                   .setIcon(R.drawable.confirm)
                   .setCancelable(true)
                   .show();

           alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
               @Override
               public void onCancel(DialogInterface dialog) {
                   finish();
               }
           });

       }


        //  onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
        Log.d("Kamera","Kamera start");
    }
}