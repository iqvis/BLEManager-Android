package com.iqvis.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

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
public class Utility {
    private static BluetoothDevice connectedDevice = null;

    private static Context context;

    public static void setContext(Context context) {
        Utility.context = context;
    }

    public static Context getContext() {
        return context;
    }


    /**
     * Calculate distance of device and return
     *
     * @param rssi
     * @param txPower
     * @return
     */
    public static double calculateAccuracy(int rssi, int txPower) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
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

    public static Bitmap getScaledBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public static DisplayMetrics getScreenResolution(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        (context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
//    public static String getGuidFromByteArray(int[] bytes)
//    {
//        ByteBuffer bb = ByteBuffer.wrap((by)bytes);
//        UUID uuid = new UUID(bb.getLong(), bb.getLong());
//        return uuid.toString();
//    }

//    public static void showAlert(Context context) {
//        try {
//            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//            alertDialog.setTitle("Alert");
//            alertDialog.setContentView(R.layout.alert_dialog_single_btn);
//            alertDialog.setMessage(context.getString(R.string.no_new_buoy_device_found));
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void ShowDialog(Context context, int res, int layout) {
//        final AlertDialog dialog = new AlertDialog.Builder(context).create();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setContentView(R.layout.alert_dialog_single_btn);
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        Button cancel_btn = (Button) dialog.findViewById(R.id.button_ok_single);
//        TextView showContext = (TextView) dialog.findViewById(R.id.context_dialog);
//        showContext.setText(context.getResources().getString(res));
//        dialog.show();
//        cancel_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        try {
            AlertDialog.Builder keyBuilder = new AlertDialog.Builder(context);
            keyBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            keyBuilder.setView(layout);
            AlertDialog dialog = keyBuilder.create();
//           final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//           positiveButton.setGravity(Gravity.CENTER);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setConnectedDevice(BluetoothDevice device) {
        connectedDevice = device;
    }
private static Fragment camefromfragment=null;
    public static Fragment getCameFromFragment() {

        return camefromfragment;
    }
    public static void setCameFromFragment(Fragment fragment) {
        camefromfragment = fragment;
    }

    public static BluetoothDevice getConnectedDevice() {

        return connectedDevice;
    }
    /*
    This method is checking Bluetooth is enable on target Device or not.
    */
    public static boolean isBluetoothEnable() {
        boolean val = false;
        BluetoothManager manager;
        BluetoothAdapter adapter;
        if (com.iqvis.blecomponent.utils.Utility.getContext() != null) {
            try {
                manager = (BluetoothManager) com.iqvis.blecomponent.utils.Utility.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
                adapter = manager.getAdapter();
                if (adapter == null || !adapter.isEnabled())
                    val = false;
                else
                    val = true;
            } catch (NullPointerException ex) {
            }
        } else
            val = false;
        return val;
    }

}
