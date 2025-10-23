package com.example.utaste.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "utaste.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FIRST = "firstName";
    public static final String COL_LAST = "lastName";
    public static final String COL_ROLE = "role";
    public static final String COL_CREATED = "createdAt";
    public static final String COL_MODIFIED = "modifiedAt";

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COL_EMAIL + " TEXT PRIMARY KEY, " +
                    COL_PASSWORD + " TEXT NOT NULL, " +
                    COL_FIRST + " TEXT, " +
                    COL_LAST + " TEXT, " +
                    COL_ROLE + " TEXT NOT NULL, " +
                    COL_CREATED + " INTEGER NOT NULL, " +
                    COL_MODIFIED + " INTEGER NOT NULL" +
                    ");";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
        // no prefill here — handled in repository
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Simple strategy for now (no data migration needed for deliverable 2 initial)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
