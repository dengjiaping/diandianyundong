package com.fox.exercise.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothUtilNew {
    // Debugging
    private static final String TAG = "BluetoothUtilNew";

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
    private BluetoothServiceNew mBluetoothServiceNew = null;
    private Handler mHandler;

    private Context mContext;

    public BluetoothUtilNew(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    public void onCreate() {

        /**
         * 确认设备是否支持蓝牙 如果getDefaultAdapter()返回null,则这个设备不支持蓝牙
         */
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        // 手机不支持蓝牙
        if (mBluetoothAdapter == null) {
            //Toast.makeText(mContext, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void onStart() {
        if (!mBluetoothAdapter.isEnabled()) {
        } else {
            if (mBluetoothServiceNew == null)
                setupChat();
        }
    }

    public synchronized void onResume() {
        if (mBluetoothServiceNew != null) {
            if (mBluetoothServiceNew.getState() == BluetoothServiceNew.STATE_NONE) {
                mBluetoothServiceNew.start();
            }

        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        mBluetoothServiceNew = new BluetoothServiceNew(mHandler);
        mOutStringBuffer = new StringBuffer("");
    }

    public void onDestroy() {
        if (mBluetoothServiceNew != null)
            mBluetoothServiceNew.stop();
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    // 自己发送给远端信息
    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBluetoothServiceNew.getState() != BluetoothServiceNew.STATE_CONNECTED) {
            //Toast.makeText(mContext, R.string.not_connected, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "toast");
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            Log.i(TAG, "message.length()" + message.length());
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBluetoothServiceNew.write(send);
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            Log.e(TAG, "SendMessage:" + message);
        }
    }

    // 开始连接设备
    public void connectDevice(boolean secure) {
        SharedPreferences sps = mContext.getSharedPreferences("sprots_uid", 0);
        int sportUid = sps.getInt("sportsUid", 0);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sports" + sportUid, 0);
        String address = sharedPreferences.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS, "");
        String name = sharedPreferences.getString(DeviceListActivity.EXTRA_DEVICE_NAME, "");
        if (!address.isEmpty() && mBluetoothServiceNew != null) {
            Log.v(TAG, "DEVICE NAME " + name);
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            mBluetoothServiceNew.connect(device, secure);
        } else {
            Log.e(TAG, "请先配对");
        }
    }
}

