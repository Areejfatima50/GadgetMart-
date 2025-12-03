package com.example.ecommerce.data.repository;

import android.content.Context;
import android.text.TextUtils;

import com.example.ecommerce.data.local.LocalStorage;
import com.example.ecommerce.data.model.CartItem;
import com.example.ecommerce.data.model.Order;
import com.example.ecommerce.data.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepository {
    private static final String KEY_ORDERS_PREFIX = "orders_";

    private final LocalStorage localStorage;
    private final ProductRepository productRepository;

    public OrderRepository(Context context, ProductRepository productRepository) {
        this.localStorage = new LocalStorage(context.getApplicationContext());
        this.productRepository = productRepository;
    }

    public Order placeOrder(String email, List<CartItem> items, double total) {
        Order order = new Order(UUID.randomUUID().toString(), System.currentTimeMillis(), items, total);
        JSONArray existing = getOrdersArray(email);
        existing.put(encode(order));
        localStorage.saveString(keyFor(email), existing.toString());
        return order;
    }

    public List<Order> getOrders(String email) {
        JSONArray existing = getOrdersArray(email);
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < existing.length(); i++) {
            JSONObject obj = existing.optJSONObject(i);
            if (obj == null) continue;
            orders.add(decode(obj));
        }
        return orders;
    }

    private JSONObject encode(Order order) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", order.getId());
            obj.put("placedAt", order.getPlacedAt());
            obj.put("total", order.getTotal());
            JSONArray itemsArr = new JSONArray();
            for (CartItem item : order.getItems()) {
                JSONObject itemObj = new JSONObject();
                itemObj.put("productId", item.getProduct().getId());
                itemObj.put("quantity", item.getQuantity());
                itemsArr.put(itemObj);
            }
            obj.put("items", itemsArr);
        } catch (JSONException ignored) {
        }
        return obj;
    }

    private Order decode(JSONObject obj) {
        List<CartItem> items = new ArrayList<>();
        JSONArray arr = obj.optJSONArray("items");
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject itemObj = arr.optJSONObject(i);
                if (itemObj == null) continue;
                Product product = productRepository.getById(itemObj.optString("productId"));
                if (product == null) continue;
                int quantity = itemObj.optInt("quantity", 1);
                items.add(new CartItem(product, Math.max(1, quantity)));
            }
        }
        return new Order(
                obj.optString("id"),
                obj.optLong("placedAt"),
                items,
                obj.optDouble("total", 0)
        );
    }

    private JSONArray getOrdersArray(String email) {
        String raw = localStorage.getString(keyFor(email));
        if (TextUtils.isEmpty(raw)) {
            return new JSONArray();
        }
        try {
            return new JSONArray(raw);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    private String keyFor(String email) {
        return KEY_ORDERS_PREFIX + (TextUtils.isEmpty(email) ? "guest" : email.toLowerCase());
    }
}

