package com.example.carsharing.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carsharing.R;
import com.example.carsharing.Other.SimpleTableDataAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class ListaRezerwacji extends AppCompatActivity {
    ArrayList<HashMap<String, String>> lista_rezerwacji;

public static String[] headers={"Temat","Data","Samochód"};
    public static String[][] dane;

    AlertDialog alertDialog;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lista_rezerwacji);
            lista_rezerwacji = new ArrayList<>();

        TextView legenta_tv=(TextView) findViewById(R.id.legenda_tv);
        legenta_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ListaRezerwacji.this);
                LayoutInflater inflater = (LayoutInflater)   ListaRezerwacji.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.legenda,null);
                dialogBuilder.setView(view);

                alertDialog =dialogBuilder.create();
                alertDialog.show();

            }
        });


        Intent intent = getIntent();

     lista_rezerwacji= ( ArrayList<HashMap<String, String>>) intent.getSerializableExtra("lista_rezerwacji");

     dane=new String[lista_rezerwacji.size()][3];
     for(int i=0;i<lista_rezerwacji.size();i++){
         dane[i][0]=lista_rezerwacji.get(i).get("Subject");
         dane[i][1]=lista_rezerwacji.get(i).get("StartDate")+"  "+lista_rezerwacji.get(i).get("EndDate");
         dane[i][2]=lista_rezerwacji.get(i).get("Eit_ResourceName");
     }

        ArrayList<Integer> kolory=new ArrayList<>();

        for(int i=0;i<lista_rezerwacji.size();i++){
        String status=lista_rezerwacji.get(i).get("Status");
        if(status.equals("0")){
            kolory.add(Color.GRAY);
        }else if(status.equals("1")){
            kolory.add(Color.GREEN);
        }else if(status.equals("2")){
            kolory.add(Color.YELLOW);
        }else{
            kolory.add(Color.RED);
        }
        }

        kolory.add(Color.MAGENTA);
        kolory.add(Color.YELLOW);
        kolory.add(Color.GREEN);
        kolory.add(Color.WHITE);

        TableView<String[]> tableView2 = (TableView<String[]>) findViewById(R.id.lista_rezerwacji_tabela);
        
        tableView2.setHeaderAdapter(new SimpleTableHeaderAdapter(this,headers));
        SimpleTableDataAdapter adapter=new SimpleTableDataAdapter(this,dane,kolory);
        tableView2.setDataAdapter(adapter);
        TableColumnWeightModel columnModel = new TableColumnWeightModel(3);
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 4);
        columnModel.setColumnWeight(2, 3);



        tableView2.setColumnModel(columnModel);

        tableView2.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MojeRezerwacje.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("BookingId",lista_rezerwacji.get(rowIndex).get("BookingId"));
                intent.putExtra("StartDate",lista_rezerwacji.get(rowIndex).get("StartDate"));
                intent.putExtra("EndDate",lista_rezerwacji.get(rowIndex).get("EndDate"));
                intent.putExtra("Status",lista_rezerwacji.get(rowIndex).get("Status"));
                intent.putExtra("GrupaProjektu",lista_rezerwacji.get(rowIndex).get("GrupaProjektu"));
                intent.putExtra("NrProjektu",lista_rezerwacji.get(rowIndex).get("NrProjektu"));

                startActivity(intent);
            }
        });

        ImageView back_bt = (ImageView) findViewById(R.id.back_bt);

        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaRezerwacji.this, Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ListaRezerwacji.this.startActivity(intent);
            }
        });



    }
}
