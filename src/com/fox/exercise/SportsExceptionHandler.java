package com.fox.exercise;

import cn.ingenic.indroidsync.SportsApp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SportsExceptionHandler extends Handler {

    private Context mContext = null;

    private Toast toast = null;

    public static final int SESSION_OUT = 0x0001;
    public static final int NET_ERROR = 0x0002;

    public SportsExceptionHandler() {
        mContext = SportsApp.getContext();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SESSION_OUT:
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(mContext, mContext.getResources().getString(R.string.exception_session_out),
                        Toast.LENGTH_SHORT);
                toast.show();
                break;
            case NET_ERROR:
                toast = Toast.makeText(mContext, mContext.getResources().getString(R.string.acess_server_error),
                        Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }

}
