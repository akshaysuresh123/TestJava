package com.example.javaapplication.Helper;

import android.content.Context;

import com.example.javaapplication.Datamodel.Farmer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FarmerRepository {

    private DbHelper dbHelper;
    private ExecutorService executorService;

    public FarmerRepository(Context context) {
        dbHelper = new DbHelper(context);
        executorService = Executors.newSingleThreadExecutor(); // Executor for background tasks
    }

    // Method to insert a farmer into the database asynchronously
    public void insertFarmer(final Farmer farmer,Callbackforinsert Callbackforinsert) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                boolean sucess=dbHelper.insertFarmer(farmer);
                if(sucess){
                    Callbackforinsert.onResult("success");
                }
            }
        });
    }

    public void DeleteFarmer(final String nationalid,Callbackfordelete Callbackfordelete ) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                boolean sucess=dbHelper.deleteFarmer(nationalid);
                if(sucess){
                    Callbackfordelete.onResult(true);
                }
            }
        });
    }





    // Method to insert a farmer into the database asynchronously
    public void updateFarmer(final Farmer farmer,String nationalId,Callbackforupdate callbackforupdate) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                boolean sucess=dbHelper.updateFarmer(farmer,nationalId);
                if(sucess){
                    callbackforupdate.onResult(true);
                }
            }
        });
    }





    // Method to get all farmers asynchronously
    public void getAllFarmers(final Callback callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Farmer> farmers = dbHelper.getAllFarmers();
                callback.onResult(farmers); // Pass the result to the callback
            }
        });
    }

    public void getAllFarmersbyFilter(String gender,final Callback callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Farmer> farmers = dbHelper.getFarmersByGender(gender);
                callback.onResult(farmers); // Pass the result to the callback
            }
        });
    }




    public void getFarmerbykey(final String key,String value,final Callback callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Farmer farmers = dbHelper.getFarmerByKey(key,value);
                List<Farmer>f=new ArrayList<>();
                f.add(farmers);
                callback.onResult(f); // Pass the result to the callback
            }
        });
    }


    // Callback interface to handle the results of asynchronous operations
    public interface Callback {
        void onResult(List<Farmer> farmers);
    }

    // Callback interface to handle the results of asynchronous operations
    public interface Callbackforinsert {
        void onResult(String farmers);
    }

    // Callback interface to handle the results of asynchronous operations
    public interface Callbackforupdate {
        void onResult(Boolean farmers);
    }
    public interface Callbackfordelete {
        void onResult(Boolean farmers);
    }

}
