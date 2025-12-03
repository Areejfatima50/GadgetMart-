package com.example.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.data.repository.AuthRepository;
import com.example.ecommerce.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);

        binding.actionPrimary.setOnClickListener(v -> attemptSignUp());
        binding.toolbarBack.setNavigationOnClickListener(v -> onBackPressed());
        binding.linkSecondary.setOnClickListener(v ->
                startActivity(new Intent(this, SignInActivity.class)));
    }

    private void attemptSignUp() {
        String name = binding.fieldName.getText().toString().trim();
        String email = binding.fieldEmail.getText().toString().trim();
        String password = binding.fieldPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Fill in all details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            binding.fieldPassword.setError("Password must be at least 6 characters");
            return;
        }

        boolean created = authRepository.signUp(name, email, password);
        if (created) {
            Toast.makeText(this, "Account ready. Welcome!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        } else {
            Toast.makeText(this, "Account exists. Try signing in.", Toast.LENGTH_SHORT).show();
        }
    }
}

