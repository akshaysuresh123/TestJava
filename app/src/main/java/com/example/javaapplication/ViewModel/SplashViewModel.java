package com.example.javaapplication.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SplashViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> isTimerFinished = new MutableLiveData<>();

    public SplashViewModel(@NonNull Application application) {
        super(application);
        startTimer();
    }


    private void startTimer() {
        // Simulate a delay for splash screen (e.g., 2.5 seconds)
        new Thread(() -> {
            try {
                Thread.sleep(2500); // 3 seconds
                isTimerFinished.postValue(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public LiveData<Boolean> getTimerFinished() {
        return isTimerFinished;
    }
}
