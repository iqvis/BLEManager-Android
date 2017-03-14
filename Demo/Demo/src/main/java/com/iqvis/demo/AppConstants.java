package com.iqvis.demo;

import android.Manifest;

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
public class AppConstants {

    public static final String[] runTimePermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN};
    public static final int ACCESS_FINE_LOCATION_PERMISSION=0;
    public static final int REQUEST_BLUETOOTH_ENABLED=1;
}
