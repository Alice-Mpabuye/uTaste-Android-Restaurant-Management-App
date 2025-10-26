package com.example.utaste.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.utaste.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private final UserDbHelper dbHelper;

    private RecipeRepository(Context context) {
        this.dbHelper = new UserDbHelper(context.getApplicationContext());
    }

    public static synchronized void init(Context context) {
        if (instance == null) instance = new RecipeRepository(context);
    }

    public static synchronized RecipeRepository getInstance() {
        if (instance == null)
            throw new IllegalStateException("RecipeRepository not initialized. Call init(context) first.");
        return instance;
    }

    // CREATE
    public long createRecipe(String name, String description, String imageName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("description", description);
        cv.put("image", imageName);
        return db.insert("recipe", null, cv);
    }

    // READ ALL
    public List<Recipe> listRecipes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("recipe", null, null, null, null, null, "name ASC");
        List<Recipe> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(new Recipe(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("description")),
                    c.getString(c.getColumnIndexOrThrow("image"))
            ));
        }
        c.close();
        return list;
    }

    // DELETE
    public boolean deleteRecipe(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("recipe", "id=?", new String[]{String.valueOf(id)}) > 0;
    }

}
