package com.example.javaapplication.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javaapplication.Interfaces.Quicklinklistner;

public class DashboardViewmodel extends AndroidViewModel {


    public DashboardViewmodel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<String> quicklink_press = new MutableLiveData<>("default");
    public LiveData<String> getQuicklink_press() {
        return quicklink_press;
    }

    private MutableLiveData<String> add_farmer_press = new MutableLiveData<>("");
    public LiveData<String> getAdd_farmer_press() {
        return add_farmer_press;
    }

    private MutableLiveData<String> add_register_press = new MutableLiveData<>("");
    public LiveData<String> getAdd_register_press() {
        return add_register_press;
    }



    public void quicklink_press(String val) {
        quicklink_press.setValue(val);

    }

    public void set_add_farmer_press(String val) {
        Log.e("TRIGGEREDf",val);
        try{
            add_farmer_press.setValue(val);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public void set_add_register_press(String val) {
        Log.e("TRIGGEREDf",val);
        try{
            add_register_press.setValue(val);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
