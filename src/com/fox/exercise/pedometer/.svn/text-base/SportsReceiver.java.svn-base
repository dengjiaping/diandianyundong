package com.fox.exercise.pedometer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;

import com.fox.exercise.MainFragmentActivity;
import com.fox.exercise.R;
import com.fox.exercise.login.SportMain;

public class SportsReceiver extends BroadcastReceiver {
    /**
     * 接受定时任务
     */

    private static final String TAG = "SportsReceiver";
    private Vibrator vibrator = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 启动震动，并持续指定的时间vibrator.vibrate(3500)/单位毫秒;
        // Date date = new Date();
        // SimpleDateFormat sdf = new SimpleDateFormat("HH");
        // String hour = sdf.format(date);
        // int data = Integer.parseInt(hour);
        // Log.e("data>>>>",data+"");
        // if (data >= 8 && data <= 17) {
        // vibrator = (Vibrator)
        // context.getSystemService(Service.VIBRATOR_SERVICE);
        // vibrator.vibrate(new long[] { 1000, 100, 50, 100, 50 }, -1);
        // Toast.makeText(context, "你已经一个小时没有运动了,快活动一下吧!",
        // Toast.LENGTH_LONG).show();
        // }

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.icon, "你已经一个小时没有运动了,快活动一下吧!", System.currentTimeMillis());
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        context = context.getApplicationContext();
        CharSequence contentTitle = "云狐运动";
        CharSequence contentText = "你已经一个小时没有运动了,快活动一下吧!";
        SharedPreferences sharedPreferences = context.getSharedPreferences("sports", Context.MODE_PRIVATE);
        Boolean isHome = sharedPreferences.getBoolean("isHome", false);
        Intent notificationIntent;
        if (isHome) {
            notificationIntent = new Intent(context, MainFragmentActivity.class);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        } else {
            notificationIntent = new Intent(context, SportMain.class);
            notificationIntent.setAction(Intent.ACTION_MAIN);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

        // 用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(0, notification);
    }
}
