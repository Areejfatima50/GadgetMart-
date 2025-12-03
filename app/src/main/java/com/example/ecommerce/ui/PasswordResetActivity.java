package com.example.ecommerce.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.data.repository.AuthRepository;
import com.example.ecommerce.databinding.ActivityPasswordResetBinding;

public class PasswordResetActivity extends AppCompatActivity {

    private ActivityPasswordResetBinding binding;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);

        binding.toolbarBack.setNavigationOnClickListener(v -> onBackPressed());
        binding.actionPrimary.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String email = binding.fieldEmail.getText().toString().trim();
        String password = binding.fieldPassword.getText().toString().trim();
        String confirmPassword = binding.fieldConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Provide your email and new password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            binding.fieldPassword.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            binding.fieldConfirmPassword.setError("Passwords do not match");
            return;
        }

        boolean updated = authRepository.resetPassword(email, password);
        if (updated) {
            Toast.makeText(this, "Password updated. Sign in again.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Account not found. Try signing up.", Toast.LENGTH_SHORT).show();
        }
    }
}
