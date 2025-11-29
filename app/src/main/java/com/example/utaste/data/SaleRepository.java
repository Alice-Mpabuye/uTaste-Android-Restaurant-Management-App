package com.example.utaste.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SaleRepository {
    private static SaleRepository instance;
    private final UserDbHelper dbHelper;
    private final List<Sale> sales = new ArrayList<>();

    private SaleRepository(Context context) {
        this.dbHelper = new UserDbHelper(context.getApplicationContext());
    }

    public static synchronized void init(Context context) {
        if (instance == null) instance = new SaleRepository(context);
    }

    public static synchronized SaleRepository getInstance() {
        if (instance == null) throw new IllegalStateException("SaleRepository not initialized. Call init(context)");
        return instance;
    }

    public long recordSale(int recipeId, int rating, String note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("recipe_id", recipeId);
        cv.put("rating", rating);
        cv.put("note", note);
        cv.put("created_at", System.currentTimeMillis());
        return db.insert("sales", null, cv);
    }

    public List<Sale> listAllSales() {
        List<Sale> out = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String q = "SELECT s.id, s.recipe_id, r.name, s.rating, s.note, s.created_at FROM sales s LEFT JOIN recipe r ON s.recipe_id = r.id ORDER BY s.created_at DESC";
        Cursor c = db.rawQuery(q, null);
        while (c.moveToNext()) {
            out.add(new Sale(
                    c.getLong(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getInt(3),
                    c.getString(4),
                    c.getLong(5)
            ));
        }
        c.close();
        return out;
    }

    /**
     * Returns a LinkedHashMap (preserve recipe ordering) keyed by recipeId -> list of Sales for that recipe.
     */
    public Map<Integer, List<Sale>> listSalesGroupedByRecipe() {
        Map<Integer, List<Sale>> map = new LinkedHashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String q = "SELECT s.id, s.recipe_id, r.name, s.rating, s.note, s.created_at FROM sales s LEFT JOIN recipe r ON s.recipe_id = r.id ORDER BY r.name ASC, s.created_at DESC";
        Cursor c = db.rawQuery(q, null);
        while (c.moveToNext()) {
            Sale s = new Sale(c.getLong(0), c.getInt(1), c.getString(2), c.getInt(3), c.getString(4), c.getLong(5));
            if (!map.containsKey(s.getRecipeId())) map.put(s.getRecipeId(), new ArrayList<>());
            map.get(s.getRecipeId()).add(s);
        }
        c.close();
        return map;
    }

    public int getCountForRecipe(int recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM sales WHERE recipe_id = ?", new String[]{String.valueOf(recipeId)});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public double getAverageRatingForRecipe(int recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT AVG(rating) FROM sales WHERE recipe_id = ?", new String[]{String.valueOf(recipeId)});
        double avg = 0.0;
        if (c.moveToFirst()) avg = c.getDouble(0);
        c.close();
        return avg;
    }

    public void clearAllSales() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("sales", null, null);
    }
}