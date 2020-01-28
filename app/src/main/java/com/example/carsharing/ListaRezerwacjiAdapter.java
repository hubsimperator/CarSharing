package com.example.carsharing;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carsharing.Other.Obiekt_Rezerwacja;

import java.util.ArrayList;

public class ListaRezerwacjiAdapter extends ArrayAdapter<Obiekt_Rezerwacja> {

    private static final String TAG ="DostepnoscListAdapter";
    private Context mContext;
    int mResource;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String samochod=getItem(position).getEit_ResourceName();
        String subject=getItem(position).getSubject();
       String start_date=getItem(position).getStartDate();
       String end_date=getItem(position).getEndDate();
       String kolor_id=getItem(position).getStatus();

       Integer kolor=Integer.valueOf(kolor_id);
       switch (kolor){
           case 0:
               kolor=Color.GRAY;
               break;
           case 1:
               kolor=Color.GREEN;
               break;
           case 2:
               kolor=Color.YELLOW;
               break;
       }

    //   Obiekt_Dostepnosc obiekt_dostepnosc =new Obiekt_Dostepnosc(samochod,start_date,end_date);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource,parent,false);

       TextView auto_tv=(TextView) convertView.findViewById(R.id.carname_tv);
        TextView start_date_tv=(TextView) convertView.findViewById(R.id.startdate_tv);
        TextView end_date_tv=(TextView) convertView.findViewById(R.id.enddate_tv);
        TextView subject_tv=(TextView) convertView.findViewById(R.id.subject_tv);

        LinearLayout llayout =(LinearLayout) convertView.findViewById(R.id.rezerwacja_layout);

        llayout.setBackgroundColor(kolor);

        auto_tv.setText(samochod);
        subject_tv.setText(subject);
        start_date_tv.setText(start_date);
        end_date_tv.setText(end_date);

return convertView;
       // return super.getView(position, convertView, parent);
    }

    public ListaRezerwacjiAdapter(Context context, int resource, ArrayList<Obiekt_Rezerwacja> objects, Context Context) {
        super(context, resource, objects);
        mContext = Context;
        mResource=resource;
    }
}

