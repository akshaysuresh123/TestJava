package com.example.javaapplication.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> isLoginSuccessful = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsLoginSuccessful() {
        return isLoginSuccessful;
    }

    // Function to validate PIN
    public void validatePin(String pin) {
        // Simulating a valid PIN check (example: "1234")
        if ("1234".equals(pin)) {
            isLoginSuccessful.setValue(true);
        } else {
            isLoginSuccessful.setValue(false);
        }
    }
}
