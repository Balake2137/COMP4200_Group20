package com.example.comp4200_group20;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<DataSet> dataSets = new ArrayList<>();
    RecyclerView recyclerView;
    DBHelper dbHelper;
    RecAdapter myAdapter;
    FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = findViewById(R.id.addButton);

        dbHelper = new DBHelper(this, "test_database", null, 1);
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddEditActivity.class);
                MainActivity.this.startActivity(i);
            }
        });

        myAdapter = new RecAdapter(dataSets, MainActivity.this);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        dataSets = new ArrayList<>();

        Cursor cursor =  dbHelper.getTitleDesc();
        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "No Recipes to Display", Toast.LENGTH_LONG).show();
        }else{
            while (cursor.moveToNext()){
                dataSets.add(new DataSet(cursor.getString(0), cursor.getString(1)));
            }
        }
        recyclerView.setAdapter(myAdapter);
    }
}
