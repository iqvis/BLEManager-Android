package com.iqvis.blecomponent.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iqvis.blecomponent.factory.ManagerFactory;
import com.iqvis.blecomponent.interfaces.ConnectionCallBack;
import com.iqvis.blecomponent.interfaces.ScanCallBack;
import com.iqvis.blecomponent.service.BluetoothLeService;
import com.iqvis.blecomponent.utils.GATTConstants;
import com.iqvis.blecomponent.utils.Utility;

import java.util.List;

/**
 * Created by IQVIS on 11/01/2017.
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
public class IqvisBLE {
    private BluetoothLeService bluetoothLeService;
    private ScanCallBack scanCallBack;
    private ConnectionCallBack connectionCallBack;

    public IqvisBLE(Activity activity, ScanCallBack scanCallBack, ConnectionCallBack connectionCallBack) {
        this.scanCallBack = scanCallBack;
        this.connectionCallBack = connectionCallBack;
        Activity mActivity = activity;
        ManagerFactory.getBleConnectionManager().init(mActivity);
        ManagerFactory.getBLEScanManager().setContext(scanCallBack);
        Utility.setContext(mActivity);
//        RegisterBroadCastReceiver();
    }

    public void readChar(BluetoothGattCharacteristic chars) {
        bluetoothLeService.readCharacteristicsValue(chars);
    }

    public boolean WriteChar(BluetoothGattCharacteristic chars) {
     return    bluetoothLeService.WriteCharacteristics(chars);
    }



    public void readDescriptor( BluetoothGattDescriptor descriptor){
        bluetoothLeService.ReadDescriptor(descriptor);
    }

    private void closeDeviceConnection() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(GATTConstants.DISCONNECT_DEVICE);
                    try {
                        ManagerFactory.getBleConnectionManager().disconnectDevice();
                        Thread.sleep(GATTConstants.DISCONNECT_SERVICE);
                        unBindService();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void unBindService() {
        ManagerFactory.getBleConnectionManager().onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
//                case BluetoothAdapter.ACTION_STATE_CHANGED: {
//                    //Used New Code
//                    if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
//                        final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
//                                BluetoothAdapter.ERROR);
//                        switch (state) {
//                            case BluetoothAdapter.STATE_OFF:
//                                //OldCode
//                                if (!AppConstants.IS_CONNECT) {
//                                    Toast.makeText(mActivity, mActivity.getString(R.string.ble_not_Enabled), Toast.LENGTH_SHORT).show();
//                                }
//                                break;
//                        }
//                    }
//                    break;
//                }
                case BluetoothLeService.ACTION_DESCRIPTOR_VALUE_AVAILABLE:{
                      String value= intent.getExtras().getString(BluetoothLeService.EXTRA_DATA);
                    connectionCallBack.readDescriptorValue(value);
                    break;
                }
                case BluetoothLeService.ACTION_GATT_DISCONNECTED: {
                    connectionCallBack.onDisconnected();

                    break;
                }
                case BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED: {
                    List<BluetoothGattService> servicesList;
                    servicesList = bluetoothLeService.getSupportedGattServices();
                    if(connectionCallBack != null)
                    connectionCallBack.OnServicesDiscovered(servicesList);
                    break;
                }
                case BluetoothLeService.ACTION_DATA_AVAILABLE: {
                    if (intent.getExtras().getBoolean("rssi")) {
//                        int tx = intent.getIntExtra(BluetoothLeService.EXTRA_DATA, 0);
                        int rssi = intent.getIntExtra(BluetoothLeService.EXTRA_RSSI_VALUE, 0);
                        connectionCallBack.readRssiValues(rssi);
//                    Toast.makeText(context, rssi+"", Toast.LENGTH_SHORT).show();
//                        double floatV = Double.parseDouble(Utility.getFormatedValue(Utility.getFloatDistance(rssi, tx)));
                    }
                    break;
                }
                case BluetoothLeService.NOTIFY_DATA: {
                   String data=  intent.getExtras().getString(BluetoothLeService.EXTRA_DATA);
                    connectionCallBack.notifyCharacteristicValue(data);

                    break;
                }
                case BluetoothLeService.EXTRA_RSSI: {

                    String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    connectionCallBack.readCharacteristicValue(data);
                    break;
                }
                case BluetoothLeService.ACTION_GATT_CONNECTED: {
                    bluetoothLeService = ManagerFactory.getBleConnectionManager().getBluetoothLeService();
                    connectionCallBack.onConnected();
                    break;
                }

            }
        }
    };

    public void ScanDevices() {
        ManagerFactory.getBLEScanManager().startScan();
    }

    public void DisconnectDevice() {
        closeDeviceConnection();
    }

    public void ConnectDevice(BluetoothDevice device) {
        try {
            String address = device.getAddress();
//            String name = device.getName();
            ManagerFactory.getBleConnectionManager().setDeviceMac(address);
            ManagerFactory.getBleConnectionManager().startingLeService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void RegisterBroadCastReceiver() {
        ManagerFactory.getBleConnectionManager().onResume(broadcastReceiver);
    }

    public void UnregisterReceiver() {
        ManagerFactory.getBleConnectionManager().onPause(broadcastReceiver);
    }


    public void GetNotified(BluetoothGattCharacteristic characteristic, boolean enabled){
        bluetoothLeService.GetNotification(characteristic, enabled);
    }
//    public void buzzerOff() {
//            ManagerFactory.getBleConnectionManager().buzzer(GATTConstants.ALERT_LOW);
//            try {
//                Thread.sleep(GATTConstants.BUZZER_CLOSE_DELAY);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//    }
//
//    public void buzzerOn() {
//        try {
//            ManagerFactory.getBleConnectionManager().buzzer(GATTConstants.ALERT_HIGH);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
