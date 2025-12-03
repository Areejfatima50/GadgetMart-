package com.example.ecommerce.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ecommerce.R;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecommerce.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_CATEGORY = "category";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCT_DETAILS = "details";
    public static final String COLUMN_PRODUCT_RATING = "rating";
    public static final String COLUMN_PRODUCT_IMAGE = "image";

    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_USER_PASSWORD + " TEXT" +
                    ");";

    private static final String TABLE_CREATE_PRODUCTS = 
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_PRODUCT_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_PRODUCT_NAME + " TEXT, " +
                    COLUMN_PRODUCT_CATEGORY + " TEXT, " +
                    COLUMN_PRODUCT_PRICE + " REAL, " +
                    COLUMN_PRODUCT_DESCRIPTION + " TEXT, " +
                    COLUMN_PRODUCT_DETAILS + " TEXT, " +
                    COLUMN_PRODUCT_RATING + " REAL, " +
                    COLUMN_PRODUCT_IMAGE + " INTEGER" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_PRODUCTS);
        seedProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    private void seedProducts(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_ID, "laptop_1");
        values.put(COLUMN_PRODUCT_NAME, "NovaBook Pro 15");
        values.put(COLUMN_PRODUCT_CATEGORY, "Laptops");
        values.put(COLUMN_PRODUCT_PRICE, 129999.00);
        values.put(COLUMN_PRODUCT_DESCRIPTION, "Creator-ready laptop with 12-core CPU and RTX graphics.");
        values.put(COLUMN_PRODUCT_DETAILS, "Intel i7 • 16GB RAM • 1TB SSD");
        values.put(COLUMN_PRODUCT_RATING, 4.8f);
        values.put(COLUMN_PRODUCT_IMAGE, R.drawable.dell_z23);
        db.insert(TABLE_PRODUCTS, null, values);

        values.clear();
        values.put(COLUMN_PRODUCT_ID, "laptop_2");
        values.put(COLUMN_PRODUCT_NAME, "AeroFlex 14");
        values.put(COLUMN_PRODUCT_CATEGORY, "Laptops");
        values.put(COLUMN_PRODUCT_PRICE, 109999.00);
        values.put(COLUMN_PRODUCT_DESCRIPTION, "2-in-1 ultrabook that handles work and binge sessions.");
        values.put(COLUMN_PRODUCT_DETAILS, "AMD Ryzen 7 • 16GB RAM • 512GB SSD");
        values.put(COLUMN_PRODUCT_RATING, 4.5f);
        values.put(COLUMN_PRODUCT_IMAGE, R.drawable.dell_zbook);
        db.insert(TABLE_PRODUCTS, null, values);

        values.clear();
        values.put(COLUMN_PRODUCT_ID, "phone_1");
        values.put(COLUMN_PRODUCT_NAME, "Pulse One X");
        values.put(COLUMN_PRODUCT_CATEGORY, "Mobile Phones");
        values.put(COLUMN_PRODUCT_PRICE, 89999.00);
        values.put(COLUMN_PRODUCT_DESCRIPTION, "Flagship camera system with all-day battery life.");
        values.put(COLUMN_PRODUCT_DETAILS, "6.7\" OLED • 256GB • 50MP triple camera");
        values.put(COLUMN_PRODUCT_RATING, 4.7f);
        values.put(COLUMN_PRODUCT_IMAGE, R.drawable.vivo_s21);
        db.insert(TABLE_PRODUCTS, null, values);

        values.clear();
        values.put(COLUMN_PRODUCT_ID, "phone_2");
        values.put(COLUMN_PRODUCT_NAME, "Pulse Mini");
        values.put(COLUMN_PRODUCT_CATEGORY, "Mobile Phones");
        values.put(COLUMN_PRODUCT_PRICE, 79999.00);
        values.put(COLUMN_PRODUCT_DESCRIPTION, "Compact powerhouse with premium aluminum frame.");
        values.put(COLUMN_PRODUCT_DETAILS, "6.1\" AMOLED • 128GB • 48MP dual camera");
        values.put(COLUMN_PRODUCT_RATING, 4.4f);
        values.put(COLUMN_PRODUCT_IMAGE, R.drawable.vivo_v21);
        db.insert(TABLE_PRODUCTS, null, values);

        values.clear();
        values.put(COLUMN_PRODUCT_ID, "tablet_1");
        values.put(COLUMN_PRODUCT_NAME, "CanvasTab 11");
        values.put(COLUMN_PRODUCT_CATEGORY, "Tablets");
        values.put(COLUMN_PRODUCT_PRICE, 74999.00);
        values.put(COLUMN_PRODUCT_DESCRIPTION, "Sketch, stream, and study with the responsive 120Hz display.");
        values.put(COLUMN_PRODUCT_DETAILS, "11\" LTPO • 8GB RAM • 256GB");
        values.put(COLUMN_PRODUCT_RATING, 4.6f);
        values.put(COLUMN_PRODUCT_IMAGE, R.drawable.tab_s25);
        db.insert(TABLE_PRODUCTS, null, values);

        values.clear();
        values.put(COLUMN_PRODUCT_ID, "tablet_2");
        values.put(COLUMN_PRODUCT_NAME, "CanvasTab 13 Studio");
        values.put(COLUMN_PRODUCT_CATEGORY, "Tablets");
        values.put(COLUMN_PRODUCT_PRICE, 99999.00);
        values.put(COLUMN_PRODUCT_DESCRIPTION, "For digital artists who need pen precision on the go.");
        values.put(COLUMN_PRODUCT_DETAILS, "13\" OLED • 12GB RAM • 512B");
        values.put(COLUMN_PRODUCT_RATING, 4.9f);
        values.put(COLUMN_PRODUCT_IMAGE, R.drawable.tab_y24);
        db.insert(TABLE_PRODUCTS, null, values);
    }
}
