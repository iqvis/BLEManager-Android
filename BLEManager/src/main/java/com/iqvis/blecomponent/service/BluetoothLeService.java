package com.iqvis.blecomponent.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.iqvis.blecomponent.utils.GATTConstants;

import java.util.List;
import java.util.UUID;

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
@SuppressWarnings("DefaultFileTemplate")
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = GATTConstants.STATE_DISCONNECTED;

    private static final UUID TX_POWER = GATTConstants.MAIN_SERVICE;
    private final static UUID UUID_TX_POWER_LEVEL = GATTConstants.TX;
    public final static String ACTION_GATT_CONNECTED =
            "com.iqvis.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.iqvis.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.iqvis.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.iqvis.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.iqvis.bluetooth.le.EXTRA_DATA";
    public final static String NOTIFY_DATA =
            "com.iqvis.bluetooth.le.NOTIFY_DATA";
    public static final String EXTRA_RSSI = "com.iqvis.bluetooth.le.EXTRA_DATA_RSSI";
    public static final String EXTRA_RSSI_VALUE = "com.iqvis.bluetooth.le.EXTRA_DATA_RSSI_VALUE";
    public static final String ACTION_DESCRIPTOR_VALUE_AVAILABLE="com.iqvis.bluetooth.le.DESCRIPTOR_VALUE";
    // Implements callback methods for GATT events that the app cares about.  For example,
// connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("STATE CONNECTED", "OK");
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = GATTConstants.STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i("DISCOVERING SERVICES", "OK");
                // Attempts to discover services after successful connection.
                mBluetoothGatt.discoverServices();
                Log.i("AFTER DISCOVER SERVICES", "OK");

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("STATE DISCONNECTED", "OK");
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = GATTConstants.STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
                Log.i("STATE DISCONNECTED", "OK");
            }
            if (mConnectionState == 123334) {
                Log.d(TAG, mConnectionState + "");
            }
        }
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                Log.i("GATT SUCCESS", "OK");
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
//            super.onReadRemoteRssi(gatt, rssi, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.i("RSSI",rssi+"");
                RSSI_VALUE = rssi;

                broadcastUpdate(ACTION_DATA_AVAILABLE, rssi);
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {

                broadcastUpdate(gatt.getDevice().getAddress(), EXTRA_RSSI, descriptor);
//                broadcastUpdate(EXTRA_RSSI, descriptor);
//                Log.i("Checking",characteristic.getValue().length+"");
            } else {
                Log.d("Value", "failed()");

            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

                broadcastUpdate(gatt.getDevice().getAddress(), EXTRA_RSSI, characteristic);
                broadcastUpdate(EXTRA_RSSI, characteristic);
//                Log.i("Checking",characteristic.getValue().length+"");
            } else {
                Log.d("Value", "failed()");

            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
//            Log.i("123",characteristic.getValue().toString());
            broadcastUpdate(gatt.getDevice().getAddress(), NOTIFY_DATA, characteristic);
        }
    };

    public int isConnected() {
        return mConnectionState;
    }

    private int RSSI_VALUE = 0;

    private void broadcastUpdate(final String action, int value) {
        final Intent intent = new Intent(action);
        intent.putExtra("rssi", true);
        intent.putExtra(EXTRA_RSSI_VALUE, value);
        sendBroadcast(intent);
        Log.i("INTENT SENT", "OK");
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
        Log.i("INTENT SENT", "OK");
    }

    public void writeAlertLevel(byte level) {
        BluetoothGattService alertService = mBluetoothGatt.getService(GATTConstants.MAIN_SERVICE);
        if (alertService == null) {
            Log.d(TAG, "Immediate Alert service not found!");
            return;
        }
        BluetoothGattCharacteristic alertLevel = alertService.getCharacteristic(GATTConstants.ALERT_LEVEL_UUID);
        if (alertLevel == null) {
            Log.d(TAG, "Alert Level characteristic not found!");
            return;
        }
        byte buf[] = new byte[]{(byte) 0x01, level, (byte) 0x00};
        alertLevel.setValue(buf);
        mBluetoothGatt.writeCharacteristic(alertLevel);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));
            intent.putExtra(EXTRA_DATA, new String(data) + stringBuilder.toString());
        }
        sendBroadcast(intent);

    }
    private void broadcastUpdate(final String address, final String action, final BluetoothGattDescriptor characteristic) {
        final Intent intent = new Intent(action);
        intent.putExtra(GATTConstants.DESCRIPTOR_VALUE, true);
        intent.putExtra(GATTConstants.EXTRAS_DEVICE_ADDRESS, address);
        intent.setAction(BluetoothLeService.ACTION_DESCRIPTOR_VALUE_AVAILABLE);
        // For all other profiles, writes the data formatted in HEX.

            final byte[] data = characteristic.getValue();
            //  Log.d("YOO", "\n" + data.length + "");
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                // Log.d("Read", new String(data) + "\n" + stringBuilder.toString());
                intent.putExtra(EXTRA_DATA, new String(data) + "" + stringBuilder.toString());
            }

        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String address, final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        intent.putExtra("rssi", false);
        intent.putExtra(GATTConstants.EXTRAS_DEVICE_ADDRESS, address);
        // For all other profiles, writes the data formatted in HEX.
        if (characteristic.getUuid().equals(UUID_TX_POWER_LEVEL)) {
            int flag = characteristic.getProperties();
            Log.i("Tx_Power_pro", flag + "");
            int format;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
            }
            int txPower = characteristic.getIntValue(format, 0);
            Log.d("POWER", String.valueOf(txPower));
            intent.putExtra(EXTRA_DATA, txPower);
            intent.putExtra(EXTRA_RSSI_VALUE, RSSI_VALUE);
        } else {
            final byte[] data = characteristic.getValue();
            //  Log.d("YOO", "\n" + data.length + "");
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                // Log.d("Read", new String(data) + "\n" + stringBuilder.toString());
                intent.putExtra(EXTRA_DATA, new String(data) + "" + stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = GATTConstants.STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mBluetoothGatt = device.connectGatt(this, false, mGattCallback, BluetoothDevice.TRANSPORT_AUTO);
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback);

        } else {
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        }
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = GATTConstants.STATE_CONNECTING;

        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void read_RSSI() {

        if(mBluetoothGatt!=null)
        mBluetoothGatt.readRemoteRssi();
    }

    public void readTX_POWER() {
        BluetoothGattService service = mBluetoothGatt.getService(TX_POWER);
        if (service == null) {
            Log.i("Checked", "SERVER NULL");
        } else {
            BluetoothGattCharacteristic tx_characteristics = service.getCharacteristic(UUID_TX_POWER_LEVEL);
            if (tx_characteristics == null) {
                Log.i("Checked", "Characteristics-null");
            } else {
//            Log.i("Checked","tx");

                mBluetoothGatt.readCharacteristic(tx_characteristics);
//            mBluetoothGatt.setCharacteristicNotification(tx_characteristics,true);
            }
//        Log.i("Checked","tx-not-null");
        }
    }

    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given  device, the app must call this method to ensure resources are
     * released properly.
     */
    private void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }


//    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//        mBluetoothGatt.readCharacteristic(characteristic);
//
//    }


//    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
//                                              boolean enabled) {
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
//    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;
        return mBluetoothGatt.getServices();
    }

    public float readCharacteristicsValue(BluetoothGattCharacteristic characteristic) {
        if(mBluetoothGatt != null)
            mBluetoothGatt.readCharacteristic(characteristic);
        return 0;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public boolean WriteCharacteristics(BluetoothGattCharacteristic characteristic) {
        try {
            if(mBluetoothGatt != null)
            return mBluetoothGatt.writeCharacteristic(characteristic);
        } catch (Exception e) {
            e.printStackTrace();
        return false;
        }
        return false;
    }

    public void ReadDescriptor(BluetoothGattDescriptor descriptor){
        mBluetoothGatt.readDescriptor(descriptor);
    }

    public void GetNotification(BluetoothGattCharacteristic characteristic, boolean enabled){
//        UUID uuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
//        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
//        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(uuid);
//        descriptor.setValue((enabled) ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
//                : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
//        mBluetoothGatt.writeDescriptor(descriptor);
  boolean b=  setCharacteristicNotification(characteristic,enabled);
    Log.i("RESULT",b+"");
    }

    private static final UUID CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                                  boolean enable) {
//        if (IS_DEBUG)
//            Log.d(TAG, "setCharacteristicNotification(device=" + device.getName() + device.getAddress() + ", UUID="
//                    + characteristicUuid + ", enable=" + enable + " )");
//        BluetoothGatt gatt = mGattInstances.get(device.getAddress()); //I just hold the gatt instances I got from connect in this HashMap
       boolean b= mBluetoothGatt.setCharacteristicNotification(characteristic, enable);
        Log.i("Notification",b+"");
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID);
        descriptor.setValue(enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : new byte[]{0x00, 0x00});
        return mBluetoothGatt.writeDescriptor(descriptor); //descriptor write operation successfully started?
    }
}
