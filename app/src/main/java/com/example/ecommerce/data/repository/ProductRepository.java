package com.example.ecommerce.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.ecommerce.data.db.DatabaseHelper;
import com.example.ecommerce.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    public static final String CATEGORY_LAPTOPS = "Laptops";
    public static final String CATEGORY_PHONES = "Mobile Phones";
    public static final String CATEGORY_TABLETS = "Tablets";

    private final DatabaseHelper dbHelper;

    public ProductRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public List<Product> getAllProducts() {
        return getProducts(null, null);
    }

    public List<Product> getByCategory(String category) {
        return getProducts(category, null);
    }

    public List<Product> search(String query) {
        return getProducts(null, query);
    }

    public List<Product> searchInCategory(String category, String query) {
        return getProducts(category, query);
    }

    private List<Product> getProducts(String category, String query) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = null;
        List<String> selectionArgs = new ArrayList<>();

        if (!TextUtils.isEmpty(category)) {
            selection = DatabaseHelper.COLUMN_PRODUCT_CATEGORY + " = ?";
            selectionArgs.add(category);
        }

        if (!TextUtils.isEmpty(query)) {
            String likeQuery = "%" + query + "%";
            if (selection == null) {
                selection = DatabaseHelper.COLUMN_PRODUCT_NAME + " LIKE ?";
            } else {
                selection += " AND " + DatabaseHelper.COLUMN_PRODUCT_NAME + " LIKE ?";
            }
            selectionArgs.add(likeQuery);
        }

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null,
                selection, selectionArgs.toArray(new String[0]), null, null, null);

        if (cursor.moveToFirst()) {
            do {
                products.add(fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    public Product getById(String productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DatabaseHelper.COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = { productId };
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);

        Product product = null;
        if (cursor.moveToFirst()) {
            product = fromCursor(cursor);
        }
        cursor.close();
        return product;
    }

    private Product fromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORY));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION));
        String details = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DETAILS));
        float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_RATING));
        int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE));

        return new Product(id, name, category, price, description, details, rating, image);
    }
}
