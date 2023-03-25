package com.example.medicinedatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplayData extends AppCompatActivity {

    private ArrayList<String> data;
    // private ArrayAdapter<String> adapter;
    private CustomAdapter adapter;
    private ListView listView;
    private DataSource dataSource;
//    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_data);
        listView = findViewById(R.id.listView);
//        tableLayout = findViewById(R.id.tableLayout);
        dataSource = new DataSource(this);

        data = new ArrayList<>();
        Cursor cursor = dataSource.readData();

//        data.add(String.format("%10s", "NAME") + String.format("%25s", "DATE") + String.format("%25s", "TIME"));

        if(cursor.getCount() == 0) {
            Toast.makeText(getBaseContext(), "No data found!", Toast.LENGTH_LONG).show();
        } else{
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                String date = cursor.getString(1);
                String time = cursor.getString(2);
                data.add(name + " " + date + " " + time);
            }
//            adapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, data);
            adapter = new CustomAdapter(this, R.layout.custom_adapter, data);
            listView.setAdapter(adapter);
        }
    }
}
