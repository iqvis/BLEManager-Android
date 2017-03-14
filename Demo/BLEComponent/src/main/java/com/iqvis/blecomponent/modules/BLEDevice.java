package com.iqvis.blecomponent.modules;

import android.bluetooth.BluetoothDevice;

/**
 * Created by IQVIS on 24/01/2017.
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
@SuppressWarnings("DefaultFileTemplate")
public class BLEDevice  {
    private String name,mac;
    private int rssi;
    private BluetoothDevice bluetoothDevice;

    public int getRssi() {
        return rssi;
    }

    public String getMac() {
        return mac;
    }

    public String getName() {
        return name;
    }

    public  BLEDevice(){

    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public  BLEDevice(String name, String  mac, int rssi,BluetoothDevice bluetoothDevice){
        this.name=name;
        this.mac=mac;
        this.rssi=rssi;
        this.bluetoothDevice=bluetoothDevice;
    }
}
