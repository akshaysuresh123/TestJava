package com.example.javaapplication.Helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.javaapplication.Datamodel.Farmer;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FarmersDB";
    private static final int DATABASE_VERSION = 1;

    // Table and Column Names
    private static final String TABLE_FARMER = "Farmer";
    private static final String COLUMN_NATIONAL_ID = "NationalID";
    private static final String COLUMN_FARM_ID = "FarmID";
    private static final String COLUMN_FARMER_NAME = "FarmerName";
    private static final String COLUMN_COOP_NAME = "CoopName";
    private static final String COLUMN_COOP_ID = "CoopID";
    private static final String COLUMN_DOB = "DOB";
    private static final String COLUMN_GENDER = "Gender";
    private static final String COLUMN_FARMER_PHOTO = "FarmerPhoto";

    // Constructor
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Farmer Table
        String CREATE_FARMER_TABLE = "CREATE TABLE " + TABLE_FARMER + "("
                + COLUMN_NATIONAL_ID + " TEXT PRIMARY KEY, "
                + COLUMN_FARM_ID + " TEXT, "
                + COLUMN_FARMER_NAME + " TEXT, "
                + COLUMN_COOP_NAME + " TEXT, "
                + COLUMN_COOP_ID + " TEXT, "
                + COLUMN_DOB + " TEXT, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_FARMER_PHOTO + " BLOB)";
        db.execSQL(CREATE_FARMER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FARMER);
        onCreate(db);
    }

    @SuppressLint("Range")
    public Farmer getFarmerByKey(String key, String value) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            // Query to fetch a farmer based on the key and value
            cursor = db.query(TABLE_FARMER, null, key + "=?",
                    new String[]{value}, null, null, null);

            // Check if the cursor has data and return the Farmer object
            if (cursor != null && cursor.moveToFirst()) {
                return new Farmer(
                        cursor.getString(cursor.getColumnIndex(COLUMN_NATIONAL_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FARM_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FARMER_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_COOP_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_COOP_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DOB)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)),
                        cursor.getBlob(cursor.getColumnIndex(COLUMN_FARMER_PHOTO))
                );
            }
            return null; // Return null if no farmer is found
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }





    // Insert Farmer Data
    public boolean insertFarmer(Farmer farmer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NATIONAL_ID, farmer.getNationalID());
        values.put(COLUMN_FARM_ID, farmer.getFarmID());
        values.put(COLUMN_FARMER_NAME, farmer.getFarmerName());
        values.put(COLUMN_COOP_NAME, farmer.getCoopName());
        values.put(COLUMN_COOP_ID, farmer.getCoopID());
        values.put(COLUMN_DOB, farmer.getDob());
        values.put(COLUMN_GENDER, farmer.getGender());
        values.put(COLUMN_FARMER_PHOTO, farmer.getFarmerPhoto());

        long result = db.insert(TABLE_FARMER, null, values);
        db.close();
        return result != -1; // Return true if insertion is successful
    }

    // Retrieve Farmer Data by National ID
    public Cursor getFarmerById(String nationalId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FARMER, null, COLUMN_NATIONAL_ID + "=?",
                new String[]{nationalId}, null, null, null);
    }
    @SuppressLint("Range")
    public List<Farmer> getFarmersByGender(String gender) {
        List<Farmer> farmers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get farmers filtered by gender
        String selectQuery = "SELECT * FROM " + TABLE_FARMER + " WHERE " + COLUMN_GENDER + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{gender});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                     String nationalID = cursor.getString(cursor.getColumnIndex(COLUMN_NATIONAL_ID));
                     String farmID = cursor.getString(cursor.getColumnIndex(COLUMN_FARM_ID));
                     String farmerName = cursor.getString(cursor.getColumnIndex(COLUMN_FARMER_NAME));
                     String coopName = cursor.getString(cursor.getColumnIndex(COLUMN_COOP_NAME));
                     String coopID = cursor.getString(cursor.getColumnIndex(COLUMN_COOP_ID));
                     String dob = cursor.getString(cursor.getColumnIndex(COLUMN_DOB));
                     String farmerGender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
                     byte[] farmerPhoto = cursor.getBlob(cursor.getColumnIndex(COLUMN_FARMER_PHOTO));

                    // Add farmer to the list
                    Farmer farmer = new Farmer(nationalID, farmID, farmerName, coopName, coopID, dob, farmerGender, farmerPhoto);
                    farmers.add(farmer);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return farmers;
    }




    // Fetch all farmers from the database
    public List<Farmer> getAllFarmers() {
        List<Farmer> farmers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get all farmers
        String selectQuery = "SELECT * FROM " + TABLE_FARMER;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String nationalID = cursor.getString(cursor.getColumnIndex(COLUMN_NATIONAL_ID));
                    @SuppressLint("Range") String farmID = cursor.getString(cursor.getColumnIndex(COLUMN_FARM_ID));
                    @SuppressLint("Range") String farmerName = cursor.getString(cursor.getColumnIndex(COLUMN_FARMER_NAME));
                    @SuppressLint("Range") String coopName = cursor.getString(cursor.getColumnIndex(COLUMN_COOP_NAME));
                    @SuppressLint("Range") String coopID = cursor.getString(cursor.getColumnIndex(COLUMN_COOP_ID));
                    @SuppressLint("Range") String dob = cursor.getString(cursor.getColumnIndex(COLUMN_DOB));
                    @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
                    @SuppressLint("Range") byte[] farmerPhoto = cursor.getBlob(cursor.getColumnIndex(COLUMN_FARMER_PHOTO));

                    // Add farmer to the list
                    Farmer farmer = new Farmer(nationalID, farmID, farmerName, coopName, coopID, dob, gender, farmerPhoto);
                    farmers.add(farmer);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return farmers;
    }


    // Update Farmer Data
    public boolean updateFarmer(Farmer farmer, String nationalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FARM_ID, farmer.getFarmID());
        values.put(COLUMN_FARMER_NAME, farmer.getFarmerName());
        values.put(COLUMN_COOP_NAME, farmer.getCoopName());
        values.put(COLUMN_COOP_ID, farmer.getCoopID());
        values.put(COLUMN_DOB, farmer.getDob());
        values.put(COLUMN_GENDER, farmer.getGender());
        values.put(COLUMN_FARMER_PHOTO, farmer.getFarmerPhoto());

        int rowsAffected = db.update(TABLE_FARMER, values, COLUMN_NATIONAL_ID + "=?",
                new String[]{nationalId});
        db.close();
        return rowsAffected > 0; // Return true if update is successful
    }

    // Delete Farmer Data
    public boolean deleteFarmer(String nationalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_FARMER, COLUMN_NATIONAL_ID + "=?",
                new String[]{nationalId});
        db.close();
        return rowsDeleted > 0; // Return true if deletion is successful
    }
}
