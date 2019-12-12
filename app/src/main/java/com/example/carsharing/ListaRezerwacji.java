package com.example.carsharing;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class ListaRezerwacji extends AppCompatActivity {
    ArrayList<HashMap<String, String>> lista_rezerwacji;

    /*
    ListaRezerwacji (ArrayList<HashMap<String, String>> _lista_rezerwacji) {
        lista_rezerwacji = new ArrayList<>();
        lista_rezerwacji = _lista_rezerwacji;
    }


     */

public static String[] headers={"Temat","Data","Samochód"};
    public static String[][] dane;


    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lista_rezerwacji);
            lista_rezerwacji = new ArrayList<>();

        Intent intent = getIntent();

     lista_rezerwacji= ( ArrayList<HashMap<String, String>>) intent.getSerializableExtra("lista_rezerwacji");

     dane=new String[lista_rezerwacji.size()][3];
     for(int i=0;i<lista_rezerwacji.size();i++){
         dane[i][0]=lista_rezerwacji.get(i).get("Subject");
         dane[i][1]=lista_rezerwacji.get(i).get("StartDate");
         dane[i][2]=lista_rezerwacji.get(i).get("Eit_Resource");
     }

/*
        TableView tableView = (TableView) findViewById(R.id.lista_rezerwacji_tabela);
        tableView.setColumnCount(4);

        TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(ListaRezerwacji.this, 4, 200);
        columnModel.setColumnWidth(1, 300);
        columnModel.setColumnWidth(2, 250);
        tableView.setColumnModel(columnModel);


 */

/*
        try {
            MojeRezerwacjeDataHandler RezerwacjeDH = new MojeRezerwacjeDataHandler(ListaRezerwacji.this);
            Log.d("BAZA","UDALO SIE");

            RezerwacjeDH.inputData("Test 1","2019-12-06 12:00:00","Moja tojota");
            RezerwacjeDH.inputData("Test 2","2019-12-07 12:00:00","Daniela mazda");
            RezerwacjeDH.inputData("Test 3","2019-12-08 12:00:00","Jacka rower");





            Integer table_length=0;
            Cursor getdata = RezerwacjeDH.getData();
            Log.d("Dane z tabeli","Proba pobrania");
            while (getdata.moveToNext()) {
                Log.d("Dane z tabeli",getdata.getString(1)+"\n"+getdata.getString(2)+" "+getdata.getString(3));
                dane[table_length][0]=getdata.getString(1);
                dane[table_length][1]=getdata.getString(2)+" "+getdata.getString(2);
                dane[table_length][2]=getdata.getString(3);

            table_length++;
            }
            RezerwacjeDH.close();
            String s=dane[0][0];

        } catch (Exception e){
            Log.d("BAZA","NIE UDALO SIE");

            e.printStackTrace();
        }

 */


        TableView<String[]> tableView2 = (TableView<String[]>) findViewById(R.id.lista_rezerwacji_tabela);
        tableView2.setHeaderAdapter(new SimpleTableHeaderAdapter(this,headers));
        SimpleTableDataAdapter adapter=new SimpleTableDataAdapter(this,dane);
        adapter.setTextSize(14);
        tableView2.setDataAdapter(adapter);
        TableColumnWeightModel columnModel = new TableColumnWeightModel(3);
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 4);
        columnModel.setColumnWeight(2, 3);
        tableView2.setColumnModel(columnModel);

        tableView2.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                //Toast.makeText(MainActivity.this, ((String[])clickedData)[1], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MojeRezerwacje.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
      //  tableView2.setDataAdapter(new SimpleTableDataAdapter(this, SPACESHIPS));




        ImageView back_bt = (ImageView) findViewById(R.id.back_bt);

        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
