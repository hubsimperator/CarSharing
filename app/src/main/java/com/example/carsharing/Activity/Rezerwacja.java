package com.example.carsharing.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.appcompat.app.AppCompatActivity;

import android.text.format.DateFormat;
import android.widget.Toast;

import com.example.carsharing.DataHandler.Lista_czas_powiadomien_DataHandler;
import com.example.carsharing.DataHandler.Lista_parking_DataHandler;
import com.example.carsharing.DataHandler.LoginDataHandler;
import com.example.carsharing.DataHandler.Logs_DataHandler;
import com.example.carsharing.DataHandler.Projekty_DataHandler;
import com.example.carsharing.DostepnoscListAdapter;
import com.example.carsharing.JSON.JSON_get_default_project;
import com.example.carsharing.JSON.JSON_lista_przypomnien;
import com.example.carsharing.JSON.JSON_lista_samochodow;
import com.example.carsharing.JSON.JSON_potwierdzenie_rezerwacji;
import com.example.carsharing.Obiekt_Dostepnosc;
import com.example.carsharing.Other.ProjektWybor;
import com.example.carsharing.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Rezerwacja extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    int DEFAULT_ADDTIME_MIN=60;
    String DEFAULT_DISTANCE="10";
    String DEFAULT_PARKING_NAME="Wałowa";
    String DEFAULT_PRZYPOMNIENIE="15 minut";
    String DEFAULT_GRUPA_PROJ;
    String DEFAULT_NUMER_PROJ;


    int year,month,day,hour,minute;
    String dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    public static EditText poczatek_et;
    public static EditText koniec_et;
    public static EditText projekt_et;
    public static EditText subject_et;
    public static EditText dystans_et;

    public static String data_poczatkowa;
    String data_bez_godzin;
    String godzina_poczatkowa;
    String data_koncowa;
    String parking="";
    public static String eit_Resource="";
    public static String grupa_projektu;
    public static String nazwa_projektu;
    public static String przypomnienie_id;

    public static TextView label_samochod_tv;
    public static TextView wybrany_samochod_tv;
    public static TextView label_minuty_tv;

    public static ImageView rezerwuj_bt;

    public static Spinner minuty_sp;
    public static Spinner parking_sp;

    public static ArrayList<String> parkingi;

    public static ArrayList<String> grupa_proj;
    public static ArrayList<String> numer_proj;


    Integer position=null;


    boolean start_date=false;
     boolean end_date=false;

    View view;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rezerwacja);
        Bundle extras= getIntent().getExtras();
        DEFAULT_PARKING_NAME=extras.getString("nearestParking");

        JSON_lista_przypomnien json_lista_przypomnien=new JSON_lista_przypomnien();
        json_lista_przypomnien.StartUpdate(Rezerwacja.this);

        label_minuty_tv=(TextView) findViewById(R.id.label_przypomnienie_tv);
        label_samochod_tv=(TextView) findViewById(R.id.label_wybrany_tv);
        wybrany_samochod_tv=(TextView) findViewById(R.id.wybor_samochodu_tv);
        rezerwuj_bt=(ImageView) findViewById(R.id.rezerwuj_bt);
        projekt_et=(EditText) findViewById(R.id.notatka);
        subject_et=(EditText) findViewById(R.id.tytul_et);
        minuty_sp=(Spinner) findViewById(R.id.spinner);
        parking_sp=(Spinner) findViewById(R.id.parking_sp);
        dystans_et=(EditText) findViewById(R.id.dystans);


        parkingi=new ArrayList<>();

try {
    JSON_get_default_project gdp = new JSON_get_default_project();
    gdp.StartUpdate(Rezerwacja.this);
}catch (Exception e){
    Logs_DataHandler log = new Logs_DataHandler(this);
    log.inputLog( "Rezerwacja.class 099: "+e.toString());
    log.close();
}

grupa_proj=new ArrayList<>();
numer_proj=new ArrayList<>();
        try {
            Projekty_DataHandler PDH = new Projekty_DataHandler(this);
            Cursor getdata = PDH.getGrup();
            while (getdata.moveToNext()) {

                grupa_proj.add(getdata.getString(0));
            }
            PDH.close();
        } catch (Exception e){
            e.printStackTrace();
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog( "Rezerwacja.class 0000: "+e.toString());
            log.close();
        }




        try {
            Lista_parking_DataHandler LDH = new Lista_parking_DataHandler(this);
            Cursor getdata = LDH.getData();
            while (getdata.moveToNext()) {

                    parkingi.add(getdata.getString(1));

            }
            LDH.close();
        } catch (Exception e){
            e.printStackTrace();
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog( "Rezerwacja.class 001: "+e.toString());
            log.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, parkingi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        parking_sp.setAdapter(adapter);

        parking_sp.setSelection(parkingi.indexOf(DEFAULT_PARKING_NAME));
        parking_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parking=parking_sp.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> powiadomienie_nazwa=new ArrayList<>();
        final ArrayList<String> powiadomienie_id=new ArrayList<>();



        try {
            Lista_czas_powiadomien_DataHandler LDH = new Lista_czas_powiadomien_DataHandler(Rezerwacja.this);
            Cursor getdata = LDH.getData();
            while (getdata.moveToNext()) {
                powiadomienie_nazwa.add(getdata.getString(2));
                powiadomienie_id.add(getdata.getString(1));

            }
            LDH.close();
        } catch (Exception e){
            e.printStackTrace();
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog( "Rezerwacja.class 002: "+e.toString());
            log.close();
        }


       adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, powiadomienie_nazwa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuty_sp.setAdapter(adapter);
        minuty_sp.setSelection(powiadomienie_nazwa.indexOf(DEFAULT_PRZYPOMNIENIE));
        przypomnienie_id=powiadomienie_id.get(powiadomienie_nazwa.indexOf(DEFAULT_PRZYPOMNIENIE));

        minuty_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              String _przypomnienie=minuty_sp.getSelectedItem().toString();
                przypomnienie_id=powiadomienie_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ImageView search_bt = (ImageView) findViewById(R.id.search_bt);
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sprawdz_czy_data_poprawna()) {
                    if (sprawdz_czy_dane_niepuste(1)) {
                        JSON_lista_samochodow json_lista_samochodow = new JSON_lista_samochodow();
                        json_lista_samochodow.StartUpdate(data_poczatkowa, data_koncowa, parking, dystans_et.getText().toString(), Rezerwacja.this);
                    }
                }
            }
        });

        projekt_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ProjektWybor projektWybor=new ProjektWybor();
              projektWybor.WyborProjektu(Rezerwacja.this,0);
              //  JSON_lista_projektow2 json_lista_projektow=new JSON_lista_projektow2();
               // json_lista_projektow.StartUpdate(0,Rezerwacja.this);
            }
        });



        dystans_et.setText(DEFAULT_DISTANCE);

        start_date=false;
        end_date=false;

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
             /*
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);

              */
                String ss[]=data_poczatkowa.split(" ");
                String s[]=ss[0].split("-");
                year=Integer.valueOf(s[0]);
                month=Integer.valueOf(s[1])-1;
                day=Integer.valueOf(s[2]);
                Log.d("a","f");

                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Rezerwacja.this,Rezerwacja.this,year,month,day);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });



        koniec_et=(EditText) findViewById(R.id.koniec_et);
        koniec_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date=false;
                end_date=true;
               long a=0;
                Calendar c= Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);
                a=c.getTimeInMillis();
                if(poczatek_et.length()>3) {
                    a = convert_epoch_date();
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(Rezerwacja.this,Rezerwacja.this,year,month,day);
                datePickerDialog.getDatePicker().setMinDate(a);
                datePickerDialog.show();
            }
        });


        setCurrentDate();


        try {
            LoginDataHandler LDH = new LoginDataHandler(this);
            Cursor getdata = LDH.getData();
            while (getdata.moveToNext()) {
                    subject_et.setHint(getdata.getString(1)+" rezerwacja");
            }
            LDH.close();
        } catch (Exception e){
            e.printStackTrace();
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog( "Rezerwacja.class 003: "+e.toString());
            log.close();
        }


        rezerwuj_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sprawdz_czy_dane_niepuste(0)) {
                    if(subject_et.length()<1) subject_et.setText(subject_et.getHint());
                    alertDialog = new AlertDialog.Builder(Rezerwacja.this)
                            .setTitle("Rezerwacja")
                            .setMessage("Czy napewno chcesz dokonać rezerwacji?")
                            .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!eit_Resource.matches("") ) {
                                        JSON_potwierdzenie_rezerwacji json_potwierdzenie_rezerwacji = new JSON_potwierdzenie_rezerwacji();
                                        json_potwierdzenie_rezerwacji.StartUpdate(data_poczatkowa, data_koncowa, "0", subject_et.getText().toString(), eit_Resource, "", przypomnienie_id, "", "", "", "", grupa_projektu, nazwa_projektu, Rezerwacja.this);
                                    }else{
                                        Toast.makeText(Rezerwacja.this,"Najpierw kliknij w lupę i wybierz auto",Toast.LENGTH_LONG).show();
                                        alertDialog.dismiss();
                                    }
                                }
                            })
                            .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
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

    public void getLocationFromAddress(String strAddress) throws IOException {

        Geocoder coder = new Geocoder(Rezerwacja.this);
        List<Address> address;
            address = coder.getFromLocationName(strAddress,5);
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
    }

public void setCurrentDate(){
    Calendar c= Calendar.getInstance();
    year=c.get(Calendar.YEAR);
    month=c.get(Calendar.MONTH)+1;
    day=c.get(Calendar.DAY_OF_MONTH);
    hour=c.get(Calendar.HOUR_OF_DAY);
    minute=c.get(Calendar.MINUTE);

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

    if(day<10){
        dayFinal="0"+Integer.toString(day);
    }else{
        dayFinal=Integer.toString(day);
    }

    if(hour<10){
        hourFinal="0"+Integer.toString(hour);
    }else{
        hourFinal=Integer.toString(hour);
    }
    if(minute<10){
        minuteFinal="0"+Integer.toString(minute);
    }else{
        minuteFinal=Integer.toString(minute);
    }

    String data=(yearFinal)+"-"+(monthFinal)+"-"+(dayFinal);
    String godzina=(hourFinal)+":"+(minuteFinal);
    data_poczatkowa=data+" "+godzina+":00";
    poczatek_et.setText(data+" "+godzina+":00");

    c.add(Calendar.MINUTE, DEFAULT_ADDTIME_MIN);
    hour=c.get(Calendar.HOUR_OF_DAY);
    minute=c.get(Calendar.MINUTE);
    if(hour<10){
        hourFinal="0"+Integer.toString(hour);
    }else{
        hourFinal=Integer.toString(hour);
    }
    if(minute<10){
        minuteFinal="0"+Integer.toString(minute);
    }else{
        minuteFinal=Integer.toString(minute);
    }
    godzina=(hourFinal)+":"+(minuteFinal);
    data_koncowa=data+" "+godzina+":00";

    koniec_et.setText(data+" "+godzina+":00");
}


public long convert_epoch_date(){
    String[] s= (poczatek_et.getText().toString()).split(" ");
   // String string_date = data_bez_godzin;
    String string_date=s[0];
    long milliseconds=0;
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    try {
        Date d = f.parse(string_date);
        milliseconds = d.getTime();
    } catch (ParseException e) {
        e.printStackTrace();
        Logs_DataHandler log = new Logs_DataHandler(this);
        log.inputLog( "Rezerwacja.class 004: "+e.toString());
        log.close();
    }
    return milliseconds;
}

public boolean sprawdz_czy_data_poprawna(){

       String s1= poczatek_et.getText().toString();
    String s2= koniec_et.getText().toString();

    try {
        Date date1=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(s1);
        Date date2=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(s2);
        if(date1.compareTo(date2)>0){
            alertDialog = new AlertDialog.Builder(Rezerwacja.this)
                    .setTitle("Uwaga")
                    .setMessage("Data końcowa musi być większa niż data początkowa !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }else return true;

    } catch (ParseException e) {
        e.printStackTrace();
        return false;
    }
    }

public boolean sprawdz_czy_dane_niepuste(int param) {
    if (param == 0) {
        if ((subject_et.length() > 1 || subject_et.getHint().length()>1) && poczatek_et.length() > 1 && koniec_et.length() > 1 && projekt_et.length() > 1) {
            return true;
        } else {
            alertDialog = new AlertDialog.Builder(Rezerwacja.this)
                    .setTitle("Uwaga")
                    .setMessage("Pola : 'Tytuł','Projekt','Początek','Koniec' oraz 'Wybrany samochód' nie mogą być puste !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }
    }
    else{
            if (poczatek_et.length() > 1 && koniec_et.length() > 1) {
                return true;
            } else {
                alertDialog = new AlertDialog.Builder(Rezerwacja.this)
                        .setTitle("Uwaga")
                        .setMessage("Pola : 'Początek'i'Koniec' nie mogą być puste !")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return false;
            }

    }
}

public ArrayList<Obiekt_Dostepnosc> dostepnoscList=new ArrayList<>();
public DostepnoscListAdapter adapter;

public void wyswietl_dostepnosc(Context con,ArrayList<Obiekt_Dostepnosc> _dostepnoscListsc,String _parking){

    Toast.makeText(con,"Brak dostępnych samochodów w wybranym terminie. Sprawdzam dostępność w najbliższym czasie ...",Toast.LENGTH_LONG).show();

    dostepnoscList=_dostepnoscListsc;
    adapter=new DostepnoscListAdapter(con,R.layout.dostepnosc_layout_adapter,_dostepnoscListsc,con);

    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con).setPositiveButton("Potwierdź", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(position != null){
                label_samochod_tv.setVisibility(View.VISIBLE);
                wybrany_samochod_tv.setText(dostepnoscList.get(position).getSamochod());
                eit_Resource=dostepnoscList.get(position).getSamochodID();
                rezerwuj_bt.setVisibility(View.VISIBLE);
                minuty_sp.setVisibility(View.VISIBLE);
                label_minuty_tv.setVisibility(View.VISIBLE);
                poczatek_et.setText(dostepnoscList.get(position).getStart_date()+":00");
                data_poczatkowa=dostepnoscList.get(position).getStart_date()+":00";
                koniec_et.setText(dostepnoscList.get(position).getEnd_date()+":00");
                data_koncowa=dostepnoscList.get(position).getEnd_date()+":00";

                alertDialog.dismiss();
            }

        }
    })
            .setNeutralButton("Powrót",null);

    LayoutInflater inflater = (LayoutInflater)   con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = inflater.inflate(R.layout.dostepnosc_layout,null);
    dialogBuilder.setView(view);

    TextView info=(TextView) view.findViewById(R.id.dostepneAutaParking);
    String s=info.getText().toString()+_parking;
    info.setText(s);

    final ListView mListView = (ListView) view.findViewById(R.id.listview);
    mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    mListView.setAdapter(adapter);

    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int _position, long id) {
            for (int i = 0; i < mListView.getChildCount(); i++) {
                if(_position == i ){
                    position=_position;
                    mListView.getChildAt(i).setBackgroundColor(Color.GREEN);
                }else{
                    mListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
    });

    alertDialog =dialogBuilder.create();
    alertDialog.show();

    Log.d("a","a");
    }


public void wyswietl_liste(Context con, final ArrayList<String> lista, final ArrayList<String> lista_id){

    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con).setPositiveButton("Potwierdź", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss();
        }
    })
            .setNeutralButton("Powrót",null)
            ;
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
            eit_Resource=lista_id.get(position);
            label_samochod_tv.setVisibility(View.VISIBLE);
           wybrany_samochod_tv.setText(lista.get(position));
           rezerwuj_bt.setVisibility(View.VISIBLE);
           minuty_sp.setVisibility(View.VISIBLE);
           label_minuty_tv.setVisibility(View.VISIBLE);

        }
    });

    alertDialog =dialogBuilder.create();
    alertDialog.show();

}

public void wyswietl_projekt(Context con,String _proj,String _grupa_projektu){
        projekt_et.setText(_proj);
        nazwa_projektu=_proj;
        grupa_projektu=_grupa_projektu;
        DEFAULT_GRUPA_PROJ=_grupa_projektu;
        DEFAULT_NUMER_PROJ=_proj;
}


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month=month+1;
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

        String a[]=data_poczatkowa.split(" ");
        String ss[]=a[1].split(":");

        hour=Integer.valueOf(ss[0]);
        minute=Integer.valueOf(ss[1]);
        Log.d("a","f");

     TimePickerDialog timePickerDialog = new TimePickerDialog(Rezerwacja.this,Rezerwacja.this,hour,minute,DateFormat.is24HourFormat(this));
    if(end_date){

        try {
            String s[]=godzina_poczatkowa.split(":");

            c.set(Calendar.HOUR_OF_DAY,Integer.valueOf(s[0]));
            c.set(Calendar.MINUTE,Integer.valueOf(s[1]));
            c.add(Calendar.MINUTE, DEFAULT_ADDTIME_MIN);
            hour=c.get(Calendar.HOUR_OF_DAY);
            minute=c.get(Calendar.MINUTE);
            timePickerDialog.updateTime(hour,minute);
        }catch (Exception ne){
            Logs_DataHandler log = new Logs_DataHandler(this);
            log.inputLog( "Rezerwacja.class 005: "+ne.toString());
            log.close();
        }

    }
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
        String godzina=(hourFinal)+":"+(minuteFinal);
       if(start_date) {
           data_poczatkowa=yearFinal+"-"+monthFinal+"-"+dayFinal+" "+(hourFinal)+":"+(minuteFinal)+":00";
           data_bez_godzin=yearFinal+"-"+monthFinal+"-"+dayFinal;
           godzina_poczatkowa=(hourFinal)+":"+(minuteFinal);
           poczatek_et.setText(data_poczatkowa);

       }else{

           data_koncowa=yearFinal+"-"+monthFinal+"-"+dayFinal+" "+(hourFinal)+":"+(minuteFinal)+":00";
           koniec_et.setText(data_koncowa);
       }


    }



}


