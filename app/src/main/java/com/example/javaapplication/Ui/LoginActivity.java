package com.example.javaapplication.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.javaapplication.ViewModel.LoginViewModel;
import com.example.javaapplication.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;


    private void navigateToNextScreen() {
        // Example: Navigate to MainActivity
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish(); // Close the SplashActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isUserLoggedIn()) {
            navigateToNextScreen(); // Skip login and go directly to Dashboard
            return;
        }

        // Initialize ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observe login success
        viewModel.getIsLoginSuccessful().observe(this, isSuccessful -> {
            if (isSuccessful != null) {
                if (isSuccessful) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    saveLoginStatus(true); // Save login status
                    navigateToNextScreen();
                    // Navigate to the next screen here
                } else {
                    Toast.makeText(this, "Invalid PIN. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setupEditTextFocusChange();

        // Set up login button click

        binding.loginButton.setOnClickListener(v -> {
            String pin = binding.pinInput1.getText().toString().trim();
            pin += binding.pinInput2.getText().toString().trim();
            pin += binding.pinInput3.getText().toString().trim();
            pin += binding.pinInput4.getText().toString().trim();

            if (pin.length() == 4) {
                viewModel.validatePin(pin);
            } else {
                Toast.makeText(this, "Please enter a 4-digit PIN", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageButtonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupEditTextFocusChange() {
        // EditText 1 to EditText 2
        binding.pinInput1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() == 1) {
                    binding.pinInput2.requestFocus();
                }
            }
        });

        // EditText 2 to EditText 3
        binding.pinInput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() == 1) {
                    binding.pinInput3.requestFocus();
                }
            }
        });

        // Optionally, you can add logic for the last EditText (no next field)
        binding.pinInput3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // No further focus shift for last EditText
                if (editable != null && editable.length() == 1) {
                    binding.pinInput4.requestFocus();
                }
            }
        });
    }

    private void saveLoginStatus(boolean isLoggedIn) {
        getSharedPreferences("AppPreferences", MODE_PRIVATE)
                .edit()
                .putBoolean("IsLoggedIn", isLoggedIn)
                .apply();
    }

    private boolean isUserLoggedIn() {
        return getSharedPreferences("AppPreferences", MODE_PRIVATE)
                .getBoolean("IsLoggedIn", false);
    }
}
