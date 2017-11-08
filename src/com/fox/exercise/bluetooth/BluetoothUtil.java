package com.fox.exercise.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.fox.exercise.R;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothUtil {
    // Debugging
    private static final String TAG = "BluetoothUtil";

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;

    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    //private BluetoothService mBluetoothService = null;

    private Activity mActivity;

    public BluetoothUtil(Activity activity) {
        this.mActivity = activity;
    }

    public void onCreate() {

        /**
         * 确认设备是否支持蓝牙 如果getDefaultAdapter()返回null,则这个设备不支持蓝牙
         */
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        // 手机不支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(mActivity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void onStart() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(enableIntent, 3);
        }
    }

}
