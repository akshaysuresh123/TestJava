package com.example.javaapplication.Ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.javaapplication.ViewModel.SplashViewModel;
import com.example.javaapplication.databinding.ActivitySplashBinding;


public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private SplashViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        // Observe the timer
        viewModel.getTimerFinished().observe(this, isFinished -> {
            if (isFinished != null && isFinished) {
                navigateToNextScreen();
            }
        });
    }

    private void navigateToNextScreen() {
        // Example: Navigate to MainActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the SplashActivity
    }
}
