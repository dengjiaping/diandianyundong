package com.fox.exercise;

import java.io.File;

import com.fox.exercise.Util;
import com.fox.exercise.SportsMain;
import com.fox.exercise.SportsUtilities;
import com.fox.exercise.api.ApiJsonParser;
import com.fox.exercise.api.ApiNetException;
import com.fox.exercise.api.ApiSessionOutException;
import com.fox.exercise.api.entity.MsgCounts;
import com.fox.exercise.api.entity.UserDetail;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

public class SportsService extends Service {

    private String mSessionId = "";
    private static final String TAG = "SportsService";
    private UserDetail mUserDetail = null;
    private boolean mIsAdmin = false;

    //	private static int PUSH_CHECK_COUNT = 0;
    private static final String NOTIFICATION_SP = "notification_sp";
    private static final String NOTIFICATION_KEY = "notification_id";
    private static final String NOTIFICATION_LOCAL_KEY = "notification_local_id";
    public static final String NOTIFICATION_MSG_ID = "notification_msg_id";
    public static final String NOTIFICATION_USERBROWSE_ID = "notification_userbrowse_id";

    // private LocalBroadcastManager mBroadcasrManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");

        ClearThread clearThread = new ClearThread();
        clearThread.start();

        // mBroadcasrManager = LocalBroadcastManager.getInstance(this);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(SportsAction.LOGIN_ACTION);
        intentFilter.addAction(SportsAction.EXIT_ACTION);
        registerReceiver(serviceReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if (intent == null)
            mSessionId = "";
        try {
            mSessionId = intent.getStringExtra(SportsAction.SEESION_ID_KEY);
            mIsAdmin = intent.getBooleanExtra(SportsAction.IS_ADMIN_KEY, false);
        } catch (NullPointerException e) {
            e.printStackTrace();
            mSessionId = "";
        }
        if (mSessionId == null || "".equals(mSessionId)) {
            Log.e(TAG, "get session fail");
            ClearThread clearThread = new ClearThread();
            clearThread.start();
            return super.onStartCommand(intent, flags, startId);
        } else {
            Log.e(TAG, "get session success");
            (new RefreshAndBroadcastThread()).start();
        }
        return Service.START_STICKY;
    }

    class RefreshAndBroadcastThread extends Thread {
        @Override
        public void run() {
            if (mSessionId == null || "".equals(mSessionId))
                return;
            try {
                mUserDetail = ApiJsonParser.refreshRank(mSessionId);

                LocalBroadcastManager localBroadcasrManager = LocalBroadcastManager.getInstance(SportsService.this);
                Intent intent = new Intent();
                intent.setAction(SportsAction.DETAIL_ACTION);
                intent.putExtra(SportsAction.DETAIL_KEY, mUserDetail);

                Log.e(TAG, "mIsAdmin:" + mIsAdmin);
                if (mIsAdmin) {
                    int verify = ApiJsonParser.getExamine().size();
                    intent.putExtra(SportsAction.VERIFY_KEY, verify);
                } else {
                    intent.putExtra(SportsAction.VERIFY_KEY, -1);
                }
                localBroadcasrManager.sendBroadcast(intent);
            } catch (ApiNetException e) {
                e.printStackTrace();
            } catch (ApiSessionOutException e) {
                e.printStackTrace();
            }
        }

    }

    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            Log.e(TAG, "onReceive");

//			if (!mIsFirstLaunch) {
//				PUSH_CHECK_COUNT++;
//				if (PUSH_CHECK_COUNT >= 1) {
//					PUSH_CHECK_COUNT = 0;
//					(new PushCheckThread()).start();
//				}
//			} else {
//				if (mFirstLaunch == 0L) {
//					mFirstLaunch = getSharedPreferences(SportsService.NOTIFICATION_SP, MODE_PRIVATE).getLong(
//							SportsService.NOTIFICATION_FIRST_TIME_KEY, 0L);
//				}
//				if (mFirstLaunch != 0L) {
//					long dTime = System.currentTimeMillis() - mFirstLaunch;
//					Log.e(TAG, "dTime:" + dTime);
//					if (dTime > 86400000L)
//						mIsFirstLaunch = false;
//				}
//			}
            if (SportsAction.LOGIN_ACTION.equals(intent.getAction())) {
                mSessionId = intent.getStringExtra(SportsAction.SEESION_ID_KEY);
                mIsAdmin = intent.getBooleanExtra(SportsAction.IS_ADMIN_KEY, false);
            } else if (SportsAction.EXIT_ACTION.equals(intent.getAction())) {
                mSessionId = "";
                return;
            }

            (new RefreshAndBroadcastThread()).start();
        }
    };

    class PushCheckThread extends Thread {

        @Override
        public void run() {

            Log.e(TAG, "PushCheckThread");
            try {
                com.fox.exercise.api.entity.Notification notifycation = ApiJsonParser.getNotification();
                SharedPreferences sp = getSharedPreferences(NOTIFICATION_SP, MODE_PRIVATE);
                int oldId = sp.getInt(NOTIFICATION_KEY, -1);
                if (oldId == notifycation.getId()) {
                    Log.e(TAG, "same notification");
                    return;
                }
                sp.edit().putInt(NOTIFICATION_KEY, notifycation.getId()).commit();
                Notification pushNotify = new Notification(R.drawable.icon, notifycation.getMsg(),
                        System.currentTimeMillis());
                pushNotify.flags = Notification.FLAG_AUTO_CANCEL;
                Intent intent = new Intent();
                if (notifycation.getMsgId() == 18) {
//					intent.setClass(SportsService.this, YunHuWebViewActivity.class);
//					intent.putExtra(NOTIFICATION_MSG_ID, notifycation.getMsgId());
//					Log.e(TAG, "###uid:" + notifycation.getUid());
//					intent.putExtra(NOTIFICATION_USERBROWSE_ID, notifycation.getUid());
                } else {
                    intent.setClass(SportsService.this, SportsMain.class);
                    intent.putExtra(NOTIFICATION_MSG_ID, notifycation.getMsgId());
                    Log.e(TAG, "###uid:" + notifycation.getUid());
                    intent.putExtra(NOTIFICATION_USERBROWSE_ID, notifycation.getUid());
                }

                int localId = sp.getInt(NOTIFICATION_LOCAL_KEY, 0);
                PendingIntent pendingIntent = PendingIntent.getActivity(SportsService.this, ++localId, intent,
                        PendingIntent.FLAG_ONE_SHOT);
                sp.edit().putInt(NOTIFICATION_LOCAL_KEY, localId).commit();
                pushNotify.setLatestEventInfo(SportsService.this, getString(R.string.sports_name),
                        notifycation.getMsg(), pendingIntent);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(0, pushNotify);

            } catch (ApiNetException e) {
                e.printStackTrace();
            }
        }
    }

    class ClearThread extends Thread {

        @Override
        public void run() {

            File f = new File(SportsUtilities.RECYCLE_PATH);
            if (f.exists()) {
                File[] files = f.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i] != null) {
                            files[i].delete();
                        } else {
                            continue;
                        }
                    }
                }
            }
            if (f != null)
                f.delete();
            f = new File("/sdcard/Recording/");
            if (f.exists()) {
                File[] files = f.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i] != null) {
                            files[i].delete();
                        } else {
                            continue;
                        }
                    }
                }
            }
            if (f != null)
                f.delete();

            f = new File(Util.CAMERA_MANAGER_BUCKET_NAME + "/MTZS/.temp");
            if (f.exists()) {
                File[] files = f.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i] != null) {
                            files[i].delete();
                        } else {
                            continue;
                        }
                    }
                }
            }
            if (f != null)
                f.delete();
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
