package com.iqvis.blecomponent.manager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.iqvis.blecomponent.R;
import com.iqvis.blecomponent.service.BluetoothLeService;
import com.iqvis.blecomponent.factory.ManagerFactory;
import com.iqvis.blecomponent.utils.GATTConstants;

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

public class BLEConnectionManager {

    private Activity activity;
    private String deviceMac;
    private BluetoothLeService bluetoothLeService;

    public BLEConnectionManager init(Activity activity) {
        this.activity = activity;
        return ManagerFactory.getBleConnectionManager();
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    /*
    This method start the Le Service.
    */
    public void startingLeService() {
        Intent serviceIntent = new Intent(activity, BluetoothLeService.class);
        if (serviceConnection != null)
            activity.bindService(serviceIntent, serviceConnection, activity.BIND_AUTO_CREATE);

    }

    public void readRSSI() {
        if (bluetoothLeService != null) {
            bluetoothLeService.read_RSSI();
        }
    }

//    public void readTx_power(){
//        bluetoothLeService.readTX_POWER();
//    }

//    public void buzzer(byte level) {
//        bluetoothLeService.writeAlertLevel(level);
//    }

    /*
    This method show the state with Buoy Device.
    */
    public boolean isConnected() {

        boolean isConnected = false;
        if (bluetoothLeService == null) {
            isConnected = false;
        } else if (bluetoothLeService.isConnected() == GATTConstants.STATE_CONNECTED) {
            isConnected = true;
        } else if (bluetoothLeService.isConnected() == GATTConstants.STATE_DISCONNECTED) {
            isConnected = false;
        }
        return isConnected;
    }

    public void disconnectDevice() {
        if (bluetoothLeService != null)
            bluetoothLeService.disconnect();
    }

    private static IntentFilter getGattIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        filter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        filter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        filter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        filter.addAction(BluetoothLeService.EXTRA_RSSI);
        filter.addAction(BluetoothLeService.NOTIFY_DATA);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothLeService.ACTION_DESCRIPTOR_VALUE_AVAILABLE);
        return filter;
    }
//    private static IntentFilter getFilters(){
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        return filter;
//    }

    public void onPause(BroadcastReceiver broadcastReceiver) {
        activity.unregisterReceiver(broadcastReceiver);
    }

    public void onResume(BroadcastReceiver broadcastReceiver) {
        activity.registerReceiver(broadcastReceiver, getGattIntentFilter());

    }

    public void onDestroy() {
        activity.unbindService(serviceConnection);
        bluetoothLeService = null;
    }

    public BluetoothLeService getBluetoothLeService() {
        return bluetoothLeService;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) iBinder).getService();
            if (!bluetoothLeService.initialize()) {
                Toast.makeText(activity, activity.getString(R.string.text_ble_not_enabled), Toast.LENGTH_SHORT).show();
//                finish();
            } else {
//                Timer mTimer = new Timer();
//                mTimer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        if(deviceMac!=null) {
//                            try {
                bluetoothLeService.connect(deviceMac);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                },2000);
//                try {
//                    (new Thread() {
//                        public void run() {
//                            while (true) {
//                                // do stuff
//                                ManagerFactory.getBleConnectionManager().readRSSI();
//                            }
//                        }
//                    }).start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bluetoothLeService = null;
        }
    };

//    public BLEConnectionManager LED(){
//        return ManagerFactory.getBleConnectionManager();
//    }

//    public BLEConnectionManager connectDevice(){
//        bluetoothLeService.connect(deviceMac);
//        return ManagerFactory.getBleConnectionManager();
//    }
}
