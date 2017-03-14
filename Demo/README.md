# BLEComponent
`BLEComponent` is Bluetooth Low Energy wrapper that scans BLE devices and pair with the BLE device to discover their services, characteristics, and descriptors. It can be used to get RSSI value periodically and scan a specific vendor device by their service UUID.
## Requirements
` BLEComponent` works on BLE supported devices with following Android OS.
### Mobile and Tablet
* OS 5.0 and Plus


## Adding `BLEComponent` to your project
### Source Files
`BLEComponent` have many files that can be helpul to scan,pair,discover services,characteristics and descriptors.
* Open your project in Android studio.
* Import `BLEComponent` as a module in your Android Studio.
* Right click on your project and select the option “Open Module Setting”. Now you can see the project structure. 
* Select your project from left side bar under Modules title and multiple option will be open. 
* Click on dependencies from top bar. 
* Click the on “+” button and select the option “Module dependency” from right side bar. 
* Add the BLEComponent as a module.

## Usage
The main guideline you need to follow when dealing with `BLEComponent` before scanning
add following line of code to initiate ` BLEComponent` in that respective Activity where you want to scan BLE device
Create the object of `IqvisBLE` on class level
```javascript
// Create the class level object in Activity 
IqvisBLE iqvisBle;
// Write this line onCreate method of activity 
iqvisBLE= new IqvisBLE(ABC_Activity.this,this,this);

```

Register and UnRegister the receivers of `IqvisBLE` like this 
```javascript
@Override
protected void onPause() {
    super.onPause();
    iqvisBLE.UnregisterReciever();
}
@Override
protected void onResume() {
    super.onResume();
    iqvisBLE.RegisterBroadCastReciever();
}

```

### Implements the both `ScanCallBack` and `ConnectionCallBack` Inetrface on that activity in which you want to scan and connect the devices.   
There are following methods available for scanning 
```javascript
// Scan all nearby BLE Devices 
// scanning will stop automatically after 20 seconds
iqvisBLE.ScanDevices(); 

// You can get Scan Devices list from below method of `ScanCallBack` Interface.
onScanReturn(final List<BluetoothDevice> devices);
onScanReturn(BluetoothDevice bluetoothDevice, final int rssi, final byte[] bytes);
onScanReturnBLE(List<BLEDevice> bluetoothDevice,List<BluetoothDevice> devices);


```
Method for connection with BLE device.
```javascript
// To pair Bluetooth device 
// Pass the BluetoothDevice bleDevice in below method

iqvisBLE.ConnectDevice(bleDevice);

// Methods of `ConnectionCallBack` interface for connection request response onConnect or onDisconnected.    
@Override
public void onConnected() {
Toast.makeText(getApplicationContext(),"Device Connected", Toast.LENGTH_SHORT).show();
}
@Override
public void onDisconnected() {
Toast.makeText(getApplicationContext(),"Device Disconnected", Toast.LENGTH_SHORT).show();
}

// You can get Device Services list from below method. 
@Override
public void OnServicesDiscovered(List<BluetoothGattService> services) {
}

// You can get Rssi value of connect Device from below method. 
@Override
public void readRssiValues(int val) {
}

// You can read the characteristic value from below method. 
@Override
public void readcharacteristicValue(String value){
}

// You can notify the characteristic value from below method. 
@Override
public void notifyCharacteristicValue(String value){
}
```
## License 
`BLEManager` is distributed under the terms and conditions of the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)




