package com.example.ecommerce.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.ecommerce.data.db.DatabaseHelper;
import com.example.ecommerce.data.local.LocalStorage;
import com.example.ecommerce.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class AuthRepository {
    private static final String KEY_ACTIVE_EMAIL = "active_email";

    private final LocalStorage localStorage;
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public AuthRepository(Context context) {
        this.localStorage = new LocalStorage(context.getApplicationContext());
        this.dbHelper = new DatabaseHelper(context.getApplicationContext());
        this.database = dbHelper.getWritableDatabase();
    }

    public boolean signUp(String name, String email, String password) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            return false;
        }

        // Check if user already exists
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_EMAIL},
                DatabaseHelper.COLUMN_USER_EMAIL + " = ?",
                new String[]{email}, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return false; // User with this email already exists
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, password);

        long newRowId = database.insert(DatabaseHelper.TABLE_USERS, null, values);
        if (newRowId != -1) {
            localStorage.saveString(KEY_ACTIVE_EMAIL, email);
            return true;
        }
        return false;
    }

    public boolean signIn(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            return false;
        }

        String[] columns = {DatabaseHelper.COLUMN_USER_ID};
        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?" + " AND " + DatabaseHelper.COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            localStorage.saveString(KEY_ACTIVE_EMAIL, email);
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

     public boolean resetPassword(String email, String newPassword) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(newPassword)) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, newPassword);

        int rowsAffected = database.update(DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COLUMN_USER_EMAIL + " = ?", new String[]{email});
        return rowsAffected > 0;
    }

    public void logout() {
        localStorage.remove(KEY_ACTIVE_EMAIL);
    }

    @Nullable
    public User getActiveUser() {
        String email = localStorage.getString(KEY_ACTIVE_EMAIL);
        if (TextUtils.isEmpty(email)) {
            return null;
        }

        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_NAME, DatabaseHelper.COLUMN_USER_EMAIL, DatabaseHelper.COLUMN_USER_PASSWORD},
                DatabaseHelper.COLUMN_USER_EMAIL + " = ?",
                new String[]{email}, null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD));
            cursor.close();
            return new User(name, userEmail, password);
        }
        cursor.close();
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_NAME, DatabaseHelper.COLUMN_USER_EMAIL, DatabaseHelper.COLUMN_USER_PASSWORD},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD));
                users.add(new User(name, email, password));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }
}
