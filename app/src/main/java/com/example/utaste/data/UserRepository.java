package com.example.utaste.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.utaste.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Repository backed by SQLite. Call init(context) once before use.
 */
public class UserRepository {
    private static UserRepository instance;
    private final UserDbHelper dbHelper;
    private final Context context;

    private UserRepository(Context ctx) {
        this.context = ctx.getApplicationContext();
        this.dbHelper = new UserDbHelper(context);
        ensureDefaultAccounts();
    }

    public static synchronized void init(Context context) {
        if (instance == null) instance = new UserRepository(context);
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null)
            throw new IllegalStateException("UserRepository not initialized. Call UserRepository.init(context) first.");
        return instance;
    }

    // Ensure admin and chef exist at first run
    private void ensureDefaultAccounts() {
        // run on background thread to avoid disk op on UI thread
        Executors.newSingleThreadExecutor().execute(() -> {
            if (findByEmailInternal("admin@local") == null) {
                User admin = new User("admin@local", "admin-pwd", User.Role.ADMIN);
                admin.setFirstName("System");
                admin.setLastName("Admin");
                insertUserInternal(admin);
            }
            if (findByEmailInternal("chef@local") == null) {
                User chef = new User("chef@local", "chef-pwd", User.Role.CHEF);
                chef.setFirstName("Head");
                chef.setLastName("Chef");
                insertUserInternal(chef);
            }
        });
    }

    // ---------------------------
    // Public API (synchronous helpers; you should call from background thread)
    // ---------------------------

    public boolean addUser(User user) {
        if (user == null || user.getEmail() == null) return false;
        synchronized (this) {
            if (findByEmailInternal(user.getEmail()) != null) return false;
            return insertUserInternal(user);
        }
    }

    public User findByEmail(String email) {
        if (email == null) return null;
        synchronized (this) {
            return findByEmailInternal(email);
        }
    }

    public boolean authenticate(String email, String password) {
        User u = findByEmail(email);
        if (u == null) return false;
        return u.getPassword().equals(password);
    }

    public boolean updateUser(String originalEmail, User updated) {
        if (originalEmail == null || updated == null || updated.getEmail() == null) return false;
        synchronized (this) {
            User existing = findByEmailInternal(originalEmail);
            if (existing == null) return false;
            // if email changed and new exists -> fail
            if (!originalEmail.equals(updated.getEmail()) && findByEmailInternal(updated.getEmail()) != null) {
                return false;
            }
            // preserve createdAt if zero
            if (updated.getCreatedAt() == 0L) updated.setCreatedAt(existing.getCreatedAt());
            updated.setModifiedAt(System.currentTimeMillis());

            // perform update (may include primary key change)
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                // if email changed, delete old row, then insert new
                if (!originalEmail.equals(updated.getEmail())) {
                    db.delete(UserDbHelper.TABLE_USERS, UserDbHelper.COL_EMAIL + "=?", new String[]{originalEmail});
                    insertUserInternal(updated, db);
                } else {
                    ContentValues cv = userToContentValues(updated);
                    db.update(UserDbHelper.TABLE_USERS, cv, UserDbHelper.COL_EMAIL + "=?", new String[]{originalEmail});
                }
                db.setTransactionSuccessful();
                return true;
            } finally {
                db.endTransaction();
            }
        }
    }

    public boolean deleteUser(String email) {
        if (email == null) return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int removed = db.delete(UserDbHelper.TABLE_USERS, UserDbHelper.COL_EMAIL + "=?", new String[]{email});
        return removed > 0;
    }

    public List<User> listWaiters() {
        List<User> res = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(UserDbHelper.TABLE_USERS,
                null,
                UserDbHelper.COL_ROLE + "=?",
                new String[]{User.Role.WAITER.name()},
                null, null, null);
        try {
            while (c.moveToNext()) {
                res.add(cursorToUser(c));
            }
        } finally {
            c.close();
        }
        return res;
    }

    public List<User> listAllUsers() {
        List<User> res = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(UserDbHelper.TABLE_USERS, null, null, null, null, null, null);
        try {
            while (c.moveToNext()) res.add(cursorToUser(c));
        } finally {
            c.close();
        }
        return res;
    }

    // ---------------------------
    // Internal helpers (expect to be called with synchronization when needed)
    // ---------------------------

    private boolean insertUserInternal(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return insertUserInternal(user, db);
    }

    private boolean insertUserInternal(User user, SQLiteDatabase db) {
        if (user.getCreatedAt() == 0L) user.setCreatedAt(System.currentTimeMillis());
        user.setModifiedAt(System.currentTimeMillis());
        ContentValues cv = userToContentValues(user);
        long id = db.insert(UserDbHelper.TABLE_USERS, null, cv);
        return id != -1;
    }

    private ContentValues userToContentValues(User u) {
        ContentValues cv = new ContentValues();
        cv.put(UserDbHelper.COL_EMAIL, u.getEmail());
        cv.put(UserDbHelper.COL_PASSWORD, u.getPassword());
        cv.put(UserDbHelper.COL_FIRST, u.getFirstName());
        cv.put(UserDbHelper.COL_LAST, u.getLastName());
        cv.put(UserDbHelper.COL_ROLE, u.getRole().name());
        cv.put(UserDbHelper.COL_CREATED, u.getCreatedAt());
        cv.put(UserDbHelper.COL_MODIFIED, u.getModifiedAt());
        return cv;
    }

    private User cursorToUser(Cursor c) {
        String email = c.getString(c.getColumnIndexOrThrow(UserDbHelper.COL_EMAIL));
        String password = c.getString(c.getColumnIndexOrThrow(UserDbHelper.COL_PASSWORD));
        String first = c.getString(c.getColumnIndexOrThrow(UserDbHelper.COL_FIRST));
        String last = c.getString(c.getColumnIndexOrThrow(UserDbHelper.COL_LAST));
        String roleStr = c.getString(c.getColumnIndexOrThrow(UserDbHelper.COL_ROLE));
        long created = c.getLong(c.getColumnIndexOrThrow(UserDbHelper.COL_CREATED));
        long modified = c.getLong(c.getColumnIndexOrThrow(UserDbHelper.COL_MODIFIED));

        User.Role role;
        try {
            role = User.Role.valueOf(roleStr);
        } catch (Exception e) {
            role = User.Role.WAITER; // fallback
        }
        User u = new User(email, password, role);
        u.setFirstName(first);
        u.setLastName(last);
        u.setCreatedAt(created);
        u.setModifiedAt(modified);
        return u;
    }

    private User findByEmailInternal(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(UserDbHelper.TABLE_USERS, null,
                UserDbHelper.COL_EMAIL + "=?",
                new String[]{email},
                null, null, null);
        try {
            if (c.moveToFirst()) {
                return cursorToUser(c);
            } else {
                return null;
            }
        } finally {
            c.close();
        }
    }

    // réinitialisé la base de donnée
    public void resetDatabase() {
        Executors.newSingleThreadExecutor().execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                // supprime seulement waiters et recipes mais pas les ingredients
                db.execSQL("DELETE FROM users WHERE role = 'WAITER'");
                db.execSQL("DELETE FROM recipe_ingredient");
                db.execSQL("DELETE FROM recipe");

                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        });
    }

    public boolean resetUserPassword(String email) {
        return dbHelper.resetUserPassword(email);
    }


}