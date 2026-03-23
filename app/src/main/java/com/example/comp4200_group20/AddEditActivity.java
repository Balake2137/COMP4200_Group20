package com.example.comp4200_group20;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class AddEditActivity extends AppCompatActivity {
    TextView header;
    Button addEditButton;
    DBHelper dbHelper;
    Cursor cursor;
    EditText title;
    EditText desc;
    EditText ingr;
    EditText inst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_recipe);
        Bundle extras = getIntent().getExtras();
        header = findViewById(R.id.addEditHeader);
        addEditButton = findViewById(R.id.addEditButton);
        title = findViewById(R.id.recipeTitle);
        desc = findViewById(R.id.recipeDecription);
        ingr = findViewById(R.id.recipeIngredients);
        inst = findViewById(R.id.recipeInstructions);

        dbHelper = new DBHelper(this, "test_database", null, 1);
        dbHelper.getReadableDatabase();

        if (extras == null) {
            header.setText("Add Recipe");
            addEditButton.setText("ADD");
            addEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!title.getText().toString().isBlank() && !desc.getText().toString().isBlank() && !ingr.getText().toString().isBlank() && !inst.getText().toString().isBlank()) {
                        dbHelper.addData(title.getText().toString(), desc.getText().toString(), ingr.getText().toString(), inst.getText().toString());
                        Intent i = new Intent(AddEditActivity.this, MainActivity.class);
                        AddEditActivity.this.startActivity(i);
                    } else {
                        Snackbar.make(v, "Cannot have empty items!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            header.setText("Edit Recipe");
            addEditButton.setText("EDIT");
            cursor = dbHelper.getDataFromTitle(extras.getString("title"));
            cursor.moveToFirst();
            title.setText(cursor.getString(1));
            desc.setText(cursor.getString(2));
            ingr.setText(cursor.getString(3));
            inst.setText(cursor.getString(4));
            addEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!title.getText().toString().isBlank() && !desc.getText().toString().isBlank() && !ingr.getText().toString().isBlank() && !inst.getText().toString().isBlank()) {
                        dbHelper.editData(extras.getString("title"), title.getText().toString(), desc.getText().toString(), ingr.getText().toString(), inst.getText().toString());
                        Intent i = new Intent(AddEditActivity.this, MainActivity.class);
                        AddEditActivity.this.startActivity(i);
                    } else {
                        Snackbar.make(v, "Cannot have empty items!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
