package com.example.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.databinding.ActivityOrderSuccessBinding;

public class OrderSuccessActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "extra_order_id";
    private ActivityOrderSuccessBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
        binding.orderId.setText(orderId);

        binding.btnKeepShopping.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        binding.btnViewOrders.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("open_orders", true);
            startActivity(intent);
            finish();
        });
    }
}

