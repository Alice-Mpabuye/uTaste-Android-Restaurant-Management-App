package com.example.utaste;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.utaste.data.Ingredient;
import com.example.utaste.data.UserDbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.database.Cursor;

@RunWith(AndroidJUnit4.class)
public class IngredientInstrumentedTest {

    private Context context;
    private UserDbHelper dbHelper;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dbHelper = new UserDbHelper(context);

        // Reset DB manually
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Ingredient");
        db.execSQL("INSERT INTO Ingredient (name, qrCode) VALUES ('Sugar', 'QRSUGAR')");
        db.execSQL("INSERT INTO Ingredient (name, qrCode) VALUES ('Salt', 'QRSALT')");
    }

    @Test
    public void testRetrieveIngredientByQRCode() {
        Ingredient sugar = dbHelper.getIngredientByQRCode("QRSUGAR");

        assertNotNull(sugar);
        assertEquals("Sugar", sugar.getName());
    }

    @Test
    public void testRetrieveIngredientByName() {
        Ingredient salt = dbHelper.getIngredientByName("Salt");

        assertNotNull(salt);
        assertEquals("QRSALT", salt.getQrCode());
    }

    @Test
    public void testUnknownQRCodeReturnsNull() {
        assertNull(dbHelper.getIngredientByQRCode("XXX"));
    }

    @Test
    public void testUnknownNameReturnsNull() {
        assertNull(dbHelper.getIngredientByName("Chocolate"));
    }

    @Test
    public void testInsertNewIngredientDirectSQL() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO Ingredient (name, qrCode) VALUES ('Butter', 'QRBUTTER')");

        Ingredient butter = dbHelper.getIngredientByName("Butter");
        assertNotNull(butter);
    }

    @Test
    public void testIngredientIdsAreAutoIncrement() {
        Ingredient sugar = dbHelper.getIngredientByName("Sugar");
        Ingredient salt = dbHelper.getIngredientByName("Salt");

        assertTrue(salt.getId() > sugar.getId());
    }

    @Test
    public void testAddIngredientToRecipe() {
        // create recipe
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO recipe (name) VALUES ('Cake')");
        int recipeId = 1;

        Ingredient sugar = dbHelper.getIngredientByName("Sugar");
        dbHelper.addIngredientToRecipe(recipeId, sugar.getId(), 50);

        // verify link exists
        Cursor cursor = db.rawQuery(
                "SELECT quantity FROM recipe_ingredient WHERE recipe_id=1 AND ingredient_id=?",
                new String[]{String.valueOf(sugar.getId())}
        );

        assertTrue(cursor.moveToFirst());
        assertEquals(50.0, cursor.getDouble(0), 0.01);
        cursor.close();

    }

    @Test
    public void testAddMultipleIngredientsToRecipe() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO recipe (name) VALUES ('Cookie')");
        int recipeId = 1;

        Ingredient sugar = dbHelper.getIngredientByName("Sugar");
        Ingredient salt = dbHelper.getIngredientByName("Salt");

        dbHelper.addIngredientToRecipe(recipeId, sugar.getId(), 10);
        dbHelper.addIngredientToRecipe(recipeId, salt.getId(), 2);

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM recipe_ingredient WHERE recipe_id=1",
                null
        );

        assertTrue(cursor.moveToFirst());
        int count = cursor.getInt(0);
        cursor.close();

        assertEquals(2, count);
    }

    @Test
    public void testRecipeIngredientRejectsDuplicatePairs() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO recipe (name) VALUES ('Bread')");
        int recipeId = 1;

        Ingredient sugar = dbHelper.getIngredientByName("Sugar");

        dbHelper.addIngredientToRecipe(recipeId, sugar.getId(), 30);

        long before = db.compileStatement("SELECT COUNT(*) FROM recipe_ingredient").simpleQueryForLong();

        // second insert with same (recipe_id, ingredient_id) silently ignored
        dbHelper.addIngredientToRecipe(recipeId, sugar.getId(), 40);

        long after = db.compileStatement("SELECT COUNT(*) FROM recipe_ingredient").simpleQueryForLong();

        assertEquals(before, after);
    }

    @Test
    public void testUpdateIngredientQuantityInRecipe() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO recipe (name) VALUES ('Pancake')");
        int recipeId = 1;

        Ingredient sugar = dbHelper.getIngredientByName("Sugar");
        dbHelper.addIngredientToRecipe(recipeId, sugar.getId(), 20);

        // Update quantity
        db.execSQL("UPDATE recipe_ingredient SET quantity=45 WHERE recipe_id=1 AND ingredient_id=?",
                new Object[]{sugar.getId()});

        Cursor c = db.rawQuery(
                "SELECT quantity FROM recipe_ingredient WHERE recipe_id=1 AND ingredient_id=?",
                new String[]{String.valueOf(sugar.getId())}
        );

        assertTrue(c.moveToFirst());
        assertEquals(45.0, c.getDouble(0), 0.01);
        c.close();
    }

    @Test
    public void testRemoveIngredientFromRecipe() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO recipe (name) VALUES ('Smoothie')");
        int recipeId = 1;

        Ingredient sugar = dbHelper.getIngredientByName("Sugar");
        dbHelper.addIngredientToRecipe(recipeId, sugar.getId(), 10);

        db.execSQL("DELETE FROM recipe_ingredient WHERE recipe_id=1 AND ingredient_id=?",
                new Object[]{sugar.getId()});

        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM recipe_ingredient WHERE recipe_id=1 AND ingredient_id=?",
                new String[]{String.valueOf(sugar.getId())}
        );

        assertTrue(c.moveToFirst());
        assertEquals(0, c.getInt(0));
        c.close();
    }

}
