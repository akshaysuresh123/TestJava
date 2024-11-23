package com.example.javaapplication.Ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.javaapplication.R;
import com.example.javaapplication.Ui.Fragments.ConnectedDeviceFragment;
import com.example.javaapplication.Ui.Fragments.FarmerListFragment;
import com.example.javaapplication.Ui.Fragments.FarmerRegistrationFragment;
import com.example.javaapplication.Ui.Fragments.QuickLinksFragment;
import com.example.javaapplication.ViewModel.DashboardViewmodel;
import com.example.javaapplication.databinding.ActivityDashboardBinding;
import com.example.javaapplication.databinding.CustomToolbarBinding;
import com.example.javaapplication.databinding.PopupLayoutBinding;

import java.util.Calendar;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private DashboardViewmodel mViewModel;

    private PopupWindow popupWindow;
    private PopupLayoutBinding popupLayoutBinding;

    private void clearLoginStatus() {
        getSharedPreferences("AppPreferences", MODE_PRIVATE)
                .edit()
                .remove("IsLoggedIn")
                .apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Add QuickLinksFragment to FrameLayout using ViewBinding
        if (savedInstanceState == null) {
            QuickLinksFragment fragment = QuickLinksFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.contentFrame.getId(), fragment) // Use binding for FrameLayout ID
                    .commit();
        }

       SetUI();



        mViewModel=new ViewModelProvider(this).get(DashboardViewmodel.class);

        mViewModel.getAdd_farmer_press().observe(this, add_farmer_press -> {


            if(add_farmer_press.trim().equals("add farmer")){
                FarmerRegistrationFragment fragment = FarmerRegistrationFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(binding.contentFrame.getId(), fragment) // Use binding for FrameLayout ID
                        .commit();
            }


        });

        mViewModel.getAdd_register_press().observe(this, add_register_press -> {
            if(add_register_press.equals("Farmer register")) {

                FarmerListFragment fragment = FarmerListFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(binding.contentFrame.getId(), fragment) // Use binding for FrameLayout ID
                        .commit();
            }

        });



        mViewModel.getQuicklink_press().observe(this, quicklink_press -> {

            if(quicklink_press.equals("Farmer Registration")){
                FarmerListFragment fragment = FarmerListFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(binding.contentFrame.getId(), fragment) // Use binding for FrameLayout ID
                        .commit();
            }
        });




    }

    private void SetUI() {


        binding.connectedDeviceLayout.setOnClickListener(v -> {
            ConnectedDeviceFragment fragment = ConnectedDeviceFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.contentFrame.getId(), fragment) // Use binding for FrameLayout ID
                    .commit();
        });

        // Set up button actions
        // Set up button actions
        binding.menuIcon.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();  // Close the popup if it's already showing
            } else {
                showPopup(v);  // Show the popup
            }
        });

    }

    private void showPopup(View anchorView) {
        // Create the layout for the popup
        popupLayoutBinding=PopupLayoutBinding.inflate(getLayoutInflater());
        View popupView = popupLayoutBinding.getRoot();

        // Initialize the PopupWindow
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // Set focusable to true so that clicking outside the popup closes it
        popupWindow.setFocusable(true);

        // Get the location of the menu icon on the screen
        int[] location = new int[2];
        anchorView.getLocationInWindow(location);  // Get position of the menu icon

        // Add offset to the position to shift it more down and left
        int xOffset = -30;  // Move to the left by 30 pixels
        int yOffset = 40;   // Move down by 40 pixels

        // Show the PopupWindow with adjusted position
        popupWindow.showAtLocation(binding.getRoot(),
                Gravity.NO_GRAVITY, location[0] + xOffset, location[1] + anchorView.getHeight() + yOffset);

        popupLayoutBinding.quickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuickLinksFragment fragment = QuickLinksFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(binding.contentFrame.getId(), fragment) // Use binding for FrameLayout ID
                        .commit();


                popupWindow.dismiss();

            }
        });

        popupLayoutBinding.RegistrationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FarmerRegistrationFragment fragment = FarmerRegistrationFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(binding.contentFrame.getId(), fragment) // Use binding for FrameLayout ID
                        .commit();
                popupWindow.dismiss();

            }
        });
        popupLayoutBinding.SignoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "Signout", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                clearLoginStatus();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);
                finish();

            }
        });


    }








    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;

    }
}
