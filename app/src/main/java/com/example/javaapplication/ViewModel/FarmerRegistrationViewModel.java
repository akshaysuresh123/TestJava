package com.example.javaapplication.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javaapplication.Datamodel.Farmer;
import com.example.javaapplication.Helper.FarmerRepository;

public class FarmerRegistrationViewModel extends AndroidViewModel {
    private FarmerRepository farmerRepository;
    private MutableLiveData<String>insertion_status=new MutableLiveData<>("default");
    public LiveData<String> getInsertion_status(){return insertion_status;}
    public void setInsertion_status(String val){insertion_status.postValue(val);}

    public void insertFarmer(Farmer farmer){
        farmerRepository.insertFarmer(farmer, new FarmerRepository.Callbackforinsert() {
            @Override
            public void onResult(String farmers) {
                setInsertion_status(farmers);
            }
        });
    }
    public FarmerRegistrationViewModel(@NonNull Application application) {
        super(application);
        farmerRepository=new FarmerRepository(application);
    }
    // TODO: Implement the ViewModel
}