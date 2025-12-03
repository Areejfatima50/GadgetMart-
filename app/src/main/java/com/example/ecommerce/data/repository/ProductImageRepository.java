package com.example.ecommerce.data.repository;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.example.ecommerce.data.local.LocalStorage;

public class ProductImageRepository {
    private static final String KEY_IMAGE_PREFIX = "product_image_";
    private final LocalStorage localStorage;

    public ProductImageRepository(Context context) {
        this.localStorage = new LocalStorage(context.getApplicationContext());
    }

    public void saveImage(String productId, Uri uri) {
        if (uri == null) return;
        localStorage.saveString(KEY_IMAGE_PREFIX + productId, uri.toString());
    }

    public Uri getImage(String productId) {
        String saved = localStorage.getString(KEY_IMAGE_PREFIX + productId);
        if (TextUtils.isEmpty(saved)) {
            return null;
        }
        return Uri.parse(saved);
    }
}

