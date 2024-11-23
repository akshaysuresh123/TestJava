package com.example.javaapplication.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javaapplication.Datamodel.Farmer;
import com.example.javaapplication.Helper.FarmerRepository;

import java.util.List;

public class FarmerListViewModel extends AndroidViewModel {
    private FarmerRepository farmerRepository;
    private MutableLiveData<List<Farmer>>farmerlist;
    public LiveData<List<Farmer>>getfarmer_list(){return farmerlist;}
    public FarmerListViewModel(@NonNull Application application) {
        super(application);
        farmerRepository=new FarmerRepository(application);
        farmerlist=new MutableLiveData<>();
    }
    public void setFarmerList(List<Farmer> farmers) {
        farmerlist.setValue(farmers);
    }
    public void getAllFarmers() {
        farmerRepository.getAllFarmers(new FarmerRepository.Callback() {
            @Override
            public void onResult(List<Farmer> farmers) {
                farmerlist.postValue(farmers);
            }
        });
    }

    public void getAllFarmersbyfilter(String gender) {
        farmerRepository.getAllFarmersbyFilter(gender,new FarmerRepository.Callback() {
            @Override
            public void onResult(List<Farmer> farmers) {
                farmerlist.postValue(farmers);
            }
        });
    }



    public void  getFarmerbykey(String key,String val){
        farmerRepository.getFarmerbykey(key,val,new FarmerRepository.Callback() {
            @Override
            public void onResult(List<Farmer> farmers) {

                farmerlist.postValue(farmers);

            }
        });
    }
}