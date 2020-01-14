package com.example.carsharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DostepnoscListAdapter extends ArrayAdapter<Obiekt_Dostepnosc> {

    private static final String TAG ="DostepnoscListAdapter";
    private Context mContext;
    int mResource;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       String samochod=getItem(position).getSamochod();
       String start_date=getItem(position).getStart_date();
       String end_date=getItem(position).getEnd_date();

    //   Obiekt_Dostepnosc obiekt_dostepnosc =new Obiekt_Dostepnosc(samochod,start_date,end_date);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource,parent,false);
        TextView auto_tv=(TextView) convertView.findViewById(R.id.textView1);
        TextView start_date_tv=(TextView) convertView.findViewById(R.id.textView2);
        TextView end_date_tv=(TextView) convertView.findViewById(R.id.textView3);

        auto_tv.setText(samochod);
        start_date_tv.setText(start_date);
        end_date_tv.setText(end_date);

return convertView;
       // return super.getView(position, convertView, parent);
    }

    public DostepnoscListAdapter(Context context, int resource, ArrayList<Obiekt_Dostepnosc> objects, Context Context) {
        super(context, resource, objects);
        mContext = Context;
        mResource=resource;
    }
}

