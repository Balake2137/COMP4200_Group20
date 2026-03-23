package com.example.comp4200_group20;

import android.os.Bundle;

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

        recyclerView = findViewById(R.id.recipe_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        dataSets.add(new DataSet("Title 1", "Desc"));
        dataSets.add(new DataSet("Title 2", "Desc"));
        dataSets.add(new DataSet("Title 3", "Desc"));

        RecAdapter myAdapter = new RecAdapter(dataSets);
        recyclerView.setAdapter(myAdapter);
    }
}
