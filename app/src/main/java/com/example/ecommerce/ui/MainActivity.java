package com.example.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ecommerce.R;
import com.example.ecommerce.data.model.User;
import com.example.ecommerce.data.repository.AuthRepository;
import com.example.ecommerce.data.repository.CartRepository;
import com.example.ecommerce.data.repository.OrderRepository;
import com.example.ecommerce.data.repository.ProductImageRepository;
import com.example.ecommerce.data.repository.ProductRepository;
import com.example.ecommerce.databinding.ActivityMainBinding;
import com.example.ecommerce.ui.fragments.ExploreFragment;
import com.example.ecommerce.ui.fragments.HomeFragment;
import com.example.ecommerce.ui.fragments.OrderHistoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private AuthRepository authRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private ProductImageRepository imageRepository;
    private TextView cartBadge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);
        productRepository = new ProductRepository(this);
        cartRepository = new CartRepository(this, productRepository);
        orderRepository = new OrderRepository(this, productRepository);
        imageRepository = new ProductImageRepository(this);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("");


        binding.bottomNav.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            boolean openOrders = getIntent().getBooleanExtra("open_orders", false);
            binding.bottomNav.setSelectedItemId(openOrders ? R.id.nav_orders : R.id.nav_home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        View actionView = cartItem.getActionView();
        cartBadge = actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(v -> onOptionsItemSelected(cartItem));

        User user = authRepository.getActiveUser();
        if (user != null) {
            cartRepository.getCartLiveData(user.getEmail()).observe(this, cartItems -> {
                if (cartItems.isEmpty()) {
                    cartBadge.setVisibility(View.GONE);
                } else {
                    cartBadge.setVisibility(View.VISIBLE);
                    cartBadge.setText(String.valueOf(cartItems.size()));
                }
            });
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getBooleanExtra("open_orders", false)) {
            binding.bottomNav.setSelectedItemId(R.id.nav_orders);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            authRepository.logout();
            startActivity(new Intent(this, SignInActivity.class));
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        String title;
        int itemId = item.getItemId();
        if (itemId == R.id.nav_explore) {
            fragment = ExploreFragment.newInstance();
            title = "Explore gadgets";
        } else if (itemId == R.id.nav_orders) {
            fragment = OrderHistoryFragment.newInstance();
            title = "Order history";
        } else {
            fragment = HomeFragment.newInstance();
            title = "Home spotlight";
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
        return true;
    }

    public AuthRepository getAuthRepository() {
        return authRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public CartRepository getCartRepository() {
        return cartRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public ProductImageRepository getImageRepository() {
        return imageRepository;
    }

    @Nullable
    public User getActiveUser() {
        return authRepository.getActiveUser();
    }
}
