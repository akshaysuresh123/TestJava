package com.example.javaapplication.Ui.Fragments;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.javaapplication.Datamodel.Farmer;
import com.example.javaapplication.Helper.DbHelper;
import com.example.javaapplication.ViewModel.DashboardViewmodel;
import com.example.javaapplication.ViewModel.FarmerRegistrationViewModel;
import com.example.javaapplication.databinding.FragmentFarmerRegistrationBinding;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class FarmerRegistrationFragment extends Fragment {

    private FarmerRegistrationViewModel mViewModel;
    private DashboardViewmodel dashboardViewmodel;
    private FragmentFarmerRegistrationBinding binding;
    private ActivityResultLauncher<String> selectImageLauncher;

    public static FarmerRegistrationFragment newInstance() {
        return new FarmerRegistrationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create an activity result launcher
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        // Set the selected image in the ImageView
                        binding.selectedImageView.setImageURI(uri);
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = FragmentFarmerRegistrationBinding.inflate(inflater, container, false);

        dashboardViewmodel = new ViewModelProvider(requireActivity()).get(DashboardViewmodel.class);
        mViewModel = new ViewModelProvider(this).get(FarmerRegistrationViewModel.class);

        mViewModel.getInsertion_status().observe(getViewLifecycleOwner(), success -> {
            if (success.equals("success")) {
                Toast.makeText(getContext(), "Data saved successfully!", Toast.LENGTH_SHORT).show();
                dashboardViewmodel.set_add_register_press("Farmer register");
            } else {
                // Handle failure case here if necessary
            }
        });

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

        setUI(); // Initialize UI components
        return binding.getRoot();
    }

    private void setUI() {
        Calendar calendar = Calendar.getInstance();

        // Open DatePicker when clicking the EditText or the calendar icon
        binding.dateEditText.setOnClickListener(v -> showDatePicker(calendar, binding.dateEditText));

        // Set button click listener
        binding.galleryButton.setOnClickListener(v -> {
            // Open the gallery to pick an image
            selectImageLauncher.launch("image/*");
        });

        // Set Save button click listener
        binding.saveButton.setOnClickListener(v -> saveFarmerData());

        AutoCompleteTextView genderDropdown = binding.gender;

        // List of gender options
        String[] genders = {"Male", "Female"};

        // Create an ArrayAdapter with the gender list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, genders);

        // Set the adapter to the AutoCompleteTextView
        genderDropdown.setAdapter(adapter);

        // Optionally handle dropdown item selection
        genderDropdown.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedGender = (String) parent.getItemAtPosition(position);
            // Do something with the selected gender
        });
    }

    private void saveFarmerData() {
        // Retrieve data from input fields
        String nationalId = binding.nationalid.getText().toString().trim();
        String farmId = binding.farmid.getText().toString().trim();
        String farmerName = binding.farmername.getText().toString().trim();
        String coopName = binding.coopname.getText().toString().trim();
        String coopId = binding.coopid.getText().toString().trim();
        String dob = binding.dateEditText.getText().toString().trim();
        String gender = binding.gender.getText().toString().trim();

        // Validate fields
        if (nationalId.isEmpty() || farmId.isEmpty() || farmerName.isEmpty() ||
                coopName.isEmpty() || coopId.isEmpty() || dob.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert image to blob with reduced quality
        byte[] imageBlob = convertImageViewToBlob(binding.selectedImageView);

        // Create a new Farmer object and insert data
        Farmer farmer = new Farmer(nationalId, farmId, farmerName, coopName, coopId, dob, gender, imageBlob);
        mViewModel.insertFarmer(farmer);
    }

    // Convert ImageView to Blob with reduced quality
    private byte[] convertImageViewToBlob(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        if (drawable == null) return null;

        Bitmap bitmap = drawable.getBitmap();

        // Resize image if it's too large (optional step to further reduce the size)
        Bitmap resizedBitmap = resizeBitmap(bitmap, 800, 600); // Resize to 800x600, you can adjust this

        // Compress bitmap with reduced quality (e.g., 80% quality)
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream); // Compress to JPEG with 80% quality
        return stream.toByteArray();
    }

    // Method to resize the bitmap (optional)
    private Bitmap resizeBitmap(Bitmap originalBitmap, int maxWidth, int maxHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        // Calculate the scaling ratio to fit the maxWidth and maxHeight
        float ratio = Math.min((float) maxWidth / width, (float) maxHeight / height);
        int newWidth = Math.round(ratio * width);
        int newHeight = Math.round(ratio * height);

        // Create a resized bitmap
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUI();
    }

    private void showDatePicker(Calendar calendar, EditText dateEditText) {
        DatePickerDialog datePicker = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    dateEditText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }
}
