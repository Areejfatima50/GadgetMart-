package com.example.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.data.model.CartItem;
import com.example.ecommerce.data.model.Order;
import com.example.ecommerce.data.model.User;
import com.example.ecommerce.data.repository.AuthRepository;
import com.example.ecommerce.data.repository.CartRepository;
import com.example.ecommerce.data.repository.OrderRepository;
import com.example.ecommerce.data.repository.ProductRepository;
import com.example.ecommerce.databinding.ActivityCheckoutBinding;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private AuthRepository authRepository;
    private ProductRepository productRepository;
    private User user;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productRepository = new ProductRepository(this);
        cartRepository = new CartRepository(this, productRepository);
        orderRepository = new OrderRepository(this, productRepository);
        authRepository = new AuthRepository(this);
        user = authRepository.getActiveUser();
        if (user == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        renderSummary();
        binding.btnPlaceOrder.setOnClickListener(v -> createOrder());

        binding.paymentGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.paymentDebit) {
                binding.cardDetailsLayout.setVisibility(View.VISIBLE);
            } else {
                binding.cardDetailsLayout.setVisibility(View.GONE);
            }
        });
    }

    private void renderSummary() {
        List<CartItem> items = cartRepository.getCart(user.getEmail());
        double total = cartRepository.getCartTotal(user.getEmail());
        binding.summaryList.setText(buildSummary(items));
        binding.summaryTotal.setText(currencyFormatter.format(total));
    }

    private String buildSummary(List<CartItem> items) {
        StringBuilder builder = new StringBuilder();
        for (CartItem item : items) {
            builder.append(item.getQuantity())
                    .append(" x ")
                    .append(item.getProduct().getName())
                    .append("\n");
        }
        return builder.toString();
    }

    private void createOrder() {
        String name = binding.fieldFullName.getText().toString().trim();
        String address = binding.fieldAddress.getText().toString().trim();
        String city = binding.fieldCity.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Complete delivery details", Toast.LENGTH_SHORT).show();
            return;
        }

        String paymentMethod;
        if (binding.paymentGroup.getCheckedRadioButtonId() == R.id.paymentDebit) {
            paymentMethod = "Debit Card";
            String cardName = binding.fieldCardName.getText().toString().trim();
            String cardNumber = binding.fieldCardNumber.getText().toString().trim();
            String expiry = binding.fieldExpiry.getText().toString().trim();
            if (TextUtils.isEmpty(cardName) || TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(expiry)) {
                Toast.makeText(this, "Complete all card details", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            paymentMethod = "Cash on Delivery";
        }

        List<CartItem> items = cartRepository.getCart(user.getEmail());
        if (items.isEmpty()) {
            Toast.makeText(this, "Cart empty", Toast.LENGTH_SHORT).show();
            return;
        }
        double total = cartRepository.getCartTotal(user.getEmail());
        Order order = orderRepository.placeOrder(user.getEmail(), items, total);
        cartRepository.clear(user.getEmail());

        Intent intent = new Intent(this, OrderSuccessActivity.class);
        intent.putExtra(OrderSuccessActivity.EXTRA_ORDER_ID, order.getId());
        startActivity(intent);
        finish();
    }
}
