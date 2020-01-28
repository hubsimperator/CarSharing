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

public class ListaSamochodowAdapter extends ArrayAdapter<Obiekt_Samochód> {

    private Context mContext;
    int mResource;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       String samochod=getItem(position).getResourceName();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource,parent,false);

        TextView auto_tv=(TextView) convertView.findViewById(R.id.textView1);
        auto_tv.setText(samochod);

return convertView;
    }

    public ListaSamochodowAdapter(Context context, int resource, ArrayList<Obiekt_Samochód> objects, Context Context) {
        super(context, resource, objects);
        mContext = Context;
        mResource=resource;
    }
}

