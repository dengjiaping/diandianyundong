package com.fox.exercise;


import java.util.Timer;

import com.fox.exercise.api.ApiBack;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;

import android.app.Service;
import android.content.Intent;

import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class SportServices extends Service {

    private SportBinder mBinder = null;


    private SportExceptionHandler mExceptionHandler = null;

    private Timer mSportTimer = null;

    private static final String TAG = "SportServices";

    private boolean isOperator = true;

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "sportService onCreate");
        mBinder = new SportBinder();
        mExceptionHandler = new SportExceptionHandler(this);

        new SportTask().start();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "sportService startId");
    }


    class SportTask extends Thread {

        @Override
        public void run() {
            //TODO
            //	mLocate.getLocation();
            SporterBundle bundle = SporterBundle.getInstance();
            while (isOperator) {
                Log.e(TAG, "w lai l");
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    ApiBack back = ApiJsonParser.uploadSportsType(bundle.getSessionId(), bundle.getSportsType(),
                            bundle.getScoreStep(), bundle.getScoreCalories(),
                            bundle.getSteps(), bundle.getCalories());
                    Log.e(TAG, "back flag=" + back.getFlag() + ";back=" + back.toString());
                } catch (ApiNetException e) {
                    e.printStackTrace();
                    Message.obtain(mExceptionHandler, SportExceptionHandler.NET_ERROR).sendToTarget();
                } catch (ApiSessionOutException e) {
                    e.printStackTrace();
                    Message.obtain(mExceptionHandler, SportExceptionHandler.SESSION_OUT).sendToTarget();
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isOperator = false;
        Log.d(TAG, "sportService onDestroy");
    }

}
