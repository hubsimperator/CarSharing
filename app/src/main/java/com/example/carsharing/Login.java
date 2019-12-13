package com.example.carsharing;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(!checkPermissions()){
            setPermissions();
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Login.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                Log.e("Token",mToken);
            }
        });

        try {
            LoginDataHandler LDH = new LoginDataHandler(this);
            Cursor getdata = LDH.getData();
            while (getdata.moveToNext()) {
                if(getdata.getString(3).matches("true"))
                {
                    ((EditText) findViewById(R.id.Logintxt)).setText(getdata.getString(1));
                    ((EditText) findViewById(R.id.Passwordtxt)).setText(getdata.getString(2));
                    ((CheckBox) findViewById(R.id.zapamietajchbox)).setChecked(true);
                }
            }
            LDH.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        ImageView im = (ImageView) findViewById(R.id.Loginbtn);
        final TextView error = (TextView)findViewById(R.id.errortxt);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Logi = ((EditText) findViewById(R.id.Logintxt)).getText().toString();
                final String Haslo = ((EditText) findViewById(R.id.Passwordtxt)).getText().toString();
                if(Logi.equals("") || Haslo.equals(""))
                {
                    error.setText("Login i Hasło nie mogą być puste");
                }
                else
                {
                        LoginDataHandler LDH = new LoginDataHandler(getApplicationContext());
                        LDH.dropdatabase();
                        LDH.inputDataTime(((CheckBox) findViewById(R.id.zapamietajchbox)).isChecked(), Logi, Haslo);
                        LDH.close();

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if(task.isSuccessful()){
                                        String token = task.getResult().getToken();
                                        JSON_Login logowanie = new JSON_Login();
                                        Log.d("token",token);
                                        logowanie.StartUpdate(Logi,Haslo,getApplicationContext(),error,token);
                                    }else
                                    {
                                        JSON_Login logowanie = new JSON_Login();
                                        logowanie.StartUpdate(Logi,Haslo,getApplicationContext(),error,null);
                                    }
                                }
                            });
                }
            }
        });
        }


    private boolean checkPermissions() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void setPermissions() {
        ActivityCompat.requestPermissions((Activity) this, new String[]{
                Manifest.permission.INTERNET , Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.VIBRATE ,Manifest.permission.CAMERA }, 1);
    }
}
