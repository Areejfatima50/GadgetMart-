package com.example.ecommerce.data.repository;

import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerce.data.local.LocalStorage;
import com.example.ecommerce.data.model.CartItem;
import com.example.ecommerce.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private static final String KEY_CART_PREFIX = "cart_";

    private final LocalStorage localStorage;
    private final ProductRepository productRepository;
    private static final MutableLiveData<List<CartItem>> cartLiveData = new MutableLiveData<>();

    public CartRepository(Context context, ProductRepository productRepository) {
        this.localStorage = new LocalStorage(context.getApplicationContext());
        this.productRepository = productRepository;
    }

    public LiveData<List<CartItem>> getCartLiveData(String email) {
        cartLiveData.postValue(getCart(email));
        return cartLiveData;
    }

    public List<CartItem> getCart(String email) {
        String raw = localStorage.getString(keyFor(email));
        return decode(raw);
    }

    public void addToCart(String email, Product product) {
        List<CartItem> items = getCart(email);
        boolean found = false;
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }
        if (!found) {
            items.add(new CartItem(product, 1));
        }
        persist(email, items);
    }

    public void updateQuantity(String email, Product product, int quantity) {
        List<CartItem> items = getCart(email);
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(Math.max(1, quantity));
                break;
            }
        }
        persist(email, items);
    }

    public void removeItem(String email, Product product) {
        List<CartItem> items = getCart(email);
        List<CartItem> remaining = new ArrayList<>();
        for (CartItem item : items) {
            if (!item.getProduct().getId().equals(product.getId())) {
                remaining.add(item);
            }
        }
        persist(email, remaining);
    }

    public void clear(String email) {
        localStorage.remove(keyFor(email));
    }

    public double getCartTotal(String email) {
        double total = 0;
        for (CartItem item : getCart(email)) {
            total += item.getLineTotal();
        }
        return total;
    }

    private void persist(String email, List<CartItem> items) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            builder.append(item.getProduct().getId()).append(":").append(item.getQuantity());
            if (i < items.size() - 1) {
                builder.append(";");
            }
        }
        localStorage.saveString(keyFor(email), builder.toString());
        cartLiveData.postValue(items);
    }

    private List<CartItem> decode(String raw) {
        List<CartItem> items = new ArrayList<>();
        if (TextUtils.isEmpty(raw)) {
            return items;
        }
        String[] tokens = raw.split(";");
        for (String token : tokens) {
            String[] pair = token.split(":");
            if (pair.length != 2) continue;
            Product product = productRepository.getById(pair[0]);
            if (product == null) continue;
            try {
                int quantity = Integer.parseInt(pair[1]);
                items.add(new CartItem(product, Math.max(1, quantity)));
            } catch (NumberFormatException ignored) {
            }
        }
        return items;
    }

    private String keyFor(String email) {
        return KEY_CART_PREFIX + (TextUtils.isEmpty(email) ? "guest" : email.toLowerCase());
    }
}
