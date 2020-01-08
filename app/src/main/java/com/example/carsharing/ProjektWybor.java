package com.example.carsharing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProjektWybor {
    List<People> mList;

    public static ArrayList<String> grupa_proj=new ArrayList<>();
    public static ArrayList<String> numer_proj=new ArrayList<>();
    View view;
    AlertDialog alertDialog;

public void WyborProjektu(Context con1){
    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con1)
            .setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();

                }
            })
            .setNegativeButton("Wybierz", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

    //pobranie z bazy danych//***********
    try {
        Projekty_DataHandler myDB = new Projekty_DataHandler(con1);
        Cursor getdata = myDB.getGrup();
        while (getdata.moveToNext()) {
            grupa_proj.add(getdata.getString(0));
        }
        myDB.close();
    } catch (Exception e) {
        Logs_DataHandler log = new Logs_DataHandler(con1);
        log.inputLog("Login.class 006: " + e.toString());
        log.close();
    }
        //*********************************

    LayoutInflater inflater = (LayoutInflater)   con1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.projekty,null);

    Spinner projekt_sp=(Spinner) view.findViewById(R.id.spinner2);

    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(con1,
            android.R.layout.simple_spinner_item,grupa_proj);
    adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    projekt_sp.setAdapter(adapter4);
    projekt_sp.setSelection(grupa_proj.indexOf("PO"));

    //pobranie z bazy danych//***********
    try {
        Projekty_DataHandler myDB = new Projekty_DataHandler(con1);
        Cursor getdata = myDB.getNumber("PO");
        while (getdata.moveToNext()) {
            numer_proj.add(getdata.getString(0));
        }
        myDB.close();
    } catch (Exception e) {
        Logs_DataHandler log = new Logs_DataHandler(con1);
        log.inputLog("Login.class 006: " + e.toString());
        log.close();
    }


    final AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);

    PeopleAdapter adapter;
    mList = retrievePeople();

    adapter = new PeopleAdapter(con1, R.layout.activity_main, R.id.lbl_name,mList);

    actv.setThreshold(2);
    actv.setAdapter(adapter);
    actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            //this is the way to find selected object/item
         //   selectedPerson = (People) adapterView.getItemAtPosition(pos);
              //nazwa_projektu=(String) adapterView.getItemAtPosition(pos);
            //nazwa_projektu=selectedPerson.getName();
        }
    });


    ImageView dropdown=(ImageView) view.findViewById(R.id.dropdown);
    dropdown.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            actv.showDropDown();
        }
    });














    dialogBuilder.setView(view);

    alertDialog =dialogBuilder.create();
    alertDialog.show();


}


    private List<People> retrievePeople() {
        List<People> list = new ArrayList<People>();
        for(int i =0;i<numer_proj.size();i++) {
            list.add(new People(numer_proj.get(i)));
        }
        return list;
    }


}