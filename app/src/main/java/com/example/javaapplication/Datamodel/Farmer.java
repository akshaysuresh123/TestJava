package com.example.javaapplication.Datamodel;

import java.util.Arrays;

public class Farmer {

    private String nationalID; // National ID
    private String farmID;     // Farm ID
    private String farmerName; // Farmer Name
    private String coopName;   // Cooperative Name
    private String coopID;     // Cooperative ID
    private String dob;        // Date of Birth
    private String gender;     // Gender
    private byte[] farmerPhoto; // Farmer Photo as Blob

    // Constructor
    public Farmer(String nationalID, String farmID, String farmerName, String coopName,
                  String coopID, String dob, String gender, byte[] farmerPhoto) {
        this.nationalID = nationalID;
        this.farmID = farmID;
        this.farmerName = farmerName;
        this.coopName = coopName;
        this.coopID = coopID;
        this.dob = dob;
        this.gender = gender;
        this.farmerPhoto = farmerPhoto;
    }

    // Default Constructor
    public Farmer() {
    }

    // Getters and Setters
    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getFarmID() {
        return farmID;
    }

    public void setFarmID(String farmID) {
        this.farmID = farmID;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getCoopName() {
        return coopName;
    }

    public void setCoopName(String coopName) {
        this.coopName = coopName;
    }

    public String getCoopID() {
        return coopID;
    }

    public void setCoopID(String coopID) {
        this.coopID = coopID;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte[] getFarmerPhoto() {
        return farmerPhoto;
    }

    public void setFarmerPhoto(byte[] farmerPhoto) {
        this.farmerPhoto = farmerPhoto;
    }

    @Override
    public String toString() {
        return "Farmer{" +
                "nationalID='" + nationalID + '\'' +
                ", farmID='" + farmID + '\'' +
                ", farmerName='" + farmerName + '\'' +
                ", coopName='" + coopName + '\'' +
                ", coopID='" + coopID + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", farmerPhoto=" + Arrays.toString(farmerPhoto) +
                '}';
    }
}
