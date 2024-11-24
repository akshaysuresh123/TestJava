package com.example.javaapplication.Ui.Fragments;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.javaapplication.Interfaces.Quicklinklistner;
import com.example.javaapplication.ViewModel.DashboardViewmodel;
import com.example.javaapplication.ViewModel.QuickLinksViewModel;
import com.example.javaapplication.databinding.FragmentQuickLinksBinding;

public class QuickLinksFragment extends Fragment {

    private QuickLinksViewModel mViewModel;
    private FragmentQuickLinksBinding binding;

    private DashboardViewmodel dashboardViewModel;

    public static QuickLinksFragment newInstance() {
        return new QuickLinksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = FragmentQuickLinksBinding.inflate(inflater, container, false);


        // Initialize ViewModel
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewmodel.class);

        binding.farmerRegCard.setOnClickListener(v -> {

           dashboardViewModel.quicklink_press("Farmer Registration");


            // TODO: Use the ViewModel
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuickLinksViewModel.class);
        // Disable the back press



}}