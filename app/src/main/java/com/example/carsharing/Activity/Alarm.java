package com.example.carsharing.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharing.JSON.JSON_anuluj_rezerwacje;
import com.example.carsharing.JSON.JSON_end_trip;
import com.example.carsharing.JSON.JSON_moje_rezerwacje;
import com.example.carsharing.R;

public class Alarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mojarezerwacja);



    }
    public void SetAlarm(Context con){
        Intent intent=new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        intent.putExtra(AlarmClock.EXTRA_HOUR,14);
        intent.putExtra(AlarmClock.EXTRA_MINUTES,14);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE,"WYLACZ REZERWACJE !");

        // intent.putExtra(AlarmClock.ALARM_SEARCH_MODE_LABEL
        ((Activity) con).startActivity(intent);
    }


}
