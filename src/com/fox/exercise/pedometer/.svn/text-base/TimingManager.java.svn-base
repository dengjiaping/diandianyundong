package com.fox.exercise.pedometer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class TimingManager {
    /**
     * 循环执行定时任务
     * 发送广播,执行后台的扫描数据
     */

    private static TimingManager mTimingManager = null;
    private Context mContext = null;
    private static final int BASE_DELAY = 1000; //延迟多长时间执行

    private TimingManager(Context context) {
        this.mContext = context;
    }

    public static TimingManager getInstance(Context context) {
        if (mTimingManager == null) {
            mTimingManager = new TimingManager(context);
        }
        return mTimingManager;
    }

    //定时循环执行,执行完之后发送广播到AlarmReceive
    public synchronized void repeatTiming() {
        Intent intent = new Intent("com.fox.exercise.pedometer.AlarmReceiver.alarmclock");
        intent.setClass(mContext, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += SportUtils.diffTime(); //第一次开启的时候延迟1秒
        //	firstTime = 1 * 1000;
        //long interval = BASE_DELAY * 20; //一天
        long interval = BASE_DELAY * 60 * 60 * 24; //一天
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, interval, sender);
    }

    //取消定时
    public synchronized void cancleRepeatTiming() {
        Intent intent = new Intent("com.fox.exercise.pedometer.AlarmReceiver.alarmclock");
        intent.setClass(mContext, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }


    //定时循环执行,执行完之后发送广播到AlarmReceive
    public synchronized void repeatTimingOneHour() {
        Intent intent = new Intent("com.fox.exercise.pedometer.SportsReceiver.alarmclock");
        intent.setClass(mContext, SportsReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        long interval = BASE_DELAY * 60 * 60; //1小时
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60 * 60 * 1000, interval, sender);
    }

    //取消定时
    public synchronized void cancleRepeatTimingOneHour() {
        Intent intent = new Intent("com.fox.exercise.pedometer.SportsReceiver.alarmclock");
        intent.setClass(mContext, SportsReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

    //定时循环执行,执行完之后发送广播
    public synchronized void repeatTimingFiveMinutes() {
        Intent intent = new Intent("com.fox.exercise.pedometer.TimerReceiver.alarmclock");
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        long interval = BASE_DELAY * 60 * 5; //5分钟
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, sender);
    }

    //取消定时
    public synchronized void cancleRepeatTimingFiveMinutes() {
        Intent intent = new Intent("com.fox.exercise.pedometer.TimerReceiver.alarmclock");
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

}
