package com.fox.exercise.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    /**
     * 接受定时任务
     */

    private static final String TAG = "AlarmReceiver";
    private int sportUid;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "o de shen ma");
        int step = StepService.mSteps;
        SportUtils.saveSportDB(context, step);
        SharedPreferences sharedPreferences = context.getSharedPreferences("sprots_uid", 0);
        sportUid = sharedPreferences.getInt("sportsUid", 0);
        SharedPreferences spf = context.getSharedPreferences("sports" + sportUid, 0);
        String session = spf.getString("session_id", "");
        SportUtils.uploadSport(context, session);
        spf.edit().putInt("last_step", 0).commit();
        StepService.mSteps = 0;
    }
}
