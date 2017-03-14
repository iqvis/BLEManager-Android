package com.iqvis.blecomponent.utils;


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
public class GATTConstants {

//    public static String TX_POWER = "00001804-0000-1000-8000-00805f9b34fb";
//    static final int service1_tx_uuid[]= {0x71, 0x3D, 0, 3, 0x50, 0x3E, 0x4C, 0x75, 0xBA, 0x94, 0x31, 0x48, 0xF1, 0x8D, 0x94, 0x1E};
//    public static String TX_POWER = Utility.getGuidFromByteArray();
    public static final int DISCONNECT_DEVICE=200;
    public static final int DISCONNECT_SERVICE=100;
    public static final int BUZZER_CLOSE_DELAY=100;
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
//    public static String Tx_Power_Level = "713D0002-503E-4C75-BA94-3148F18D941E";
    public static final byte ALERT_HIGH = 0x01;
    public static final byte ALERT_LOW =  0x00;
    public static final UUID MAIN_SERVICE = UUID.fromString("713D0000-503E-4C75-BA94-3148F18D941E");
    public static final UUID ALERT_LEVEL_UUID = UUID.fromString("713D0003-503E-4C75-BA94-3148F18D941E");
    public static final UUID TX = UUID.fromString("713D0002-503E-4C75-BA94-3148F18D941E");
    public static final long SCAN_PERIOD = 15000;
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String DESCRIPTOR_VALUE="descriptor";

}
