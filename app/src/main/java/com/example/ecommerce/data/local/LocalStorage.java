package com.example.ecommerce.data.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Thin wrapper around SharedPreferences so repositories can focus on their own logic.
 */
public class LocalStorage {
    private static final String PREF_NAME = "ecommerce_local_store";
    private final SharedPreferences sharedPreferences;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void saveBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }
}

