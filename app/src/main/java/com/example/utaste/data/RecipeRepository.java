package com.example.utaste.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        return db.insertWithOnConflict("recipe", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    // READ ALL
    public List<Recipe> listRecipes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("recipe", null, null, null, null, null, "name ASC");
        List<Recipe> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(new Recipe(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3)
            ));
        }
        c.close();
        return list;
    }
    
    // READ ONE
    public Recipe getRecipeById(int recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM recipe WHERE id = ?", new String[]{String.valueOf(recipeId)});
        if (cursor.moveToFirst()) {
            Recipe recipe = new Recipe(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
            return recipe;
        }
        return null;
    }

    // UPDATE
    public int updateRecipe(int id, String name, String description, String imageName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("description", description);
        cv.put("image", imageName);
        return db.update("recipe", cv, "id = ?", new String[]{String.valueOf(id)});
    }

    // DELETE
    public void deleteRecipe(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("recipe", "id = ?", new String[]{String.valueOf(id)});
        db.delete("recipe_ingredient", "recipe_id = ?", new String[]{String.valueOf(id)});
    }

    // --- Ingredient-related methods ---

    public void addIngredientToRecipe(int recipeId, int ingredientId, double quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("ingredient_id", ingredientId);
        values.put("quantity", quantity);
        db.insert("recipe_ingredient", null, values);
    }

    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT i.id, i.name, ri.quantity FROM Ingredient i INNER JOIN recipe_ingredient ri ON i.id = ri.ingredient_id WHERE ri.recipe_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(recipeId)});
        if (cursor.moveToFirst()) {
            do {
                ingredients.add(new RecipeIngredient(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ingredients;
    }

    public long insertIngredient(Ingredient ing) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", ing.getName());
        cv.put("qrCode", ing.getQrCode());
        return db.insertWithOnConflict("Ingredient", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }


    public void clearIngredientsForRecipe(int recipeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("recipe_ingredient", "recipe_id = ?", new String[]{String.valueOf(recipeId)});
    }

    public void updateIngredientQuantity(int recipeId, int ingredientId, double quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        db.update("recipe_ingredient", values, "recipe_id = ? AND ingredient_id = ?",
                new String[]{String.valueOf(recipeId), String.valueOf(ingredientId)});
    }

}
