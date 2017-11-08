package com.fox.exercise.api;

import com.fox.exercise.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int config = context.getResources().getInteger(R.integer.config_withsync);
        if (config == 1) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Log.e("BootReceiver", "i have bootcompleted");
                ApiConstant.isservice = false;
                Intent newIntent = new Intent(context, WatchService.class);
                context.startService(newIntent);

            }
        }
    }
}