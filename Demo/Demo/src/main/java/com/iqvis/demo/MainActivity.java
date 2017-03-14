package com.iqvis.demo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iqvis.blecomponent.activity.IqvisBLE;
import com.iqvis.blecomponent.interfaces.ConnectionCallBack;
import com.iqvis.blecomponent.interfaces.ScanCallBack;
import com.iqvis.blecomponent.modules.BLEDevice;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by IQVIS on 10/24/2016.

 Copyright 2016 IQVIS. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 "http://www.apache.org/licenses/LICENSE-2.0"

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License. */
public class MainActivity extends AppCompatActivity implements ScanCallBack,ConnectionCallBack {

    private IqvisBLE iqvisBLE;
    private Button rescan;
    private ListView device_list_view, service_list_view, characteristics_list_view;
    private TextView list_text;
    private BluetoothDevice connectedDevice;
    private List<BluetoothGattService> servicesList;
    private List<BluetoothDevice> scannedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runTimePermissions();
        iqvisBLE = new IqvisBLE(MainActivity.this, this, this);
        try{
            //noinspection ConstantConditions
            getSupportActionBar().hide();}
        catch (NullPointerException e){
            e.printStackTrace();
        }
        checkingBLESupport();
        initViews();
        iqvisBLE.ScanDevices();
    }

    private void checkingBLESupport() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(MainActivity.this, getString(R.string.text_ble_not_supported), Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!Utility.isBluetoothEnable()) {
            Toast.makeText(MainActivity.this, getString(com.iqvis.blecomponent.R.string.text_ble_not_enabled), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, AppConstants.REQUEST_BLUETOOTH_ENABLED);
        }
    }

    private void initViews() {
        rescan = (Button) findViewById(R.id.button_rescan);
//        Button connect = (Button) findViewById(R.id.button_connect);
//        Button disconnect = (Button) findViewById(R.id.button_disconnect);
        device_list_view = (ListView) findViewById(R.id.devices_list_view);
        service_list_view = (ListView) findViewById(R.id.services_list_view);
        characteristics_list_view = (ListView) findViewById(R.id.charactersitics_list_view);
        list_text = (TextView) findViewById(R.id.textView_devices);

//        TextView deviceName = (TextView) findViewById(R.id.textView_ble_name);
//        TextView deviceMac = (TextView) findViewById(R.id.textView_ble_mac);
//        TextView deviceServices = (TextView) findViewById(R.id.textView_ble_services);
        setListeners();
    }

    private void setListeners() {
        device_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connectedDevice = scannedDevices.get(position);
                iqvisBLE.ConnectDevice(connectedDevice);
            }
        });

        service_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<BluetoothGattCharacteristic> characteristics = servicesList.get(position).getCharacteristics();
                String[] charact = new String[characteristics.size()];
                for (int i = 0; i < characteristics.size(); i++) {
                    charact[i] = characteristics.get(i).toString();
                }
                service_list_view.setVisibility(View.GONE);
                characteristics_list_view.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, charact);
                characteristics_list_view.setAdapter(adapter);
            }
        });
        rescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iqvisBLE.ScanDevices();
                device_list_view.setVisibility(View.VISIBLE);
                service_list_view.setVisibility(View.GONE);
                characteristics_list_view.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        iqvisBLE.UnregisterReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iqvisBLE.RegisterBroadCastReceiver();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (View.VISIBLE == device_list_view.getVisibility()) {
            finish();
        } else if (View.VISIBLE == service_list_view.getVisibility()) {
            iqvisBLE.DisconnectDevice();
            service_list_view.setVisibility(View.GONE);
            device_list_view.setVisibility(View.VISIBLE);
        } else if (View.VISIBLE == characteristics_list_view.getVisibility()) {
            characteristics_list_view.setVisibility(View.GONE);
            service_list_view.setVisibility(View.VISIBLE);
        }
    }

    private void runTimePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    | checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    | checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                    | checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(AppConstants.runTimePermissions, AppConstants.ACCESS_FINE_LOCATION_PERMISSION);
            }
        }
    }

    @Override
    public void onScanReturn(final List<BluetoothDevice> devices) {
        scannedDevices = devices;
        ScanCallBack callBack = null;
        if (devices.size() == 0) {
            list_text.setText(getString(R.string.no_device_found));
        } else {
            list_text.setText(getString(R.string.devices));
            device_list_view.setVisibility(View.VISIBLE);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> BLEDevices = new ArrayList<>(devices.size());
                    for (int i = 0; i < devices.size(); i++) {
                        BLEDevices.add(devices.get(i).getName());
                    }
                    DeviceListAdapter DeviceListAdapter = new DeviceListAdapter(BLEDevices, MainActivity.this);
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, BLEDevices);
                    device_list_view.setAdapter(DeviceListAdapter);
                    Toast.makeText(MainActivity.this, devices.get(0).getAddress(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        try {
            if(callBack!=null)
            callBack.onScanReturn(devices);
        } catch (NullPointerException e) {
e.printStackTrace();
        }
    }

    @Override
    public void onConnected() {
        Toast.makeText(getApplicationContext(), "Connect", Toast.LENGTH_SHORT).show();
//        list_text.setText("Connected");
        device_list_view.setVisibility(View.GONE);
        service_list_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnServicesDiscovered(List<BluetoothGattService> services) {
        servicesList = services;
        String[] serv = new String[services.size()];
        for (int i = 0; i < services.size(); i++) {
            serv[i] = services.get(i).toString();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, serv);
        service_list_view.setAdapter(adapter);
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(getApplicationContext(), "Disconnect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void readRssiValues(int val) {
    }
    @Override
    public void onScanReturn(BluetoothDevice bluetoothDevice, final int rssi, final byte[] bytes) {
    }
    @Override
    public void onScanReturnBLE(List<BLEDevice> bluetoothDevice,List<BluetoothDevice> devices){
    }
    @Override
    public void readCharacteristicValue(String value){
    }

    @Override
    public void readDescriptorValue(String value) {

    }

    @Override
    public void notifyCharacteristicValue(String value){
    }
}