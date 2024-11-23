package com.example.javaapplication.Ui.Fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.javaapplication.Adapter.BluetoothDeviceAdapter;
import com.example.javaapplication.Datamodel.BluetoothDeviceItem;
import com.example.javaapplication.ViewModel.ConnectedDeviceViewModel;
import com.example.javaapplication.databinding.FragmentConnectedDeviceBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConnectedDeviceFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSIONS = 2;
    private ConnectedDeviceViewModel mViewModel;
    private FragmentConnectedDeviceBinding binding;

    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDeviceItem> deviceList = new ArrayList<>();
    private BluetoothDeviceAdapter adapter;
    private BluetoothDevice selectedDevice;

    public static ConnectedDeviceFragment newInstance() {
        return new ConnectedDeviceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentConnectedDeviceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConnectedDeviceViewModel.class);

        // Set up RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BluetoothDeviceAdapter(deviceList, this::onDeviceSelected);
        binding.recyclerView.setAdapter(adapter);

        // Set up scan button
        binding.scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDeviceDiscovery();
            }
        });

        // Disable the back press
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Do nothing or show a message if required
                    }
                }
        );

        // Check Bluetooth availability
        BluetoothManager bluetoothManager = getSystemService(getContext(), BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getContext(), "Bluetooth not supported on this device.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Request permissions if needed
        requestPermissionsIfNeeded();
    }

    // Request necessary Bluetooth permissions
    private void requestPermissionsIfNeeded() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_PERMISSIONS);
        }
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                Toast.makeText(getContext(), "Permissions granted!", Toast.LENGTH_SHORT).show();
                startDeviceDiscovery();
            } else {
                Toast.makeText(getContext(), "Permissions denied. Bluetooth features won't work.", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Start device discovery
    private void startDeviceDiscovery() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsIfNeeded();
            return;
        }

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            // Start scanning for nearby Bluetooth devices
            bluetoothAdapter.startDiscovery();
        }
    }

    // BroadcastReceiver for when a new device is discovered
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                String deviceName = device != null ? device.getName() : "Unknown Device";
                String deviceAddress = device != null ? device.getAddress() : "";
                deviceList.add(new BluetoothDeviceItem(deviceName, deviceAddress));
                adapter.notifyDataSetChanged();
            }
        }
    };

    // Handle device selection
    private void onDeviceSelected(BluetoothDeviceItem deviceItem) {
        String deviceName = deviceItem.getName();
        String deviceAddress = deviceItem.getAddress();
        selectedDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
        connectToDevice(selectedDevice);
    }

    // Connect to the selected Bluetooth device
    private void connectToDevice(BluetoothDevice device) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsIfNeeded();
            return;
        }

        device.createBond();  // Attempt to pair with the device
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(receiver, filter);
        // Register for bond state changes
        IntentFilter bondFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(bondReceiver, bondFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister the receiver to avoid memory leaks
        if (receiver != null) {
            getContext().unregisterReceiver(receiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister Bluetooth bond state receiver if it's registered
        if (bondReceiver != null) {
            getActivity().unregisterReceiver(bondReceiver);
        }
    }

    // BroadcastReceiver to handle bonding state changes
    private final BroadcastReceiver bondReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                String deviceName = device != null ? device.getName() : "Unknown";

                if (bondState == BluetoothDevice.BOND_BONDED) {
                    Log.d("BondState", "Device Paired: " + deviceName);
                    // Handle successful pairing
                } else if (bondState == BluetoothDevice.BOND_NONE) {
                    Log.d("BondState", "Device Unpaired or Pairing Failed: " + deviceName);
                }
            }
        }
    };
}
