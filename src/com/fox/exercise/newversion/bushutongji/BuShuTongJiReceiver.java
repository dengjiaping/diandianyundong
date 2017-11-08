package com.fox.exercise.newversion.bushutongji;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BuShuTongJiReceiver extends BroadcastReceiver {

    private String TAG = "develop_debug";
    private String user_present = "android.intent.action.USER_PRESENT";
    private String restart_service = "com.fox.exercise.newversion.bushutongji.RESTART_SERVICE";
    private boolean service_running = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.e(TAG, "WatchDogReceiver action : " + intent.getAction());

        SharedPreferences data = context.getSharedPreferences("bushutongji.xml", Context.MODE_PRIVATE);
        if (data.getBoolean("stop_service", false)) {
            return;
        }
        service_running = false;

        if ((intent.getAction() != null) &&
                ((intent.getAction().equals(Intent.ACTION_TIME_TICK)) ||
                        (intent.getAction().equalsIgnoreCase(user_present)))) {
            //检查Service状态
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if ("com.fox.exercise.newversion.bushutongji.BuShuTongJiService".equals(service.service.getClassName())) {
                    service_running = true;
                    break;
                }
            }

            if (!service_running) {
                Intent restart = new Intent(context, BuShuTongJiService.class);
                context.startService(restart);
            }
        } else if ((intent.getAction() != null) && (intent.getAction().equalsIgnoreCase(restart_service))) {
            Intent restart = new Intent(context, BuShuTongJiService.class);
            context.startService(restart);
        }
    }
}