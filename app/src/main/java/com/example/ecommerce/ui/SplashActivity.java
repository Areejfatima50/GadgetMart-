package com.example.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.data.repository.AuthRepository;
import com.example.ecommerce.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        binding.logo.startAnimation(fadeIn);
        binding.brandName.startAnimation(fadeIn);
        binding.title.startAnimation(fadeIn);
        binding.featuresList.startAnimation(fadeIn);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (authRepository.getActiveUser() != null) {
                authRepository.logout();
                startActivity(new Intent(this, SignInActivity.class));
            } else {
                startActivity(new Intent(this, SignInActivity.class));
            }
            finish();
        }, 1500);
    }
}

