package com.iqvis.blecomponent.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.iqvis.blecomponent.R;
import com.iqvis.blecomponent.factory.ManagerFactory;
import com.iqvis.blecomponent.interfaces.ScanCallBack;
import com.iqvis.blecomponent.modules.BLEDevice;
import com.iqvis.blecomponent.utils.GATTConstants;
import com.iqvis.blecomponent.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IQVIS on 10/24/2016.
 */
/*
Copyright 2016 IQVIS. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
"http://www.apache.org/licenses/LICENSE-2.0"

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
limitations under the License.
 */
public class BLEScanManager {

    private final ArrayList<BluetoothDevice> list = new ArrayList<>();
    private final ArrayList<BLEDevice> BLElist = new ArrayList<>();

    private boolean mLeScanning = false;
    private BluetoothAdapter adapter;
    private ScanCallBack mScanCallBack;

    public void setContext(ScanCallBack scanCallBack) {
        mScanCallBack = scanCallBack;
    }

    /*
    This method is adding the device in ArrayList.
     */
    private final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, final int rssi, final byte[] bytes) {
            if (!list.contains(bluetoothDevice) && bluetoothDevice.getName() != null) {
                ScanCallBack scanCallBack = mScanCallBack;
                scanCallBack.onScanReturn(bluetoothDevice, rssi, bytes);
                list.add(bluetoothDevice);
                BLElist.add(new BLEDevice(bluetoothDevice.getName(), bluetoothDevice.getAddress(), rssi,bluetoothDevice));
            }
        }
    };

    public void startScan() {
        BLElist.clear();
        list.clear();
        try {
            if (ManagerFactory.getBLEScanManager().ismLeScanning()) {
                Toast.makeText(Utility.getContext(), Utility.getContext().getString(R.string.text_already_scanning), Toast.LENGTH_LONG).show();
            } else {
                if (!ManagerFactory.getBleConnectionManager().isConnected()) {
                }
                Toast.makeText(Utility.getContext(), Utility.getContext().getString(R.string.text_scan_start), Toast.LENGTH_SHORT).show();
                scanLeDevice(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    This method is scanning the devices.
     */
    private void scanLeDevice(final boolean enable) {
        BluetoothManager manager = (BluetoothManager) Utility.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = manager.getAdapter();
        if (enable) {
            long scan_period;
            scan_period = GATTConstants.SCAN_PERIOD;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLeScanning = false;
                    adapter.stopLeScan(leScanCallback);
                    ScanCallBack scanCallBack = mScanCallBack;
                    scanCallBack.onScanReturn(getDeviceEntities());
                    scanCallBack.onScanReturnBLE(getDeviceBLEEntities(),getDeviceEntities());
                }
            }, scan_period);
            mLeScanning = true;
            adapter.startLeScan(leScanCallback);
        } else {
            mLeScanning = false;
            adapter.stopLeScan(leScanCallback);
        }
    }
    public boolean ismLeScanning() {
        return mLeScanning;
    }
    private List<BluetoothDevice> getDeviceEntities() {
        return list;
    }

    private List<BLEDevice> getDeviceBLEEntities() {
        return BLElist;
    }
public void stopLEScan(){
    adapter.stopLeScan(leScanCallback);

}
}
