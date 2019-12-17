package com.example.carsharing;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;


public class ListaRezerwacji extends AppCompatActivity {
    ArrayList<HashMap<String, String>> lista_rezerwacji;

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
         dane[i][1]=lista_rezerwacji.get(i).get("StartDate")+"  "+lista_rezerwacji.get(i).get("EndDate");
         dane[i][2]=lista_rezerwacji.get(i).get("Eit_ResourceName");
     }

        TableView<String[]> tableView2 = (TableView<String[]>) findViewById(R.id.lista_rezerwacji_tabela);
        tableView2.setHeaderAdapter(new SimpleTableHeaderAdapter(this,headers));
        SimpleTableDataAdapter adapter=new SimpleTableDataAdapter(this,dane);
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
