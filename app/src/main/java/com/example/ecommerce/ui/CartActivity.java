package com.example.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ecommerce.data.model.CartItem;
import com.example.ecommerce.data.model.User;
import com.example.ecommerce.data.repository.AuthRepository;
import com.example.ecommerce.data.repository.CartRepository;
import com.example.ecommerce.data.repository.ProductRepository;
import com.example.ecommerce.databinding.ActivityCartBinding;
import com.example.ecommerce.ui.adapter.CartAdapter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.Listener {

    private ActivityCartBinding binding;
    private CartRepository cartRepository;
    private AuthRepository authRepository;
    private ProductRepository productRepository;
    private CartAdapter adapter;
    private User user;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productRepository = new ProductRepository(this);
        cartRepository = new CartRepository(this, productRepository);
        authRepository = new AuthRepository(this);
        user = authRepository.getActiveUser();
        if (user == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new CartAdapter(this);
        binding.recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerCart.setHasFixedSize(true);
        binding.recyclerCart.setAdapter(adapter);
        binding.btnCheckout.setOnClickListener(v -> proceedToCheckout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            List<CartItem> items = cartRepository.getCart(user.getEmail());
            adapter.submitList(items);
            binding.emptyState.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
            binding.contentGroup.setVisibility(items.isEmpty() ? View.GONE : View.VISIBLE);
            binding.totalLabel.setText(currencyFormatter.format(cartRepository.getCartTotal(user.getEmail())));
        }
    }

    private void proceedToCheckout() {
        if (user == null) return;
        if (cartRepository.getCart(user.getEmail()).isEmpty()) {
            Toast.makeText(this, "Cart empty", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, CheckoutActivity.class));
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        if (user == null) return;
        cartRepository.updateQuantity(user.getEmail(), item.getProduct(), newQuantity);
        onResume();
    }

    @Override
    public void onRemove(CartItem item) {
        if (user == null) return;
        cartRepository.removeItem(user.getEmail(), item.getProduct());
        onResume();
    }
}

