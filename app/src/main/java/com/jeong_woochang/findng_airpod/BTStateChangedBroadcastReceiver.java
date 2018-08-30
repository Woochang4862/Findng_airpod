package com.jeong_woochang.findng_airpod;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by jeong-woochang on 2018. 8. 30..
 */

public class BTStateChangedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                -1);

        switch (state) {
            case BluetoothAdapter.STATE_CONNECTED:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_CONNECTED",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_CONNECTING:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_CONNECTING",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_DISCONNECTED",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_DISCONNECTING",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_OFF:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_OFF",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_ON:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_ON",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_TURNING_OFF",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_TURNING_ON",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
