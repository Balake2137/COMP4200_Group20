package com.example.comp4200_group20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE recipe(_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, ingredients TEXT, instructions TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long addData(String titleInput, String descriptionInput, String ingredientsInput, String instructionsInput){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", titleInput);
        contentValues.put("description", descriptionInput);
        contentValues.put("ingredients", ingredientsInput);
        contentValues.put("instructions", instructionsInput);
        return sqLiteDatabase.insert("recipe", null,contentValues);
    }

    public Cursor getTitleDesc(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT title, description FROM recipe", null);
    }

    public Cursor getDataFromTitle(String title){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM recipe WHERE title=?", new String[]{title});
    }

    public boolean deleteData(String inputTitle) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM recipe WHERE title=?", new String[]{inputTitle});
        if (cursor.getCount() > 0) {
            sqLiteDatabase.delete("recipe", "title = ?", new String[]{inputTitle});
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public long editData(String oldTitle, String titleInput, String descriptionInput, String ingredientsInput, String instructionsInput){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", titleInput);
        contentValues.put("description", descriptionInput);
        contentValues.put("ingredients", ingredientsInput);
        contentValues.put("instructions", instructionsInput);

        return sqLiteDatabase.update("recipe", contentValues, "title=?", new String[]{oldTitle} );
    }

}
