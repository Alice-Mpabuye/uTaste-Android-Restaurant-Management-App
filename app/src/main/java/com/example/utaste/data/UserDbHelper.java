package com.example.utaste.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "utaste.db";
    private static final int DATABASE_VERSION = 1;

    // ... (User table definitions are unchanged)
    public static final String TABLE_USERS = "users";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FIRST = "firstName";
    public static final String COL_LAST = "lastName";
    public static final String COL_ROLE = "role";
    public static final String COL_CREATED = "createdAt";
    public static final String COL_MODIFIED = "modifiedAt";
    private static final String SQL_CREATE_USERS = "CREATE TABLE " + TABLE_USERS + " (" + COL_EMAIL + " TEXT PRIMARY KEY, " + COL_PASSWORD + " TEXT NOT NULL, " + COL_FIRST + " TEXT, " + COL_LAST + " TEXT, " + COL_ROLE + " TEXT NOT NULL, " + COL_CREATED + " INTEGER NOT NULL, " + COL_MODIFIED + " INTEGER NOT NULL" + ");";


    private static final String CREATE_TABLE_INGREDIENT=
            "CREATE TABLE Ingredient (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "qrCode TEXT UNIQUE NOT NULL" +
            ");";

    private static final String CREATE_TABLE_RECIPE =
            "CREATE TABLE recipe (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL UNIQUE, " +
                    "description TEXT, " +
                    "image TEXT)";

    private static final String CREATE_TABLE_RECIPE_INGREDIENT =
            "CREATE TABLE recipe_ingredient (" +
                    "recipe_id INTEGER, " +
                    "ingredient_id INTEGER, " +
                    "quantity REAL, " +
                    "PRIMARY KEY(recipe_id, ingredient_id))";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(CREATE_TABLE_INGREDIENT);
        db.execSQL(CREATE_TABLE_RECIPE);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENT);

        // Prefill ingredients
        db.execSQL("INSERT INTO Ingredient (name, qrCode) VALUES ('Farine', 'FARINE001')");
        db.execSQL("INSERT INTO Ingredient (name, qrCode) VALUES ('Sucre', 'SUCRE002')");
        db.execSQL("INSERT INTO Ingredient (name, qrCode) VALUES ('Beurre', 'BEURRE003')");
        db.execSQL("INSERT INTO Ingredient (name, qrCode) VALUES ('Oeufs', 'OEUFS004')");
        db.execSQL("INSERT INTO Ingredient (name, qrCode) VALUES ('Vanille', 'VANILLE005')");
        db.execSQL("INSERT INTO Ingredient (name, qrCode) VALUES ('Levure', 'LEVURE006')");
        db.execSQL("INSERT INTO Ingredient (name, qrCode) VALUES ('SEL', 'SEL007')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredient");
        db.execSQL("DROP TABLE IF EXISTS recipe");
        db.execSQL("DROP TABLE IF EXISTS Ingredient");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // --- Only ingredient-specific methods remain ---

    public Ingredient getIngredientByQRCode(String qrCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name FROM Ingredient WHERE qrCode = ?", new String[]{qrCode});

        if (cursor.moveToFirst()) {
            Ingredient ing = new Ingredient(
                    cursor.getInt(0),
                    cursor.getString(1),
                    qrCode
            );
            cursor.close();
            return ing;
        }
        cursor.close();
        return null;
    }
}
