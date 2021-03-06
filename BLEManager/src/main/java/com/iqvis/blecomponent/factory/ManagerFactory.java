package com.iqvis.blecomponent.factory;

import android.annotation.SuppressLint;

import com.iqvis.blecomponent.manager.BLEScanManager;
import com.iqvis.blecomponent.manager.BLEConnectionManager;

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
public class ManagerFactory {

    private static BLEScanManager bleScanManager = null;
    @SuppressLint("StaticFieldLeak")
    private static BLEConnectionManager bleConnectionManager=null;

    public static BLEConnectionManager getBleConnectionManager(){
        if(bleConnectionManager==null){
            bleConnectionManager= new BLEConnectionManager();
        }
        return bleConnectionManager;
    }
    public static BLEScanManager getBLEScanManager(){
        if(bleScanManager == null){
            bleScanManager = new BLEScanManager();
        }
        return bleScanManager;
    }

}

