package com.example.javaapplication.Ui.Fragments;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javaapplication.Adapter.FarmerAdapter;
import com.example.javaapplication.Datamodel.Farmer;
import com.example.javaapplication.Helper.DbHelper;
import com.example.javaapplication.R;
import com.example.javaapplication.ViewModel.DashboardViewmodel;
import com.example.javaapplication.ViewModel.FarmerListViewModel;
import com.example.javaapplication.databinding.FragmentFarmerListBinding;
import com.example.javaapplication.databinding.PopupFilterBinding;

import java.util.List;

public class FarmerListFragment extends Fragment implements FarmerAdapter.Fetchnewdata {

    private FarmerListViewModel mViewModel;
    private DashboardViewmodel dashboardViewmodel;
    private FragmentFarmerListBinding binding;
    private PopupFilterBinding popupFilterBinding;
    private RecyclerView recyclerView;
    private FarmerAdapter adapter;
    EditText nationalIdEditText, farmerIdEditText, farmerNameEditText;

    public static FarmerListFragment newInstance() {
        return new FarmerListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFarmerListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dashboardViewmodel=new ViewModelProvider(requireActivity()).get(DashboardViewmodel.class);

        mViewModel = new ViewModelProvider(this).get(FarmerListViewModel.class);
        recyclerView = binding.farmerList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewModel=new ViewModelProvider(this).get(FarmerListViewModel.class);
        mViewModel.getAllFarmers();
        mViewModel.getfarmer_list().observe(getViewLifecycleOwner(), farmers -> {
            // Pass the data to the adapter
            adapter = new FarmerAdapter(getContext(), farmers,this);
            recyclerView.setAdapter(adapter);

        });

        setClickevents();
        // Disable the back press
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Do nothing or show a message if required
                    }
                }
        );

    }
    private void setClickevents() {

        SwipeRefreshLayout swipeRefreshLayout = binding.swipeRefreshLayout;

// Set a refresh listener
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Fetch updated data
            refreshFarmerList();

            // Simulate data loading delay (for better UX)
            swipeRefreshLayout.postDelayed(() -> {
                // Stop the refreshing animation
                swipeRefreshLayout.setRefreshing(false);
            }, 1000); // 1 second delay for demonstration
        });


        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterPopup();
            }
        });

        binding.addFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dashboardViewmodel.set_add_farmer_press("add farmer");

            }
        });
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                performSearch();

            }
        });
        // Initialize Views
        nationalIdEditText = binding.nationalid;
        farmerIdEditText = binding.farmerid;
        farmerNameEditText = binding.farmername;

        // Set focus listeners
        setFocusListener(nationalIdEditText, farmerIdEditText, farmerNameEditText);
        setFocusListener(farmerIdEditText, nationalIdEditText, farmerNameEditText);
        setFocusListener(farmerNameEditText, nationalIdEditText, farmerIdEditText);

        // Attach the TextWatcher to each EditText
        nationalIdEditText.addTextChangedListener(textWatcher);
        farmerIdEditText.addTextChangedListener(textWatcher);
        farmerNameEditText.addTextChangedListener(textWatcher);

    }
    private void setFocusListener(EditText focusedEditText, EditText... others) {
        focusedEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Clear other EditText fields
                for (EditText other : others) {
                    other.setText("");
                }
            }
        });
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable editable) {
            // Check if all fields are empty after the text has changed
            if (areAllFieldsEmpty()) {
                // Disable search button or show message
                binding.searchButton.setEnabled(false); // Disable search if all fields are empty
                mViewModel.getAllFarmers();
            } else {
                // Enable search button if any field has content
                binding.searchButton.setEnabled(true);
            }
        }
    };
    private void refreshFarmerList() {
        // Fetch updated farmer data from ViewModel or database
        mViewModel.getAllFarmers();
    }


    private boolean areAllFieldsEmpty() {
        String nationalId = nationalIdEditText.getText().toString().trim();
        String farmerId = farmerIdEditText.getText().toString().trim();
        String farmerName = farmerNameEditText.getText().toString().trim();

        return nationalId.isEmpty() && farmerId.isEmpty() && farmerName.isEmpty();
    }

    private void performSearch() {
        String nationalId = nationalIdEditText.getText().toString().trim();
        String farmerId = farmerIdEditText.getText().toString().trim();
        String farmerName = farmerNameEditText.getText().toString().trim();

        if (nationalId.isEmpty() && farmerId.isEmpty() && farmerName.isEmpty()) {
            // Show error if all fields are empty
            Toast.makeText(getContext(), "Please enter a value to search.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform search based on the non-empty field
        if (!nationalId.isEmpty()) {
            mViewModel.getFarmerbykey("NationalID",nationalId);
        } else if (!farmerId.isEmpty()) {
            mViewModel.getFarmerbykey("FarmID",farmerId);
        } else if (!farmerName.isEmpty()) {
            mViewModel.getFarmerbykey("FarmerName",farmerName);
        }
    }

    @Override
    public void fetchnewdata(Boolean val) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "Farmer updated successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getAllFarmers();
    }


    private void showFilterPopup() {
        // Inflate the layout for the PopupWindow
        popupFilterBinding = PopupFilterBinding.inflate(getLayoutInflater());

        View popupView = popupFilterBinding.getRoot();

        // Initialize PopupWindow with the inflated layout
        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Make the popup focusable and dim the background
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        // Show the PopupWindow
        popupWindow.showAtLocation(binding.contentFrame, Gravity.END, 0, 0);

        // Get references to the UI elements
        CheckBox maleCheckBox = popupView.findViewById(R.id.checkbox_male);
        CheckBox femaleCheckBox = popupView.findViewById(R.id.checkbox_female);
        Button applyButton = popupView.findViewById(R.id.apply_button);

        // Apply button functionality
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve selected gender values
                boolean isMaleChecked = maleCheckBox.isChecked();
                boolean isFemaleChecked = femaleCheckBox.isChecked();

                // You can now use these values to filter the list or take appropriate action
                // Example: apply gender filter
                applyGenderFilter(isMaleChecked, isFemaleChecked);

                // Dismiss the popup window after applying filter
                popupWindow.dismiss();
            }
        });
    }

    // Example method to apply gender filter based on checkbox selections
    private void applyGenderFilter(boolean isMaleChecked, boolean isFemaleChecked) {
        if (isMaleChecked) {

        mViewModel.getAllFarmersbyfilter("Male");
            // Apply male filter logic
        }
        if (isFemaleChecked) {
            mViewModel.getAllFarmersbyfilter("Female");
            // Apply female filter logic
        }
    }


}