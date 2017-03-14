package com.iqvis.blecomponent.utils;


import android.content.Context;
import java.text.DecimalFormat;

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
public class Utility {

    private static Context context;
    public static void setContext(Context context) {
        Utility.context = context;
    }
    public static Context getContext() {
        return context;
    }

    private static DecimalFormat format = new DecimalFormat(".##");

    public static String getFormatedValue(double v) {
        return format.format(v);
    }

    public static double getFloatDistance(int rssi, int txPower) {
        /*
         * RSSI = TxPower - 10 * n * lg(d)
         * n = 2 (in free space)
         *
         * d = 10 ^ ((TxPower - RSSI) / (10 * n))
         */
        return (Math.pow(10d, ((double) txPower - rssi) / (10 * 2))) / 100000;
    }


}
