package com.example.ecommerce.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.data.local.LocalStorage;
import com.example.ecommerce.data.repository.AuthRepository;
import com.example.ecommerce.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    private static final String KEY_REMEMBER_EMAIL = "remember_email";
    private static final String KEY_REMEMBER_PASSWORD = "remember_password";
    private static final String KEY_REMEMBER_ME = "remember_me";

    private ActivitySignInBinding binding;
    private AuthRepository authRepository;
    private LocalStorage localStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);
        localStorage = new LocalStorage(this);

        binding.actionPrimary.setOnClickListener(v -> attemptSignIn());
        binding.linkSecondary.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
        binding.linkTertiary.setOnClickListener(v ->
                startActivity(new Intent(this, PasswordResetActivity.class)));

        loadCredentials();
    }

    private void loadCredentials() {
        if (localStorage.getBoolean(KEY_REMEMBER_ME, false)) {
            String email = localStorage.getString(KEY_REMEMBER_EMAIL);
            String password = localStorage.getString(KEY_REMEMBER_PASSWORD);
            binding.fieldEmail.setText(email);
            binding.fieldPassword.setText(password);
            binding.checkboxRemember.setChecked(true);
        }
    }

    private void attemptSignIn() {
        String email = binding.fieldEmail.getText().toString().trim();
        String password = binding.fieldPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter your email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.checkboxRemember.isChecked()) {
            localStorage.saveString(KEY_REMEMBER_EMAIL, email);
            localStorage.saveString(KEY_REMEMBER_PASSWORD, password);
            localStorage.saveBoolean(KEY_REMEMBER_ME, true);
        } else {
            localStorage.remove(KEY_REMEMBER_EMAIL);
            localStorage.remove(KEY_REMEMBER_PASSWORD);
            localStorage.remove(KEY_REMEMBER_ME);
        }

        if (authRepository.signIn(email, password)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}

