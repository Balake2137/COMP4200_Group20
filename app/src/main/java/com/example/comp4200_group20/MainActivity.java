package com.example.comp4200_group20;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<DataSet> dataSets = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper dbHelper = new DBHelper(this, "test_database", null, 1);
        dbHelper.getReadableDatabase();

        recyclerView = findViewById(R.id.recipe_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Cursor cursor =  dbHelper.getTitleDesc();
        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "No Recipes to Display", Toast.LENGTH_LONG).show();
        }else{
            while (cursor.moveToNext()){
                dataSets.add(new DataSet(cursor.getString(0), cursor.getString(1)));
            }
        }

        dataSets.add(new DataSet("Test", "Desc"));

        RecAdapter myAdapter = new RecAdapter(dataSets);
        recyclerView.setAdapter(myAdapter);
    }
}
