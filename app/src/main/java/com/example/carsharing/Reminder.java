package com.example.carsharing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class Reminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public int year,month,day,hour,minute;
    public String dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    public static EditText poczatek_et;
    public static EditText koniec_et;

    public static String data_poczatkowa;
    public static String data_koncowa;

    public static TextView label_samochod_tv;
    public static TextView wybrany_samochod_tv;

    public static ImageView rezerwuj_bt;


    public boolean start_date=false;
    public boolean end_date=false;

    View view;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rezerwacja);

        label_samochod_tv=(TextView) findViewById(R.id.label_wybrany_tv);
        wybrany_samochod_tv=(TextView) findViewById(R.id.wybor_samochodu_tv);
        rezerwuj_bt=(ImageView) findViewById(R.id.rezerwuj_bt);

        ImageView search_bt = (ImageView) findViewById(R.id.search_bt);
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    try{
                //    Log.d("Data",data_poczatkowa);
                  //  Log.d("Data",data_koncowa);
                JSON_lista_samochodow json_lista_samochodow = new JSON_lista_samochodow();
                json_lista_samochodow.StartUpdate("a","b", Reminder.this);


              //  }
             //   catch (NullPointerException ne){
             //       Toast.makeText(Rezerwacja.this,"Wprowadź wszystkie dane !",Toast.LENGTH_SHORT).show();
             //   }
            }
        });

        poczatek_et=(EditText) findViewById(R.id.poczatek_et);
        poczatek_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(data_poczatkowa==null)){
                    poczatek_et.setText(data_poczatkowa);
                }
                start_date=true;
                end_date=false;
                Calendar c= Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Reminder.this, Reminder.this,year,month,day);
                datePickerDialog.show();
            }
        });

        koniec_et=(EditText) findViewById(R.id.koniec_et);
        koniec_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date=false;
                end_date=true;
                Calendar c= Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Reminder.this, Reminder.this,year,month,day);
                datePickerDialog.show();
            }
        });


        ImageView back_bt = (ImageView) findViewById(R.id.back_bt);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
}
public void wyswietl_liste(Context con, final ArrayList<String> lista){

    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con).setPositiveButton("Zamknij", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss();
        }
    });
    LayoutInflater inflater = (LayoutInflater)   con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = inflater.inflate(R.layout.dostepne_samochody,null);
    dialogBuilder.setView(view);


    final ListView ch1 = (ListView) view.findViewById(R.id.checkable_list);
    ch1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    ArrayAdapter<String> adapter = new ArrayAdapter<String >(con, R.layout.listlayoutitem, R.id.txt_lan,lista);
    ch1.setAdapter(adapter);
    ch1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            label_samochod_tv.setVisibility(View.VISIBLE);
           wybrany_samochod_tv.setText(lista.get(position));
           rezerwuj_bt.setVisibility(View.VISIBLE);

        }
    });

    alertDialog =dialogBuilder.create();
    alertDialog.show();

}

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(year<10){
            yearFinal="0"+Integer.toString(year);
        }else{
            yearFinal=Integer.toString(year);
        }

        if(month<10){
            monthFinal="0"+Integer.toString(month);
        }else{
            monthFinal=Integer.toString(month);
        }

        if(dayOfMonth<10){
            dayFinal="0"+Integer.toString(dayOfMonth);
        }else{
            dayFinal=Integer.toString(dayOfMonth);
        }

     Calendar c= Calendar.getInstance();
     hour=c.get(Calendar.HOUR_OF_DAY);
     minute=c.get(Calendar.MINUTE);

     TimePickerDialog timePickerDialog = new TimePickerDialog(Reminder.this, Reminder.this,hour,minute,DateFormat.is24HourFormat(this));
     timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(hourOfDay<10){
            hourFinal="0"+Integer.toString(hourOfDay);
        }else{
            hourFinal=Integer.toString(hourOfDay);
        }
        if(minute<10){
            minuteFinal="0"+Integer.toString(minute);
        }else{
            minuteFinal=Integer.toString(minute);
        }

        String data=(dayFinal)+"-"+(monthFinal)+"-"+(yearFinal);
        String godzina=(hourFinal)+":"+(minute);
       if(start_date) {
           poczatek_et.setText(data + "  " + godzina);
           data_poczatkowa=yearFinal+"-"+monthFinal+"-"+dayFinal+" "+(hourFinal)+":"+(minute)+":00";
       }else{
           koniec_et.setText(data + "  " + godzina);
           data_koncowa=yearFinal+"-"+monthFinal+"-"+dayFinal+" "+(hourFinal)+":"+(minute)+":00";
       }


    }
}

