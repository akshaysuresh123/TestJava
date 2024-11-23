package com.example.javaapplication.Datamodel;

public class BluetoothDeviceItem {

    private String name;
    private String address;
    private boolean isConnected;

    public BluetoothDeviceItem(String name, String address) {
        this.name = name;
        this.address = address;
        this.isConnected = false;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
