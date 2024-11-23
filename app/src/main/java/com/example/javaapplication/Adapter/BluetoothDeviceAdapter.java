package com.example.javaapplication.Adapter;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.javaapplication.Datamodel.BluetoothDeviceItem;
import com.example.javaapplication.R;

import java.util.List;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    private List<BluetoothDeviceItem> deviceList;
    private OnDeviceClickListener onDeviceClickListener;

    public BluetoothDeviceAdapter(List<BluetoothDeviceItem> deviceList, OnDeviceClickListener onDeviceClickListener) {
        this.deviceList = deviceList;
        this.onDeviceClickListener = onDeviceClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BluetoothDeviceItem item = deviceList.get(position);
        holder.deviceName.setText(item.getName());


        if (item.isConnected()) {
            holder.deviceStatus.setText("Connected");
        } else {
            holder.deviceStatus.setText("Not Connected");
        }

        holder.itemView.setOnClickListener(v -> onDeviceClickListener.onDeviceClick(item));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public interface OnDeviceClickListener {
        void onDeviceClick(BluetoothDeviceItem deviceItem);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView deviceName;
        TextView deviceAddress;
        TextView deviceStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
            deviceStatus = itemView.findViewById(R.id.deviceStatus);
        }
    }
}
